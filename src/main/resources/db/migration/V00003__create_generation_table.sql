DROP TABLE IF EXISTS generation CASCADE ;

CREATE TABLE generation(
    id INTEGER PRIMARY KEY,
    brand_id INTEGER,
    model_id INTEGER,
    name VARCHAR(50),
    FOREIGN KEY (brand_id) REFERENCES brand (id),
    FOREIGN KEY (model_id) REFERENCES model (id)
 );