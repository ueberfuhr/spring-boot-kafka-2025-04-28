package de.sample.schulung.statistics.domain;

import org.springframework.stereotype.Service;

@Service
public class CustomerStatisticsService {

  private final CustomerStatisticsSink customerStatisticsSink;

  public CustomerStatisticsService(CustomerStatisticsSink customerStatisticsSink) {
    this.customerStatisticsSink = customerStatisticsSink;
  }

  public CustomerStatistics getStatistics() {
    return customerStatisticsSink.getStatistics();
  }

}
