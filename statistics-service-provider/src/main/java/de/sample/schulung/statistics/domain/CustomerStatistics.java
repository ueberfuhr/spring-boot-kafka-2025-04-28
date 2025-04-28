package de.sample.schulung.statistics.domain;

import java.time.LocalDate;

public class CustomerStatistics {

  private long count = 0;
  private LocalDate earliestBirthdate;
  private LocalDate latestBirthdate;

  public long getCount() {
    return count;
  }

  public CustomerStatistics setCount(long count) {
    this.count = count;
    return this;
  }

  public LocalDate getEarliestBirthdate() {
    return earliestBirthdate;
  }

  public CustomerStatistics setEarliestBirthdate(LocalDate earliestBirthdate) {
    this.earliestBirthdate = earliestBirthdate;
    return this;
  }

  public LocalDate getLatestBirthdate() {
    return latestBirthdate;
  }

  public CustomerStatistics setLatestBirthdate(LocalDate latestBirthdate) {
    this.latestBirthdate = latestBirthdate;
    return this;
  }
}
