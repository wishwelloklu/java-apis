#!/bin/bash

# --- CONFIGURATION ---
WINDOWS_USER="wishw"
WINDOWS_IP="192.168.0.102"
WSL_DISTRO="Ubuntu-22.04"
WSL_USER="wishwell"
PROJECT_PATH="/home/wishwell/projects/java-apis"
# ---------------------

echo "Deploying to Windows (WSL Mode)..."

# Simplified command structure to avoid quoting errors
ssh $WINDOWS_USER@$WINDOWS_IP "wsl -d $WSL_DISTRO -u $WSL_USER bash -c 'cd $PROJECT_PATH; echo Pulling code...; git pull; echo Building containers...; docker compose -f docker-compose.prod.yml up -d --build'"