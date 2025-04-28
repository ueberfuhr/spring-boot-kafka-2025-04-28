package de.sample.schulung.accounts.boundary;

import de.sample.schulung.accounts.domain.Customer;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoMapper {

  public CustomerDto map(Customer source) {
    if (source == null) {
      return null;
    }

    CustomerDto customerDto = new CustomerDto();

    customerDto.setUuid(source.getUuid());
    customerDto.setName(source.getName());
    customerDto.setDateOfBirth(source.getDateOfBirth());
    customerDto.setState(mapState(source.getState()));

    return customerDto;
  }

  public Customer map(CustomerDto source) {
    if (source == null) {
      return null;
    }

    Customer customer = new Customer();

    customer.setUuid(source.getUuid());
    customer.setName(source.getName());
    customer.setDateOfBirth(source.getDateOfBirth());
    customer.setState(mapState(source.getState()));

    return customer;
  }

  public String mapState(Customer.CustomerState source) {
    return switch (source) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

  public Customer.CustomerState mapState(String source) {
    return switch (source) {
      case "active" -> Customer.CustomerState.ACTIVE;
      case "locked" -> Customer.CustomerState.LOCKED;
      case "disabled" -> Customer.CustomerState.DISABLED;
      default -> throw new ValidationException();
    };
  }

}
