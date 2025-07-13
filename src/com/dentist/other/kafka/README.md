# Kafka-X-AOC Proof of Concept

This package contains a cute little proof of concept for Kafka and Advent of Code. You can read a file from disk, publish it line-by-line to some topic, and then read those lines back down. See year2021.day18.day18Driver.java for an example.

### Journal

1. Running Kafka on Windows is not a thing, so I installed WSL2 (windows subsystem for linux (2)) on this device. Now I have a cute little Ubuntu running.


2. The default config for a Kafka server out of the box assumes that anyone connecting to it will be on localhost. I had to change the advertised listener address in the server.properties file in the kafka/config directory. I set it to the ip address of my ubuntu "box", which was really WSL2. But my java application running from windows couldn't find anything on localhost:9092, but *COULD* find it on 172.52.blah blah blah:9092. Note that connecting to a broker is a two-step process. In step one we tell the client where to try to connect. Then the Kafka server responds with the address of the *real* node it wants the client to connect to. That address comes from the `advertised listener address` or whatever in the server.properties file.


3. Out of the box, the kafka libraries I was using for Java depended on the `SLF4J-api` library, but did not provide an implementation of a real SLF4J logger. This meant that when the application started up, I got a warning about not finding a static logging something or other, and then I never got any Kafka output. I needed to put a suitable SLF4J implementation jar on the classpath. I added a gradle dependency to `org.slf4j:slf4j-simple:2.0.17` which worked fine. Note that the slf4j-simple implementation only puts system-err to console, so any system-out I had sprinkled in my code got ignored. I could _probably_ fix this in my (existing?) log4j.properties file. But I like red text, so whatever.


4. This project is entirely useless, but I learned some good things. Kafka is a big old solution to a real problem. But in order to have the problems that Kafka is going to solve for you, you need to be a whole lot bigger than I am. 