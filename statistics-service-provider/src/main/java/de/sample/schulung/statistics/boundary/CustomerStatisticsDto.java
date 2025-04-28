package de.sample.schulung.statistics.boundary;

import java.time.LocalDate;

public class CustomerStatisticsDto {

  private long count;
  private LocalDate earliestBirthdate;
  private LocalDate latestBirthdate;

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public LocalDate getEarliestBirthdate() {
    return earliestBirthdate;
  }

  public void setEarliestBirthdate(LocalDate earliestBirthdate) {
    this.earliestBirthdate = earliestBirthdate;
  }

  public LocalDate getLatestBirthdate() {
    return latestBirthdate;
  }

  public void setLatestBirthdate(LocalDate latestBirthdate) {
    this.latestBirthdate = latestBirthdate;
  }
}
