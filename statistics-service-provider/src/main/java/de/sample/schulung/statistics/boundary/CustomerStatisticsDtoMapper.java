package de.sample.schulung.statistics.boundary;

import de.sample.schulung.statistics.domain.CustomerStatistics;
import org.springframework.stereotype.Component;

@Component
public class CustomerStatisticsDtoMapper {

  public CustomerStatisticsDto map(CustomerStatistics source) {
    if (source == null) {
      return null;
    }

    CustomerStatisticsDto customerStatisticsDto = new CustomerStatisticsDto();

    customerStatisticsDto.setCount(source.getCount());
    customerStatisticsDto.setEarliestBirthdate(source.getEarliestBirthdate());
    customerStatisticsDto.setLatestBirthdate(source.getLatestBirthdate());

    return customerStatisticsDto;
  }

}
