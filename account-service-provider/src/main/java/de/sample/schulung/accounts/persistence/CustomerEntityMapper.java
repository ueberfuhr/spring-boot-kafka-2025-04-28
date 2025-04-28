package de.sample.schulung.accounts.persistence;

import de.sample.schulung.accounts.domain.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerEntityMapper {

  public CustomerEntity map(Customer source) {
    if (source == null) {
      return null;
    }

    CustomerEntity customerEntity = new CustomerEntity();

    customerEntity.setUuid(source.getUuid());
    customerEntity.setName(source.getName());
    customerEntity.setDateOfBirth(source.getDateOfBirth());
    customerEntity.setState(source.getState());

    return customerEntity;
  }

  public Customer map(CustomerEntity source) {
    if (source == null) {
      return null;
    }

    Customer customer = new Customer();

    customer.setUuid(source.getUuid());
    customer.setName(source.getName());
    customer.setDateOfBirth(source.getDateOfBirth());
    customer.setState(source.getState());

    return customer;
  }

  public void copy(CustomerEntity source, Customer target) {
    if (source == null) {
      return;
    }

    target.setUuid(source.getUuid());
    target.setName(source.getName());
    target.setDateOfBirth(source.getDateOfBirth());
    target.setState(source.getState());
  }
}
