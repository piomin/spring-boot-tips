#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

extract_app

log "Training cache using strategy $(detect_cache_strategy)"
(
  cd "${RUNTIME_DIR}" && \
  "$(java_cmd)" \
    $(cache_create_flags) \
    org.springframework.boot.loader.launch.JarLauncher
)

cache_finalize_if_needed
log "Cache artifacts ready:"
cache_artifacts | sed 's#^#  - #'
