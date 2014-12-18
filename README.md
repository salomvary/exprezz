# Exprezz

A proof-of concept JavaScript HTTP client and server running on [Java 8
Nashorn](http://openjdk.java.net/projects/nashorn/) engine built on
[Finagle](https://twitter.github.io/finagle/).

## Anatomy

- A minimalistic [JavaScript HTTP server](src/main/javascript/application.js)
	(which is a client the same time) consuming nodejs-like API's.
- [Nodejs-like server](src/main/scala/com/exprezz/Server.scala) implemented in Scala backed by Finagle.
- [Nodejs-like client](src/main/scala/com/exprezz/Client.scala).
- ["Main" class](src/main/scala/com/exprezz/Application.scala).


## Running

You will need [Oracle Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).


```
$ ./sbt run
[info] Running com.exprezz.Application
Nov 27, 2014 7:50:35 PM com.twitter.finagle.Init$ apply
INFO: Finagle version 6.22.0 (rev=452c248f9e5bb71d13c48b90bb2e300f6e4fbf61) built at 20141013-200514
Server running at http://localhost:9101

$ curl http://localhost:9101/berlin
{
 "weather": "Mostly Cloudy",
 "sunset": "3:58 pm"
}
```

## Is this REAL?

It's just a playground, I'm not planning to create a real framwework. But you
get the idea...

## FAQ

### How about thread safety?

As neither Nashorn nor Finagle guarantee against concurrent re-entry to the JavaScript context, things might easily go wrong. Treating shared JavaScript variables as immutable should prevent most of the problems. [More about Nashorn thread safety](https://blogs.oracle.com/nashorn/entry/nashorn_multi_threading_and_mt).

### How do I specify Java version?

Do this on mac: ```JAVA_HOME=`/usr/libexec/java_home -v 1.8` ./sbt run```
