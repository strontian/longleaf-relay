# longleaf-relay

##Synopsis 

longleaf-relay is a message broker written in Java. It consists of a server implementation and a client which the user extends with their own implementation.

The server:

  1. stores all received messages in 'channels'
  2. allows clients to connect
  3. allows clients to subscribe to channels
  4. publishes all stored messages in a channel to new subscribers(messages are sent in the order the were received)
  5. publishes any new message in a channel to existing subscribers

The client:

  1. can subscribe to any channel, channels are keyed using a string
  2. once subscribed, a client will immediately receive all messages currently in the channel
  3. while connected, will receive any new messages sent to the channel
  4. can send messages to any channel, whether or not they are subscribed to that channel
  5. can delete all messages in a channel

## Example Usage

The easiest way to write a client is to extend the abstract class RelayClient: 

```java
public class ExampleClient extends RelayClient() {
  @Override
  handleMessage(RelayMessage relayMessage, MessageEvent e) {
    //this is invoked each time the client receives a message from the server  
  }
}

ExampleClient exampleClient = new ExampleClient();

```
To connect to the server: 
```java
exampleClient.configAndConnect("localhost", 40001);
```

To subscribe to a channel:
```java
exampleClient.subscribeToChannel("exampleChannel"); 
```
To publish a message to a channel as a String:
```java
String toPublish = "exampleMessage";
exampleClient.publishString(toPublish, "exampleChannel");
```

Java objects can be encoded as JSON and sent as messages, a powerful way to build sophisticated client behavior:
```java
ExampleMessage toPublish = new ExampleMessage("exampleString1", "exampleString2");
exampleClient.publishObject(toPublish, channelName);
```

To delete all messages in a channel:
```java 
exampleClient.flushChannel("exampleChannelName")
```
To send a message that will not be cached, and will only be received by currently connected clients: 
```java
exampleClient.broadcastString("stringToBroadcast", "exampleChannelName")
```

To unsubscribe from a channel:
```java
exampleClient.unsubscribeChannel("exampleChannelName");
```

Clients that wish to subscribe to messages should override the method handleJSON:

```java
public class ExampleClient extends RelayClient {

  @Override
  handleMessage(RelayMessage relayMessage, MessageEvent e) {
    System.out.println(relayMessage.payload);
    //if the publishObject method was used, the object can be decoded like this:
    ExampleMessage example = gson.fromJson(relayMessage.payload, ExampleMessage.class);
    //gson instance is provided by RelayMessageHandler(a superclass of RelayClient)
  }

}
```

## Build

This project uses Maven to build. To build the source, from the project directory(where the pom.xml file is), run:
 
```console
mvn package
```

## Running the server

Once the source is built, to start the server(this must be run from the project directory, see configuration below):

```console
java -jar target/longleaf-relay-1.0-SNAPSHOT-jar-with-dependencies.jar
```
## Running the example clients

To run the example receiver, which just prints messages received to the console:

```console
java -jar target/longleaf-relay-1.0-SNAPSHOT-jar-with-dependencies.jar strawn.longleaf.relay.examples.ExampleReceiver
```

To run the example sender, which sends two messages to the server:

```console
java -jar target/longleaf-relay-1.0-SNAPSHOT-jar-with-dependencies.jar strawn.longleaf.relay.examples.ExampleSender
```

## Configuration

Client and server configuration are located in resources/clientconfig.properties and resources/serverconfig.properties, respectively. Since they are loaded using a relative path(see strawn.longleaf.relay.util.RelayConfigLoader), the easiest way to load the configuration is to include the resources folder in the directory where the program is run.


## Motivation
  
The main advantage of longleaf-relay is the is the flexibility if provides to the clients. 
  
Many producers of data can publish to the same channel, making it easy to aggregate messages from many sources
  
Publishers and receivers do not have to maintain direct a connection to one another. This provides some advantages:
  
  1. Publishers and receivers need not have fixed IP addresses, only the server does
  2. Receivers can receive messages sent during a time they were not connected
  3. The publisher and receiver can circumvent complicated or restrictive network architecture by using the server as a relay 

## Map Channels

A second paradigm for representing channel data is available through map channels. Messages in a map channel are represented on the server as a map. A publishing client must provide both a channel key and a message key, which together uniquely indentify a single message stored on the server. Any data subsequently published in the same channel and same key will replace previous data sent. 

There are analogous functions to most of the functions for working with regular channels:

```java
//publish an object as JSON to a map channel
exampleClient.publishJSONMap(exampleObject, exampleChannelName, exampleMessageKey);

//publish a String to a map channel
exampleClient.publishStringMap(exampleString, exampleChannelName, exampleMessageKey);

//subscribe to a map channel
exampleClient.subscribeMapChannel(exampleChannelName);

//unsubscribe to a map channel
exampleClient.unsubscribeMapChannel(exampleChannelName);

//delete all data in a map channel
exampleClient.flushMapChannel(exampleChannelName);
```

Data from a map channel will not necessarily arrive in the order received upon subscription


## License

longleaf-relay is provided under the Apache License v2.0. Please see LICENSE and NOTICE. 

Dependencies Gson and Netty are licensed by Apache License v2.0. Please see NOTICE.


