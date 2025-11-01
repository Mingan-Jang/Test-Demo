
-- 先刪除舊表
DROP TABLE IF EXISTS public."event" CASCADE;
DROP TABLE IF EXISTS public.dead_letter_message CASCADE;

-- 建立事件表
CREATE TABLE public."event" (
    event_id char(36)  NOT NULL,
    message_id      varchar(100) NOT NULL,
    occur_time timestamp NOT NULL,
    event_type varchar(50) NULL,
    priority varchar(20) NULL,
    description varchar(500) NULL,
    soce varchar(32) NULL,
    creator varchar(50) NULL,
    create_time timestamp NULL,
    updater varchar(50) NULL,
    update_time timestamp NULL,
    CONSTRAINT event_pkey PRIMARY KEY (event_id)
);

-- 事件表註解
COMMENT ON TABLE public."event" IS '事件資料表';
COMMENT ON COLUMN public."event".message_id IS '原始訊息唯一識別碼';
COMMENT ON COLUMN public."event".event_id IS '事件唯一識別碼';
COMMENT ON COLUMN public."event".occur_time IS '事件發生時間';
COMMENT ON COLUMN public."event".event_type IS '事件類型';
COMMENT ON COLUMN public."event".priority IS '優先等級';
COMMENT ON COLUMN public."event".description IS '事件描述';
COMMENT ON COLUMN public."event".creator IS '創造者';
COMMENT ON COLUMN public."event".create_time IS '創造時間';
COMMENT ON COLUMN public."event".updater IS '更新者';
COMMENT ON COLUMN public."event".update_time IS '更新時間';
COMMENT ON COLUMN public."event".soce IS '事件來源或動作來源';


-- 建立死信訊息表
CREATE TABLE public.dead_letter_message (
    dlx_id          varchar(36)  NOT NULL,
    dlx_type varchar(20) DEFAULT 'MANUAL' null,
    message_id      varchar(100) NOT NULL,
    dlx_exchange    varchar(64)  NULL,
    dlx_routing     varchar(64)  NULL,
    dlx_queue       varchar(64)  NULL,
    payload         jsonb        NOT NULL,
    error_type      varchar(10)  NULL,
    error_desc      varchar(256) NULL,
    retry_count     int          DEFAULT 0,
    status          varchar(2)   DEFAULT 'N' NULL,
    ori_exchange    varchar(64)  NULL,
    ori_routing     varchar(64)  NULL,
    ori_queue       varchar(64)  NULL,
    creator         varchar(50)  NULL,
    create_time     timestamp    DEFAULT now() NOT NULL,
    CONSTRAINT dead_letter_message_pkey PRIMARY KEY (message_id)
);
-- 註解
COMMENT ON TABLE public.dead_letter_message IS '死信訊息表，紀錄從佇列處理失敗或重試失敗的訊息內容';

COMMENT ON COLUMN public.dead_letter_message.dlx_id IS '死信佇列唯一識別碼';
COMMENT ON COLUMN public.dead_letter_message.dlx_type IS '死信類型（AUTO=Broker自動、MANUAL=手動業務送死信）';
COMMENT ON COLUMN public.dead_letter_message.message_id IS '原始訊息唯一識別碼';
COMMENT ON COLUMN public.dead_letter_message.dlx_exchange IS '死信交換器名稱';
COMMENT ON COLUMN public.dead_letter_message.dlx_routing IS '死信路由鍵';
COMMENT ON COLUMN public.dead_letter_message.dlx_queue IS '死信佇列名稱';
COMMENT ON COLUMN public.dead_letter_message.payload IS '原始訊息內容（JSON 格式）';
COMMENT ON COLUMN public.dead_letter_message.error_type IS '錯誤類型代碼（VAL=資料驗證失敗, DUP=資料重複, NF=原始事件不存在, TO=處理逾時, ERR=處理異常, RETRY=超過最大重試次數）';
COMMENT ON COLUMN public.dead_letter_message.error_desc IS '錯誤詳細描述';
COMMENT ON COLUMN public.dead_letter_message.retry_count IS '訊息重試次數';
COMMENT ON COLUMN public.dead_letter_message.status IS '處理狀態代碼（N=新死信, R=處理中, F=重新處理失敗, S=重新處理成功）';
COMMENT ON COLUMN public.dead_letter_message.ori_exchange IS '原始交換器名稱';
COMMENT ON COLUMN public.dead_letter_message.ori_routing IS '原始路由鍵';
COMMENT ON COLUMN public.dead_letter_message.ori_queue IS '原始佇列名稱';
COMMENT ON COLUMN public.dead_letter_message.creator IS '建立者';
COMMENT ON COLUMN public.dead_letter_message.create_time IS '建立時間';
