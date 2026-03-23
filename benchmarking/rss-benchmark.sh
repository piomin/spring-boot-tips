#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

RUNS="${RUNS:-5}"
TMP_FILE="$(mktemp)"
trap 'rm -f "${TMP_FILE}"' EXIT

display_mode() {
  case "${1}" in
    aot)
      printf '%s\n' "leyden"
      ;;
    native)
      printf '%s\n' "graalvm"
      ;;
    *)
      printf '%s\n' "${1}"
      ;;
  esac
}

ensure_results_dir
build_app
"${BENCHMARK_DIR}/prepare-aot.sh" >/dev/null
"${BENCHMARK_DIR}/build-native.sh" >/dev/null

for mode in plain extracted aot native; do
  for _ in $(seq 1 "${RUNS}"); do
    rss_file="$(mktemp)"
    /usr/bin/time -f '%M' -o "${rss_file}" "${BENCHMARK_DIR}/measure-once.sh" "${mode}" >/dev/null 2>/dev/null
    printf '%s %s\n' "${mode}" "$(tr -d '[:space:]' < "${rss_file}")" >> "${TMP_FILE}"
    rm -f "${rss_file}"
  done
done

{
  printf '| Mode | Mean max RSS [KB] | Min [KB] | Max [KB] |\n'
  printf '|:---|---:|---:|---:|\n'
  for mode in plain extracted aot native; do
    awk -v mode="${mode}" -v display="$(display_mode "${mode}")" '
      $1 == mode {
        rss[++count] = $2 + 0;
        sum += $2 + 0;
        if (count == 1 || $2 < min) min = $2;
        if (count == 1 || $2 > max) max = $2;
      }
      END {
        mean = sum / count;
        printf "| `%s` | %.0f | %d | %d |\n", display, mean, min, max;
      }
    ' "${TMP_FILE}"
  done
} > "${RSS_RESULTS_FILE}"

log "Wrote RSS summary to ${RSS_RESULTS_FILE}"
