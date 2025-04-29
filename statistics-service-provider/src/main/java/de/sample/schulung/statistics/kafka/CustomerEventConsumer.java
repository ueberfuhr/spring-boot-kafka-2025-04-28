package de.sample.schulung.statistics.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerEventConsumer {

  @KafkaListener(
    topics = "${application.kafka.customer-events.topic}"
  )
  public void consume(
    @Payload CustomerEventRecord eventRecord,
    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
  ) {
    log.info("Received event: partition={}, event={}", partition, eventRecord);
  }

}
