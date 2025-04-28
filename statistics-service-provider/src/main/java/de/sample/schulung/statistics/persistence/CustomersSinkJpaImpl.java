package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.Customer;
import de.sample.schulung.statistics.domain.CustomersSink;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomersSinkJpaImpl implements CustomersSink {

  private final CustomerEntityRepository repo;
  private final CustomerEntityMapper mapper;

  public CustomersSinkJpaImpl(
    CustomerEntityRepository repo,
    CustomerEntityMapper mapper
  ) {
    this.repo = repo;
    this.mapper = mapper;
  }

  @Override
  public void saveCustomer(Customer customer) {
    var customerEntity = mapper.map(customer);
    repo.save(customerEntity);
  }

  @Override
  public void deleteCustomer(UUID uuid) {
    repo.deleteById(uuid);
  }

  @Override
  public long count() {
    return repo.count();
  }
}
