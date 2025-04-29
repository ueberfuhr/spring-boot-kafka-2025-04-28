package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.CustomersService;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@SpringBootTest
@AutoConfigureTestDatabase
@EmbeddedKafka(
  partitions = 1,
  topics = {
    "customer-events"
  }
)
class CustomerEventsWithSimpleEmbeddedKafkaTests {

  @Autowired
  CustomersService customersService;
  @Autowired
  private EmbeddedKafkaBroker embeddedKafka;
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  private Consumer<String, String> consumer;

  @BeforeEach
  void setup() {
    Map<String, Object> consumerProps = KafkaTestUtils
      .consumerProps(
        "testGroup",
        "true",
        embeddedKafka
      );
    consumer = new DefaultKafkaConsumerFactory<>(
      consumerProps,
      new StringDeserializer(),
      new StringDeserializer()
    )
      .createConsumer();
    embeddedKafka.consumeFromAnEmbeddedTopic(
      consumer,
      "customer-events"
    );
  }

  @AfterEach
  void tearDown() {
    consumer.close();
  }

  @Test
  void shouldProduceMessageOnCustomerCreate() {
    var customer = new Customer();
    customer.setName("Tom");
    customer.setDateOfBirth(LocalDate.now().minusYears(20));
    customer.setState(Customer.CustomerState.ACTIVE);
    customersService.createCustomer(customer);

    var records = KafkaTestUtils.getRecords(consumer);
    assertThat(records)
      .hasSize(1);
    var record = records
      .iterator()
      .next();
    assertThat(record)
      .returns("customer-events", from(ConsumerRecord::topic))
      .returns(customer.getUuid().toString(), from(ConsumerRecord::key));

    JsonAssertions
      .assertThatJson(record.value())
      .isObject()
      .containsEntry("event_type", "created")
      .containsEntry("uuid", customer.getUuid().toString())
      .node("customer")
      .isObject()
      .containsEntry("name", "Tom");

  }

}
