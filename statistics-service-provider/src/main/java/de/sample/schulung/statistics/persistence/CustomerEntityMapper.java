package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerEntityMapper {

  public CustomerEntity map(Customer customer) {
    if (customer == null) {
      return null;
    }

    CustomerEntity customerEntity = new CustomerEntity();

    customerEntity.setUuid(customer.getUuid());
    customerEntity.setDateOfBirth(customer.getDateOfBirth());

    return customerEntity;
  }

}
