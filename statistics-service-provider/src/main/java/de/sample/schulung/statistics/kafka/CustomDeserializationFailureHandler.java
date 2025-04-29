package de.sample.schulung.statistics.kafka;

import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

import java.util.function.Function;

public class CustomDeserializationFailureHandler<T>
  implements Function<FailedDeserializationInfo, T> {

  @Override
  public T apply(FailedDeserializationInfo info) {
    byte[] data = info.getData();
    // eigene Behandlung???
    return null;
  }

}
