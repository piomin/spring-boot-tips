#!/usr/bin/env bash

set -euo pipefail

source "$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/common.sh"

build_native_app
log "Native image ready at ${NATIVE_BINARY}"
