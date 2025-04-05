#!/bin/bash

# Default values
HOST="localhost"
PORT="8082"
PROTOCOL="http"
THREADS="50"
RAMPUP="30"
DURATION="300"
TOKEN=""
OUTPUT_DIR="results/$(date +%Y%m%d_%H%M%S)"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --host)
      HOST="$2"
      shift 2
      ;;
    --port)
      PORT="$2"
      shift 2
      ;;
    --protocol)
      PROTOCOL="$2"
      shift 2
      ;;
    --threads)
      THREADS="$2"
      shift 2
      ;;
    --rampup)
      RAMPUP="$2"
      shift 2
      ;;
    --duration)
      DURATION="$2"
      shift 2
      ;;
    --token)
      TOKEN="$2"
      shift 2
      ;;
    --output)
      OUTPUT_DIR="$2"
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# Create output directory
mkdir -p "$OUTPUT_DIR"

# Get authentication token if not provided
if [ -z "$TOKEN" ]; then
  echo "No token provided, attempting to get one..."
  TOKEN=$(curl -s -X POST "$PROTOCOL://$HOST:$PORT/api/v1/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin"}' | jq -r '.token')
  
  if [ -z "$TOKEN" ] || [ "$TOKEN" == "null" ]; then
    echo "Failed to get authentication token. Proceeding without it."
    TOKEN=""
  else
    echo "Successfully obtained authentication token."
  fi
fi

# Run JMeter test
echo "Starting load test with the following parameters:"
echo "Host: $HOST"
echo "Port: $PORT"
echo "Protocol: $PROTOCOL"
echo "Threads: $THREADS"
echo "Ramp-up: $RAMPUP seconds"
echo "Duration: $DURATION seconds"
echo "Output directory: $OUTPUT_DIR"

jmeter -n \
  -t streaming-catalog-load-test.jmx \
  -l "$OUTPUT_DIR/results.jtl" \
  -j "$OUTPUT_DIR/jmeter.log" \
  -e -o "$OUTPUT_DIR/dashboard" \
  -Jhost="$HOST" \
  -Jport="$PORT" \
  -Jprotocol="$PROTOCOL" \
  -Jthreads="$THREADS" \
  -Jrampup="$RAMPUP" \
  -Jduration="$DURATION" \
  -Jtoken="$TOKEN"

echo "Load test completed. Results available in $OUTPUT_DIR"
