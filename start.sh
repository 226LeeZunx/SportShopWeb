#!/bin/bash

set -e

# Start FastAPI chatbot on internal port 8000
uvicorn app:app --host 0.0.0.0 --port 8000 &
PYTHON_PID=$!

# Start Java web application
catalina.sh run &
TOMCAT_PID=$!

# Stop both processes if either exits
trap 'kill "$PYTHON_PID" "$TOMCAT_PID" 2>/dev/null || true' TERM INT

wait -n "$PYTHON_PID" "$TOMCAT_PID"