#!/bin/bash

# --- CONFIGURATION ---
WINDOWS_USER="wishw"
WINDOWS_IP="192.168.0.156"
WSL_DISTRO="Ubuntu-22.04"
WSL_USER="wishwell"
PROJECT_PATH="/home/wishwell/projects/java-apis"
# ---------------------

echo "--- Deploying to Windows (WSL Mode) ---"

# This sends the command to Windows -> WSL -> Ubuntu -> Docker
ssh $WINDOWS_USER@$WINDOWS_IP "wsl -d $WSL_DISTRO -u $WSL_USER bash -c 'cd $PROJECT_PATH; echo Pulling code...; git pull; echo Rebuilding...; docker compose -f docker-compose.prod.yml up -d --build'"

echo "--- Deployment Complete! ---"