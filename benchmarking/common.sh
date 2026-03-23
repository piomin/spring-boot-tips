#!/usr/bin/env bash

set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REPO_ROOT="$(cd "${PROJECT_ROOT}/.." && pwd)"
BENCHMARK_DIR="${PROJECT_ROOT}/benchmarking"
RESULTS_DIR="${BENCHMARK_DIR}/results"
ARTIFACT_ID="spring-boot-tips"
APP_VERSION="1.3-SNAPSHOT"
JAR_NAME="${ARTIFACT_ID}-${APP_VERSION}.jar"
TARGET_JAR="${PROJECT_ROOT}/target/${JAR_NAME}"
NATIVE_BINARY="${PROJECT_ROOT}/target/${ARTIFACT_ID}"
EXTRACT_DIR="${PROJECT_ROOT}/application"
RUNTIME_DIR="${PROJECT_ROOT}/runtime"
BENCH_PORT="${SERVER_PORT:-8080}"
LEYDEN_JAVA_HOME_DEFAULT="${REPO_ROOT}/.tools/jdk-leyden"
GRAALVM_HOME_DEFAULT="${REPO_ROOT}/.tools/graalvm-native"
READINESS_URL="http://127.0.0.1:${BENCH_PORT}/actuator/health"
STARTUP_RESULTS_FILE="${RESULTS_DIR}/startup.md"
RSS_RESULTS_FILE="${RESULTS_DIR}/rss.md"

log() {
  printf '[%s] %s\n' "$(date '+%H:%M:%S')" "$*"
}

die() {
  printf 'error: %s\n' "$*" >&2
  exit 1
}

ensure_results_dir() {
  mkdir -p "${RESULTS_DIR}"
}

leyden_java_home() {
  if [[ -n "${LEYDEN_JAVA_HOME:-}" ]]; then
    printf '%s\n' "${LEYDEN_JAVA_HOME}"
    return
  fi
  if [[ -x "${LEYDEN_JAVA_HOME_DEFAULT}/bin/java" ]]; then
    printf '%s\n' "${LEYDEN_JAVA_HOME_DEFAULT}"
    return
  fi
  if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
    printf '%s\n' "${JAVA_HOME}"
    return
  fi
  die "set LEYDEN_JAVA_HOME to a Project Leyden JDK"
}

java_cmd() {
  printf '%s/bin/java\n' "$(leyden_java_home)"
}

graalvm_home() {
  if [[ -n "${GRAALVM_HOME:-}" ]]; then
    printf '%s\n' "${GRAALVM_HOME}"
    return
  fi
  if [[ -x "${GRAALVM_HOME_DEFAULT}/bin/native-image" ]]; then
    printf '%s\n' "${GRAALVM_HOME_DEFAULT}"
    return
  fi
  if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/native-image" ]]; then
    printf '%s\n' "${JAVA_HOME}"
    return
  fi
  if command -v native-image >/dev/null 2>&1; then
    dirname "$(dirname "$(command -v native-image)")"
    return
  fi
  die "set GRAALVM_HOME to a GraalVM install with native-image"
}

mvn_cmd() {
  if command -v mvn >/dev/null 2>&1; then
    printf '%s\n' "mvn"
    return
  fi
  die "mvn is required"
}

jvm_flags() {
  "$(java_cmd)" -XX:+UnlockExperimentalVMOptions -XX:+PrintFlagsFinal -version 2>&1
}

detect_cache_strategy() {
  local flags
  flags="$(jvm_flags)"
  if grep -q 'AOTCacheOutput' <<<"${flags}"; then
    printf '%s\n' "aotcacheoutput"
    return
  fi
  if grep -q 'AOTMode' <<<"${flags}"; then
    printf '%s\n' "aotmode"
    return
  fi
  if grep -q 'CacheDataStore' <<<"${flags}"; then
    printf '%s\n' "cachedatastore"
    return
  fi
  if grep -q 'ArchiveClassesAtExit' <<<"${flags}"; then
    printf '%s\n' "cds"
    return
  fi
  printf '%s\n' "unsupported"
}

cache_artifacts() {
  case "$(detect_cache_strategy)" in
    aotcacheoutput|aotmode)
      printf '%s\n' "${RUNTIME_DIR}/app.aot"
      ;;
    cachedatastore)
      printf '%s\n%s\n' "${RUNTIME_DIR}/app.cds" "${RUNTIME_DIR}/app.cds.code"
      ;;
    cds)
      printf '%s\n' "${RUNTIME_DIR}/application.jsa"
      ;;
    *)
      die "no supported Leyden/CDS cache strategy found"
      ;;
  esac
}

build_app() {
  log "Building Spring Boot jar"
  (cd "${PROJECT_ROOT}" && "$(mvn_cmd)" -q -DskipTests package)
}

build_native_app() {
  log "Building GraalVM native image"
  (
    cd "${PROJECT_ROOT}" && \
    JAVA_HOME="$(graalvm_home)" \
    PATH="$(graalvm_home)/bin:${PATH}" \
    "$(mvn_cmd)" -q -Pnative -DskipTests native:compile
  )
}

ensure_target_jar() {
  [[ -f "${TARGET_JAR}" ]] || build_app
}

extract_app() {
  ensure_target_jar
  rm -rf "${EXTRACT_DIR}" "${RUNTIME_DIR}"
  mkdir -p "${EXTRACT_DIR}" "${RUNTIME_DIR}"
  log "Extracting ${TARGET_JAR}"
  (
    cd "${PROJECT_ROOT}" && \
    "$(java_cmd)" -Djarmode=tools -jar "${TARGET_JAR}" extract --layers --launcher --destination "${EXTRACT_DIR}"
  )
  for layer in dependencies spring-boot-loader snapshot-dependencies application; do
    if [[ -d "${EXTRACT_DIR}/${layer}" ]]; then
      cp -a "${EXTRACT_DIR}/${layer}/." "${RUNTIME_DIR}/"
    fi
  done
}

wait_for_http() {
  local url="${1}"
  local attempts="${2:-300}"
  local delay="${3:-0.05}"
  for _ in $(seq 1 "${attempts}"); do
    if curl -fsS "${url}" >/dev/null 2>&1; then
      return 0
    fi
    sleep "${delay}"
  done
  return 1
}

cache_create_flags() {
  case "$(detect_cache_strategy)" in
    aotcacheoutput)
      printf '%s\n' "-XX:AOTCacheOutput=${RUNTIME_DIR}/app.aot -Dspring.context.exit=onRefresh"
      ;;
    aotmode)
      printf '%s\n' "-XX:AOTMode=record -XX:AOTConfiguration=${RUNTIME_DIR}/app.aotconf -Dspring.context.exit=onRefresh"
      ;;
    cachedatastore)
      printf '%s\n' "-Xshare:off -XX:CacheDataStore=${RUNTIME_DIR}/app.cds -Dspring.context.exit=onRefresh"
      ;;
    cds)
      printf '%s\n' "-Xshare:off -XX:ArchiveClassesAtExit=${RUNTIME_DIR}/application.jsa -Dspring.context.exit=onRefresh"
      ;;
    *)
      die "unsupported cache strategy"
      ;;
  esac
}

cache_finalize_if_needed() {
  case "$(detect_cache_strategy)" in
    aotmode)
      log "Assembling app.aot from recorded configuration"
      (
        cd "${RUNTIME_DIR}" && \
        "$(java_cmd)" \
          -XX:AOTMode=create \
          "-XX:AOTConfiguration=${RUNTIME_DIR}/app.aotconf" \
          "-XX:AOTCache=${RUNTIME_DIR}/app.aot" \
          -Dspring.context.exit=onRefresh \
          org.springframework.boot.loader.launch.JarLauncher
      )
      rm -f "${RUNTIME_DIR}/app.aotconf"
      ;;
  esac
}

run_mode_flags() {
  case "${1}" in
    plain|extracted|native)
      printf '%s\n' ""
      ;;
    aot)
      case "$(detect_cache_strategy)" in
        aotcacheoutput|aotmode)
          printf '%s\n' "-XX:AOTCache=${RUNTIME_DIR}/app.aot"
          ;;
        cachedatastore)
          printf '%s\n' "-XX:CacheDataStore=${RUNTIME_DIR}/app.cds"
          ;;
        cds)
          printf '%s\n' "-XX:SharedArchiveFile=${RUNTIME_DIR}/application.jsa"
          ;;
        *)
          die "unsupported cache strategy"
          ;;
      esac
      ;;
    *)
      die "unsupported run mode: ${1}"
      ;;
  esac
}

run_mode_dir() {
  case "${1}" in
    plain|native)
      printf '%s\n' "${PROJECT_ROOT}"
      ;;
    extracted|aot)
      printf '%s\n' "${RUNTIME_DIR}"
      ;;
    *)
      die "unsupported run mode: ${1}"
      ;;
  esac
}

run_mode_target() {
  case "${1}" in
    plain)
      printf '%s\n' "jar:${TARGET_JAR}"
      ;;
    extracted|aot)
      printf '%s\n' "main:org.springframework.boot.loader.launch.JarLauncher"
      ;;
    native)
      printf '%s\n' "bin:${NATIVE_BINARY}"
      ;;
    *)
      die "unsupported run mode: ${1}"
      ;;
  esac
}

ensure_extract_ready() {
  case "${1}" in
    extracted|aot)
      [[ -d "${RUNTIME_DIR}" && -f "${RUNTIME_DIR}/org/springframework/boot/loader/launch/JarLauncher.class" ]] || die "missing extracted runtime layout; run benchmarking/prepare-aot.sh first"
      ;;
  esac
}

ensure_cache_ready() {
  case "${1}" in
    aot)
      while IFS= read -r artifact; do
        [[ -f "${artifact}" ]] || die "missing cache artifact ${artifact}; run benchmarking/prepare-aot.sh first"
      done < <(cache_artifacts)
      ;;
  esac
}

ensure_native_ready() {
  case "${1}" in
    native)
      [[ -x "${NATIVE_BINARY}" ]] || die "missing native image ${NATIVE_BINARY}; run benchmarking/build-native.sh first"
      ;;
  esac
}
