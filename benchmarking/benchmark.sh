#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

RUNS="${RUNS:-5}"
WARMUP="${WARMUP:-1}"

ensure_results_dir
build_app
"${BENCHMARK_DIR}/prepare-aot.sh" >/dev/null
"${BENCHMARK_DIR}/build-native.sh" >/dev/null

cd "${PROJECT_ROOT}"
hyperfine \
  --warmup "${WARMUP}" \
  --runs "${RUNS}" \
  --export-markdown "${STARTUP_RESULTS_FILE}" \
  --command-name plain \
  "${BENCHMARK_DIR}/measure-once.sh plain" \
  --command-name extracted \
  "${BENCHMARK_DIR}/measure-once.sh extracted" \
  --command-name leyden \
  "${BENCHMARK_DIR}/measure-once.sh aot" \
  --command-name graalvm \
  "${BENCHMARK_DIR}/measure-once.sh native"

log "Wrote startup benchmark summary to ${STARTUP_RESULTS_FILE}"
