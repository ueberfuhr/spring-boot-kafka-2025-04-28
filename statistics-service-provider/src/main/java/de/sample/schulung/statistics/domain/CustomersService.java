package de.sample.schulung.statistics.domain;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Service
public class CustomersService {

  private final CustomersSink customersSink;

  public CustomersService(CustomersSink customersSink) {
    this.customersSink = customersSink;
  }

  public void saveCustomer(Customer customer) {
    customersSink.saveCustomer(customer);
  }

  public void deleteCustomer(UUID uuid) {
    customersSink.deleteCustomer(uuid);
  }

  public long count() {
    return customersSink.count();
  }

}
