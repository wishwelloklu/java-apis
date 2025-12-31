#!/bin/bash

# --- CONFIGURATION ---
WINDOWS_USER="wishw"              # Your Windows Username
WINDOWS_IP="192.168.0.102"        # Your Windows PC IP
PROJECT_PATH="C:/Users/wishw/projects/java-apis" # Where you cloned it on Windows
# ---------------------

echo "🚀 Deploying to Windows Server..."

# 1. SSH into Windows and run commands
ssh $WINDOWS_USER@$WINDOWS_IP "powershell -Command \"
    cd $PROJECT_PATH;
    echo '⬇️ Pulling latest code...';
    git pull;
    echo '🐳 Rebuilding Docker containers...';
    docker-compose -f docker-compose.prod.yml up -d --build;
    echo '✅ Deployment Complete!';
\""