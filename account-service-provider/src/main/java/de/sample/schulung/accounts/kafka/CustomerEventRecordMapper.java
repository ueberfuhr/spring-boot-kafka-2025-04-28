package de.sample.schulung.accounts.kafka;

import de.sample.schulung.accounts.domain.Customer;
import de.sample.schulung.accounts.domain.events.CustomerCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerEventRecordMapper {

  @Mapping(
    target = "birthdate",
    source = "dateOfBirth"
  )
  CustomerRecord map(Customer source);

  @Mapping(
    target = "eventType",
    constant = "created"
  )
  @Mapping(
    target = "uuid",
    source = "customer.uuid"
  )
  CustomerEventRecord map(CustomerCreatedEvent source);

  // TODO: Update / Delete?

}
