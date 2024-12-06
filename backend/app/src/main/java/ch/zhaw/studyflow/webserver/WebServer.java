package ch.zhaw.studyflow.webserver;

/**
 * Interface for a web server.
 * A WebServer instance is returned by the WebServerBuilder. The server can be started by calling the start method.
 */
public interface WebServer {
    /**
     * Starts the web server.
     */
    void start();
}
