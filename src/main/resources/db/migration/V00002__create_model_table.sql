DROP TABLE IF EXISTS model CASCADE ;

CREATE TABLE model(
    id INTEGER PRIMARY KEY,
    brand_id INTEGER,
    name VARCHAR(50),
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);