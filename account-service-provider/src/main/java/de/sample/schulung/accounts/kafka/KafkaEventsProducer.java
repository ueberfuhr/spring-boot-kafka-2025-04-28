package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.events.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaEventsProducer {

  private final KafkaTemplate<UUID, CustomerEventRecord> kafkaTemplate;
  private final CustomerEventRecordMapper mapper;
  private final KafkaTargetProperties target;

  @EventListener
  public void onCustomerCreated(CustomerCreatedEvent event) {
    //var customer = event.customer();
    var eventRecord = mapper.map(event);
    // UUID des Customers -> Partition
    kafkaTemplate.send(
      target.getTopic(),
      eventRecord.uuid(),
      eventRecord
    );
  }

  // TODO: Update / Delete?

}
