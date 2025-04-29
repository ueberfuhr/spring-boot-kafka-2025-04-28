package de.sample.schulung.accounts.kafka;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaTargetProperties {

  private String topic;
  private int partitions = 1;

}
