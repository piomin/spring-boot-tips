#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

MODE="${1:-}"
[[ -n "${MODE}" ]] || die "usage: benchmarking/run-mode.sh <plain|extracted|aot|native>"

ensure_extract_ready "${MODE}"
ensure_cache_ready "${MODE}"
ensure_native_ready "${MODE}"

RUN_DIR="$(run_mode_dir "${MODE}")"
RUN_TARGET="$(run_mode_target "${MODE}")"
RUN_FLAGS="$(run_mode_flags "${MODE}")"

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
