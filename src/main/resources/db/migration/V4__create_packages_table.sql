CREATE TABLE packages (
    id BIGSERIAL PRIMARY KEY,
    mineral_type VARCHAR(255) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    mine_date DATE NOT NULL,
    location VARCHAR(255) NOT NULL,
    grade VARCHAR(255) NOT NULL,
    miner_id VARCHAR(255) NOT NULL,
    miner_name VARCHAR(255) NOT NULL,
    created_at DATE NOT NULL,
    status VARCHAR(255) NOT NULL,
    notes TEXT
);
