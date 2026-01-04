# production docker command:
docker compose -f docker-compose.prod.yml up -d --build

# View logs from ssh
docker compose -f docker-compose.prod.yml logs -f

# Stop containers
docker compose -f docker-compose.prod.yml down