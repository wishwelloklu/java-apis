# production docker command:
docker compose -f docker-compose.prod.yml up -d --build

# View logs from ssh
docker compose -f docker-compose.prod.yml logs -f

# Stop containers
docker compose -f docker-compose.prod.yml down

# SSH into windows pc
1. ssh wish@192.168.1.156
2. wsl -d Ubuntu-24.04 -u wishwell
3. cd /home/wishwell/Projects/java-apis
4. git pull
