# Spring Boot - Kafka

## Contents:

In this repository, we can find the following projects:

- [Account Service Provider](account-service-provider)
- [Statistics Service Provider](statistics-service-provider)

## Wiederholungsfragen

### Begriffsdefinitionen

Was bedeuten die Begriffe?
* Bootstrap Server
* Topic
* Partition
* Replica (Replication Factor)
* Consumer Group

### Anwendungsbeispiel "Log Aggregation"

Ein Topic „service-logs“ sammelt die Log-Ausgaben mehrerer Microservices.
Beispiel einer Nachricht vom _AuthenticationService_:

```json
{ 
  "timestamp": "2025-04-29T10:00:00Z",
  "service": "auth-service",
  "level": "ERROR",
  "message": "Failed login for user X"
}
```

Sind diese Nachrichten partitionierbar? Wenn ja, wie?

### Anzahl an Partitionen

Was passiert, wenn man in einem Topic zu wenige / zu viele Partitionen anlegt?

### PULL vs. PUSH

Was bedeutet es, wenn man bei Kafka-Consumern von _PULL-Prinzip_ spricht? Welche Vorteile hat dies gegenüber dem _PUSH-Prinzip_?
