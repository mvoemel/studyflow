package ch.zhaw.studyflow;

import ch.zhaw.studyflow.webserver.controllers.routing.RouteSegmentOld;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        URL test = new URL("https://test.pqf.com/user/:id");
        var a = RouteSegmentOld.segmentize("test/{a}:{b}asd/pp");
        System.out.println(test.toExternalForm());
    }
}