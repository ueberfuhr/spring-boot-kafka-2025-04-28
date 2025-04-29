# Spring Boot - Kafka

## Contents:

In this repository, we can find the following projects:

- [Account Service Provider](account-service-provider)
- [Statistics Service Provider](statistics-service-provider)

## Wiederholungsfragen

- [Begriffsdefinitionen](#begriffsdefinitionen)
- [Anwendungsbeispiel "Log Aggregation"](#anwendungsbeispiel-log-aggregation)
- [Anzahl an Partitionen](#anzahl-an-partitionen)
- [PULL vs. PUSH](#pull-vs-push)

### Begriffsdefinitionen

Was bedeuten die Begriffe?
* Bootstrap Server
  - Liste der Kafka-Broker, mit denen sich ein Client initial verbinden kann
  - Ausfallsicherheit: ist ein Broker weg, verbindet sich der Client mit einem anderen Broker
* Topic
  - Nachrichtenkanal
  - logische Aufteilung für unterschiedliche Nachrichten
  - Empfehlung: pro Topic sollten Nachrichten gleich aufgebaut sein
* Partition
  - "Warteschlange" (Reihenfolge von Nachrichten)
  - einem Topic zugeordnet
  - Skalierung/Parallelisierung innerhalb eines Topics
* Replica (Replication Factor)
  - Anzahl der Broker, auf die eine Partition/ein Topic repliziert wird
  - Ausfallsicherheit
* Consumer Group
  - logischer Name zusammengehöriger Consumer
  - pro Consumer Group wird jede Nachricht nur einmal zugestellt (Kafka merkt sich "Offset" pro Consumer Group und Partition)
  - Anwendung
    - Pods eines Services gehören zu einer Consumer Group
    - jeder Microservice bildet eine eigene Consumer Group

### Anwendungsbeispiel "Log Aggregation"

Ein Topic `service-logs` sammelt die Log-Ausgaben mehrerer Microservices.
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

**Antwort:**

- Analyse:
  - Die Logs eines einzelnen Service sind voneinander abhängig. (chronologisch)
  - Die Logs unterschiedlicher Services sind voneinander unabhängig.
- Konzeption:
  - Ansatz 1: Wir partitionieren die Ausgaben pro Service. (Key=Service)
  - Ansatz 2: Aufgrund des Zeitstempels kann die chronologische Reihenfolge auch ohne Einhaltung der Reihenfolge bei der Übermittlung reproduziert werden. Insofern können wir hier auch frei partitionieren. (kein Key, [Sticky Partitioner](https://cwiki.apache.org/confluence/display/KAFKA/KIP-480%3A+Sticky+Partitioner))

### Anzahl an Partitionen

Was passiert, wenn man in einem Topic zu wenige / zu viele Partitionen anlegt?

**Antwort:**

Bei zu wenige Partitionen (nur eine z.B.) ist keine Skalierung/Parallelisierung möglich.
Bei zu vielen Partitionen drohen Performance/Last-Probleme:

#### Memory- und File-Handle-Overhead
- Jede Partition wird als eigene Log-Datei auf der Festplatte behandelt und braucht eigene Ressourcen (Dateihandles, Netzwerkpuffer, Speicher).
- Wenn du sehr viele Partitionen hast (z.B. 100.000 oder mehr), dann:
  - verbrauchst du extrem viel RAM für die Metadaten (z.B. In-Memory-Indexstrukturen, Controller-Informationen).
  - kannst du in Linux/Unix das Limit für offene Dateien (open file descriptors) erreichen.

#### Performance-Probleme
- Die Latenz kann steigen, weil der Kafka-Broker für jede Partition mehr Verwaltungsaufwand hat.
- Die Leader Election (wenn ein Broker ausfällt und ein neuer Partition-Leader gewählt werden muss) dauert länger.
- Replication wird ineffizienter, weil jede Partition einzeln repliziert wird.

#### Producer- und Consumer-Probleme
- Produzenten müssen für jede Nachricht die richtige Partition finden (Partionierung ist komplexer und langsamer).
- Konsumenten müssen viele Partitionen verwalten. Jeder Consumer in einer Consumer Group bekommt Partitionen zugewiesen. Zu viele Partitionen können die Zuweisung ineffizient oder fehleranfällig machen.

#### Controller-Last
Der Kafka-Controller ist der Broker, der alle Partition-States verwaltet.
- Zu viele Partitionen erhöhen die Belastung des Controllers enorm.
- Dadurch kann es zu Instabilität oder sogar zu Cluster-Ausfällen kommen.

#### Cluster-Skalierung
Ein zu großes Verhältnis von Partitionen zu Brokern (z.B. 1 Million Partitionen auf 3 Broker) ist problematisch.
Faustregel: Kafka funktioniert am besten mit nicht mehr als 20.000 Partitionen pro Broker (abhängig von der Kafka-Version, Hardware und Konfiguration).

#### Zusammenfassung
✅ Mehr Partitionen = bessere Parallelisierung
❌ Zu viele Partitionen = mehr RAM-Verbrauch, langsamere Verwaltung, Risiko von Ausfällen

### PULL vs. PUSH

Was bedeutet es, wenn man bei Kafka-Consumern von _PULL-Prinzip_ spricht? Welche Vorteile hat dies gegenüber dem _PUSH-Prinzip_?

**Antwort:**

Kafka-Consumer arbeiten klar nach dem PULL-Prinzip:
- Consumer holen ("pullen") sich aktiv die Nachrichten vom Kafka-Broker ab.
- Der Broker schickt keine Nachrichten unaufgefordert an die Consumer (kein "Push").

**Details:**
- Ein Consumer sendet regelmäßig Fetch-Requests an den Broker:
  - In diesem Request fragt er: "Gib mir neue Daten ab Offset X."
  - Der Broker antwortet entweder sofort, wenn neue Daten vorhanden sind, oder wartet kurz (je nach Konfiguration, z.B. `fetch.min.bytes`, `fetch.max.wait.ms`), bevor er Daten zurückschickt.
- Dadurch kann der Consumer selbst steuern, wie schnell er konsumiert. Er kann z.B. bewusst langsamer konsumieren, pausieren oder parallel mehrere Partitionen abarbeiten.

**Warum PULL und nicht PUSH?**
- Bessere Lastkontrolle: Consumer können ihre eigene Geschwindigkeit und Last selbst regeln.
- Bessere Fehlerbehandlung: Wenn ein Consumer überlastet ist, kann er einfach langsamer pullen.
- Batch-Verarbeitung: Consumer können viele Nachrichten auf einmal abrufen (was effizienter ist).
- Backpressure: Der Broker wird nicht überlastet, weil er nicht unkontrolliert Nachrichten "rausdrücken" muss.

**Vergleich zu anderen Systemen:**

Kafka (PULL) unterscheidet sich damit z.B. von Systemen wie RabbitMQ (eher PUSH-orientiert).
