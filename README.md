# telemetry-ingestion-lab

A Java 21 project for learning and demonstrating concurrency, TCP networking, and performance engineering.

The goal is to build a small telemetry ingestion server that accepts fixed-size binary messages over TCP, processes them concurrently, and exposes measurable performance characteristics.

## Protocol

Each message is a fixed-size binary frame.

### Frame format

- Frame size: `32 bytes`
- Byte order: `BIG_ENDIAN`

| Bytes   | Field         | Type   |
|--------:|---------------|--------|
| 0..7    | deviceId      | long   |
| 8..11   | metricId      | int    |
| 12..19  | timestamp     | long   |
| 20..27  | value         | double |
| 28..31  | reserved      | int    |

### Example message model

- `deviceId`: source of telemetry
- `metricId`: metric identifier
- `timestamp`: event timestamp
- `value`: measured value

## Modules / packages

- `protocol` — binary frame model, encoder, decoder
- `core` — aggregation and processing logic
- `server` — TCP server
- `loadgen` — load generator / client
- `bench` — JMH benchmarks
- `docs` — notes, results, profiling findings

## How progress will be measured

The project will track:

- messages received
- messages processed
- throughput
- queue depth
- dropped messages
- latency percentiles

## Why this project exists

This repository exists as a learning repo to complement my knowledge on:

- lower-level Java
- concurrency design
- sockets / Netty / NIO
- measurable performance work

## Status

Work in progress.