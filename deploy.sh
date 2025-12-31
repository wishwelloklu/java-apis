#!/bin/bash

# --- CONFIGURATION ---
WINDOWS_USER="wishw"
WINDOWS_IP="192.168.0.102"
# Note: Use backslashes (escaped) for Windows CMD path
PROJECT_PATH="C:\\Users\\wishw\\projects\\java-apis"
# ---------------------

echo "🚀 Deploying to Windows Server (CMD Mode)..."

ssh $WINDOWS_USER@$WINDOWS_IP <<EOF
    cd $PROJECT_PATH
    echo "⬇️ Pulling latest code..."
    git pull
    
    echo "🐳 Rebuilding Docker containers..."
    
    REM --- THE FIX: Disable fancy graphics to prevent crash ---
    set COMPOSE_PROGRESS=plain
    set DOCKER_CLI_HINTS=false
    set COMPOSE_PARALLEL_LIMIT=1

    set DOCKER_CONFIG=C:\Users\wishw\.docker_plain
    if not exist C:\Users\wishw\.docker_plain mkdir C:\Users\wishw\.docker_plain
    echo {"auths":{}} > C:\Users\wishw\.docker_plain\config.json

    
    docker compose -f docker-compose.prod.yml pull --no-parallel
    docker compose -f docker-compose.prod.yml up -d --build
    exit
EOF