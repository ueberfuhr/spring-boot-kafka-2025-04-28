package de.sample.schulung.statistics.persistence;

import de.sample.schulung.statistics.domain.CustomerStatistics;
import de.sample.schulung.statistics.domain.CustomerStatisticsSink;
import org.springframework.stereotype.Component;

@Component
public class CustomerStatisticsSinkJpaImpl implements CustomerStatisticsSink {

  private final CustomerEntityRepository repo;

  public CustomerStatisticsSinkJpaImpl(CustomerEntityRepository repo) {
    this.repo = repo;
  }

  @Override
  public CustomerStatistics getStatistics() {
    return repo.getStatistics();
  }

}
