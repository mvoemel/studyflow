package ch.zhaw.studyflow.webserver.executors;

import ch.zhaw.studyflow.webserver.HttpRequest;

import java.util.concurrent.Callable;

public interface EndpointExecutor {
    Callable<HttpRequest> execute(HttpRequest request);
}
