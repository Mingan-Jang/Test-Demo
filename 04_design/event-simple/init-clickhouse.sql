CREATE TABLE event_history
(
    event_id String,                                     -- 唯一事件 ID
    message_id LowCardinality(String),
    occur_time DateTime64(3) NOT NULL,
    event_type LowCardinality(Nullable(String)),
    priority LowCardinality(Nullable(String)),
    description Nullable(String),
    soce LowCardinality(Nullable(String)),
    creator LowCardinality(Nullable(String)),
    create_time Nullable(DateTime64(3)),
    updater LowCardinality(Nullable(String)),
    update_time Nullable(DateTime64(3))
)
ENGINE = MergeTree
PARTITION BY toYYYYMM(occur_time)
ORDER BY (occur_time, event_id)
PRIMARY KEY (occur_time, event_id)
SETTINGS index_granularity = 8192;
