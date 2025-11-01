FROM rabbitmq:3.9.11-management

# 複製插件到 plugins 目錄
COPY plugins/rabbitmq_delayed_message_exchange-3.9.0.ez /plugins/

# 啟用插件
RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange