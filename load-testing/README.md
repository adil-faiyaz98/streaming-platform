# Load Testing for Streaming Catalog Service

This directory contains JMeter load test scripts and utilities for performance testing the Streaming Catalog Service.

## Prerequisites

- Apache JMeter 5.5 or later
- JDK 11 or later
- curl and jq (for the shell script)

## Test Plan

The JMeter test plan simulates different types of API requests with the following distribution:

- 70% - Get all movies (paginated)
- 20% - Get movie by ID
- 10% - Search movies by title

## Running the Tests

### Using the Shell Script

The `run-load-test.sh` script provides a convenient way to run the load tests with various parameters:

```bash
./run-load-test.sh --host api-dev.streaming-platform.example.com --port 80 --protocol https --threads 100 --duration 600
```

Available parameters:

- `--host`: Target host (default: localhost)
- `--port`: Target port (default: 8082)
- `--protocol`: HTTP protocol (default: http)
- `--threads`: Number of concurrent users (default: 50)
- `--rampup`: Ramp-up period in seconds (default: 30)
- `--duration`: Test duration in seconds (default: 300)
- `--token`: JWT token for authentication (optional, will attempt to get one if not provided)
- `--output`: Output directory for results (default: results/YYYYMMDD_HHMMSS)

### Manual Execution

You can also run the test manually using JMeter GUI or command line:

```bash
jmeter -n -t streaming-catalog-load-test.jmx -l results.jtl -e -o dashboard \
  -Jhost=localhost -Jport=8082 -Jprotocol=http -Jthreads=50 -Jrampup=30 -Jduration=300 -Jtoken=your-jwt-token
```

## Analyzing Results

After the test completes, JMeter will generate:

1. A JTL file with detailed test results
2. A dashboard report with visualizations and statistics
3. A log file with JMeter execution information

Key metrics to analyze:

- Throughput (requests per second)
- Response times (average, median, 90th percentile, 95th percentile)
- Error rate
- Resource utilization (CPU, memory, network) on the server side

## Performance Targets

The service should meet the following performance targets:

- Throughput: At least 100 requests per second
- Response time: 95th percentile under 500ms
- Error rate: Less than 0.1% under normal load
- Scalability: Linear scaling with additional instances
