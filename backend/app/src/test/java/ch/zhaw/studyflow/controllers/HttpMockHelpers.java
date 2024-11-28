package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.webserver.http.CaptureContainer;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.http.query.QueryParameters;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class HttpMockHelpers {
    public static HttpResponse makeUnautherizedResponse() {
        HttpResponse response = mock(HttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatusCode.UNAUTHORIZED);
        return response;
    }

    public static RequestContext makeRequestContext(HttpRequest request) {
        return makeRequestContext(request, Map.of());
    }

    public static RequestContext makeRequestContext(HttpRequest request, Map<String, String> captures) {
        RequestContext requestContext = mock(RequestContext.class);
        when(requestContext.getRequest()).thenReturn(request);
        CaptureContainer container = mock(CaptureContainer.class);
        when(requestContext.getUrlCaptures()).thenReturn(container);
        when(container.get(any())).then(a -> Optional.ofNullable(captures.get(a.getArgument(0))));
        return requestContext;
    }

    public static HttpRequest makeHttpRequest() {
        HttpRequest request = mock(HttpRequest.class);
        HttpResponse response = makeHttpResponse();
        when(request.createResponse()).thenReturn(response);
        return request;
    }

    public static HttpRequest makeHttpRequest(ReadableBodyContent body) {
        HttpRequest request = makeHttpRequest();
        when(request.getRequestBody()).thenReturn(Optional.of(body));
        return request;
    }

    public static  void addQueryParameters(HttpRequest request, Map<String, List<String>> parameters) {
        QueryParameters queryParameters = mock(QueryParameters.class);
        when(queryParameters.getSingleValue(anyString()))
                .then(invoc -> {
                    List<String> values = parameters.get(invoc.getArgument(0));
                    if (values == null || values.isEmpty()) {
                        return Optional.empty();
                    }
                    if (values.size() > 1) {
                        throw new IllegalArgumentException("Multiple values found for key: ");
                    }
                    return Optional.of(values.getFirst());
                });
        when(queryParameters.keys()).thenReturn(parameters.keySet());
        when(queryParameters.getValues(anyString()))
                .then(a -> Optional.ofNullable(parameters.get(a.getArgument(0))));
        when(request.getQueryParameters()).thenReturn(queryParameters);
    }

    public static HttpResponse makeHttpResponse() {
        HttpResponse response = mock(HttpResponse.class);
        when(response.setResponseBody(any())).thenReturn(response);
        when(response.setStatusCode(any())).thenReturn(response);
        return response;
    }

    public static <T> ReadableBodyContent makeJsonRequestBody(Class<T> type, T value) {
        ReadableBodyContent content = mock(ReadableBodyContent.class);
        when(content.tryRead(type)).thenReturn(Optional.of(value));
        return content;
    }

    public static ArgumentCaptor<HttpStatusCode> captureResponseCode(HttpResponse response) {
        ArgumentCaptor<HttpStatusCode> captor = ArgumentCaptor.forClass(HttpStatusCode.class);
        verify(response, atLeast(0)).setStatusCode(captor.capture());
        when(response.getStatusCode()).thenReturn(captor.getValue());
        return captor;
    }

    public static ArgumentCaptor<WritableBodyContent> captureResponseBody(HttpResponse response) {
        ArgumentCaptor<WritableBodyContent> captor = ArgumentCaptor.forClass(WritableBodyContent.class);
        verify(response, atLeast(0)).setResponseBody(captor.capture());
        return captor;
    }
}
