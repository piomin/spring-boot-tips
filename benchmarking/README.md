# Benchmarking

This directory contains a local benchmark harness for comparing the same application in four startup modes:

- `plain`: fat jar
- `extracted`: exploded Spring Boot layout
- `leyden`: exploded layout with a Project Leyden premain cache
- `graalvm`: GraalVM native image

## Requirements

- Maven
- `hyperfine`
- a Project Leyden JDK
- a GraalVM JDK with `native-image`

The scripts auto-detect local installs used during development, but you can point them explicitly with:

```bash
export LEYDEN_JAVA_HOME=/path/to/leyden-jdk
export GRAALVM_HOME=/path/to/graalvm
```

## Scripts

- `benchmark.sh`: repeated startup benchmark, writes `benchmarking/results/startup.md`
- `rss-benchmark.sh`: repeated max RSS benchmark, writes `benchmarking/results/rss.md`
- `prepare-aot.sh`: rebuild extracted runtime and train the Leyden cache
- `build-native.sh`: build the GraalVM native image
- `measure-once.sh`: launch one mode and stop once `/actuator/health` is up
- `run-mode.sh`: launch one mode without auto-stopping

## Usage

Run the startup benchmark:

```bash
benchmarking/benchmark.sh
```

Run the max RSS benchmark:

```bash
benchmarking/rss-benchmark.sh
```

Control the number of runs:

```bash
RUNS=10 WARMUP=2 benchmarking/benchmark.sh
RUNS=10 benchmarking/rss-benchmark.sh
```
