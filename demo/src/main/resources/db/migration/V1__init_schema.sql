-- Create plans table
CREATE TABLE plans (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    max_requests_per_month INTEGER
);

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    plan_id BIGINT REFERENCES plans(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed initial plans based on PRD
INSERT INTO plans (name, description, price, max_requests_per_month) VALUES
('Free', 'Essential features for casual users. 10 explanations/month.', 0.00, 10),
('Plus', 'For power users who need more. 200 explanations/month + File uploads.', 3000.00, 200),
('Pro', 'Ultimate access for professionals. Unlimited explanations + Priority.', 7500.00, 1000000), -- Using 1,000,000 to represent "fair use" unlimited
('Pay-As-You-Go', 'No commitment, pay only for what you use.', 0.00, 0); -- Base price 0, logic handles per-use charge
