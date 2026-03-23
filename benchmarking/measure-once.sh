#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

MODE="${1:-}"
[[ -n "${MODE}" ]] || die "usage: benchmarking/measure-once.sh <plain|extracted|aot|native>"

ensure_extract_ready "${MODE}"
ensure_cache_ready "${MODE}"
ensure_native_ready "${MODE}"

RUN_DIR="$(run_mode_dir "${MODE}")"
RUN_TARGET="$(run_mode_target "${MODE}")"
RUN_FLAGS="$(run_mode_flags "${MODE}")"
LOG_FILE="$(mktemp)"

cleanup() {
  if [[ -n "${PID:-}" ]] && kill -0 "${PID}" >/dev/null 2>&1; then
    kill "${PID}" >/dev/null 2>&1 || true
    wait "${PID}" >/dev/null 2>&1 || true
  fi
  rm -f "${LOG_FILE}"
}
trap cleanup EXIT

(
  cd "${RUN_DIR}"
  case "${RUN_TARGET}" in
    jar:*)
      exec "$(java_cmd)" ${RUN_FLAGS} -jar "${RUN_TARGET#jar:}"
      ;;
    main:*)
      exec "$(java_cmd)" ${RUN_FLAGS} "${RUN_TARGET#main:}"
      ;;
    bin:*)
      exec "${RUN_TARGET#bin:}"
      ;;
    *)
      die "unsupported run target ${RUN_TARGET}"
      ;;
  esac
) >"${LOG_FILE}" 2>&1 &
PID=$!

if ! wait_for_http "${READINESS_URL}" 800 0.02; then
  cat "${LOG_FILE}" >&2
  die "application did not become healthy in mode ${MODE}"
fi

kill "${PID}" >/dev/null 2>&1 || true
wait "${PID}" >/dev/null 2>&1 || true
