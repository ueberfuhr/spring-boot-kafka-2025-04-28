package de.sample.schulung.accounts.kafka;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class CustomJsonDeserializer extends JsonDeserializer<Object> {

  public CustomJsonDeserializer() {
    super(CustomJsonSerializer.createCustomObjectMapper());
  }

}
