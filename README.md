# longleaf-relay

##Synopsis 

longleaf-relay is a message relay system written in Java. The system consists of a single server, and multiple clients who can subscribe to and/or publish data.

Messages are stored on the server in channels. After subscribing to a channel, a client will receive all messages previously sent to the channel in order. It will then receive a message of type END_REFRESH. While connected, the client will receive any further messages sent to the channel. Multiple clients can subscribe to the same channel, and will all receive the same messages.

## Example Usage

The easiest way to write a client is to extend the abstract class RelayClient. This client is able to both subscribe to channels, and publish messages.
```java
public class ExampleClient extends RelayClient() {

}

exampleClient exampleClient = new exampleClient();

```
To connect to the server: 
```java
exampleClient.configAndConnect(String host, int port);
```

To subscribe to a channel:
```java
exampleClient.subscribeToChannel("exampleChannel"); 

To publish a message to a channel as a String:
```java
exampleClient.publishString(String toPublish, String channelName);
```

Java objects can be encoded as JSON, and used to communicate; very powerful for building more sophisticated client behavior:
```java
exampleClient.publishObject(Object toPublish, String channelName);
```

To delete all messages in a channel:
```java 
exampleClient.flushChannel(String channelName)
```
To send a message that will not be cached, and will only be received by currently connected clients: 
```java
exampleClient.broadcastString(String toBroadcast, String channelName)
```

Clients that wish to subscribe to messages should override the method handleJSON:

```java
public class ExampleClient extends RelayClient {

  @Override
  handleMessage(RelayMessage relayMessage, MessageEvent e) {
    System.out.println(relayMessage.payload);
  }

}
```

## Build

This project uses Maven to build. To build the source, from the project directory(where the pom.xml file is), run:
 
```console
mvn package
```

## Running the server

 Once the source is built, to start the server, do:

```console
java -jar target/longleaf-relay-1.0-SNAPSHOT-jar-with-dependencies.jar
```
## Running the example clients

To run the example receiver, which just prints messages received to the console, do:

```console
java -jar target/longleaf-relay-1.0-SNAPSHOT-jar-with-dependencies.jar strawn.longleaf.relay.examples.ExampleReceiver
```

To run the example sender, which sends two messages to the server, do:

```console
java -jar target/longleaf-relay-1.0-SNAPSHOT-jar-with-dependencies.jar strawn.longleaf.relay.examples.ExampleSender
```

## Motivation

  The main advantage of longleaf-relay is that is provides a lot of flexibility to the clients. Messages from many publishers are can be aggregated on the server, so a single listening client can get messages from many different places. Receivers don't have to be connected at the time a message is sent to receive it. Also publishers and receivers do not have to maintain a connection to one another. They can connect and disconnect from the server at will. 

 Another use case is that if two clients are both behind networks that make it difficult for them to connect to each other, or if they do not know of each others location, they can use a centrally located server to talk to one another.



## License

MIT




