aws sqs receive-message \
  --queue-url http://localhost:4566/000000000000/order-queue \
  --max-number-of-messages 5 \
  --wait-time-seconds 10 \
  --region sa-east-1 \
  --endpoint-url http://localhost:4566