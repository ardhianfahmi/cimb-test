CREATE TABLE call_monitoring (
    id                BIGSERIAL PRIMARY KEY,
    call_id           VARCHAR(50) NOT NULL UNIQUE,
    call_timestamp    TIMESTAMP NOT NULL,
    cs_name           VARCHAR(100) NOT NULL,
    customer_name     VARCHAR(100) NOT NULL,
    sentiment_score   SMALLINT NOT NULL CHECK (sentiment_score BETWEEN 0 AND 100),
    created_at        TIMESTAMP NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_call_monitoring_timestamp ON call_monitoring (call_timestamp);

CREATE INDEX idx_call_monitoring_sentiment ON call_monitoring (sentiment_score);

CREATE INDEX idx_call_monitoring_cs_name ON call_monitoring (cs_name);
CREATE INDEX idx_call_monitoring_customer_name ON call_monitoring (customer_name);