package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.CustomersService;
import de.sample.schulung.accounts.kafka.AutoConfigureKafkaEmbedded.KafkaTestContext;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureKafkaEmbedded
class CustomerEventsWithEmbeddedKafkaTests {

  @Autowired
  CustomersService customersService;
  @Autowired
  KafkaTestContext<UUID, CustomerEventRecord> context;
  @Autowired
  KafkaTargetProperties target;

  @Test
  void shouldProduceMessageOnCustomerCreate() {
    var customer = new Customer();
    customer.setName("Tom");
    customer.setDateOfBirth(LocalDate.now().minusYears(20));
    customer.setState(Customer.CustomerState.ACTIVE);
    customersService.createCustomer(customer);

    assertThat(context.poll(3, TimeUnit.SECONDS))
      .isPresent()
      .get()
      .returns(target.getTopic(), from(ConsumerRecord::topic))
      .extracting(ConsumerRecord::value)
      .returns("created", from(CustomerEventRecord::eventType))
      .returns(customer.getUuid(), from(CustomerEventRecord::uuid))
      .extracting(CustomerEventRecord::customer)
      .returns("Tom", from(CustomerRecord::name));
  }

}
