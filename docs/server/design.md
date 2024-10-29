# WebServer Architecture
A initial goal of the project was, to use no external libraries as possible apart from the JDK.
This included the web server. But since it is quite a lot of work to completely implement a HTTP-Server,
the decision was made to use the `com.sun.net.httpserver` package instead and build a web framework,
similar to a modern web framework, around it.

Based on this decision, a new (team internal) requirement was added to the web server: 
The possibility to implement a custom http server should be kept open.
This lead to the design decision, that the web server should be implemented as a wrapper around the `com.sun.net.httpserver` package.

This also gave way to the opportunity to simplify the interaction with the `com.sun.net.httpserver` package which doesn't 
provide the developer with certain quality of live features and abstractions (by design). Examples for such a missing abstraction would be cookies.

