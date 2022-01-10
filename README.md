Publisher/Subscriber Framework with Proxy at Socket Level in Java
=======================================

### Background
This project was done as part of the Fall 2021 CS601 Principles of Software Development course at the University of San Francisco.

### Goals
Implement the following:
- Message broker framework that will support [publish/subscribe](https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern) functionality.
- Asynchronous publisher which maintains message ordering
- Asynchornous publisher without ordered messages
- Synchronoush publisher which blocks during message delivery
- Framework to publish to remote subscribers through a proxy using Sockets


### Learning Journey Results
- Deep dive into asynchronous operations and working with multiple threads, ExecutorServices, ThreadPools.
- Comparing different approaches to breaking up publishing (1 thread per subscriber, 1 thread per message send, etc.)
- Using ReenterentReadWriteLock to fine-tune read/write access to concurrent collections.
- Implemented a blocking queue with different approaches (eventually went with Java's built-in)