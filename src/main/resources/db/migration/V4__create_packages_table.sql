CREATE TABLE packages (
    id BIGSERIAL PRIMARY KEY,
    mineral_type VARCHAR(255) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    mine_date DATE NOT NULL,
    location VARCHAR(255) NOT NULL,
    grade VARCHAR(255) NOT NULL,
    miner_id BIGINT NOT NULL,
    CONSTRAINT fk_miner FOREIGN KEY (miner_id) REFERENCES users (id),
    created_at DATE NOT NULL,
    status VARCHAR(255) NOT NULL,
    notes TEXT
);