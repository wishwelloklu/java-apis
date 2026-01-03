# production docker command:
docker-compose -f docker-compose.prod.yml up -d --build

# View logs from ssh
ssh wishw@192.168.0.102 "wsl -d Ubuntu-22.04 -u wishwell docker compose -f /home/wishwell/projects/java-apis/docker-compose.prod.yml logs -f"

# Stop containers
ssh wishw@192.168.0.102 "wsl -d Ubuntu-22.04 -u wishwell docker compose -f /home/wishwell/projects/java-apis/docker-compose.prod.yml down"