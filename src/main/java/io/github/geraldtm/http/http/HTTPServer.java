package io.github.geraldtm.http.http;

import io.github.geraldtm.http.http.requests.HTTPRequest;
import io.github.geraldtm.http.http.requests.handlers.HTTP401Handler;
import io.github.geraldtm.http.http.requests.handlers.HTTP501Handler;
import io.github.geraldtm.http.http.requests.handlers.HTTPHandler;
import io.github.geraldtm.http.tcp.TCPServer;
import java.security.InvalidParameterException;

public class HTTPServer extends TCPServer {

  String[] headers = { "Server:Custom GTM", "Content-Type:text/html" };
  String version = "HTTP/1.1";

  public HTTPServer(String host, int port) {
    super(host, port);
    HTTPHandler.initialize(headers, version);
  }

  public HTTPServer() {
    this("127.0.0.1", 8888);
  }

  @Override
  public String handleRequest(String data) {
    System.out.println(
      "Got HTTP request from " +
      addr.toString().replace("/", "") +
      " as follows: \r\n" +
      data
    );
    HTTPRequest request = new HTTPRequest(data);
    String response;
    try {
      String className =
        "io.github.geraldtm.http.http.requests.handlers.HTTP" +
        request.getMethod() +
        "Handler";
      System.out.println(className);
      Class<?> handlerClass;
      HTTPHandler handler;
      try {
        handlerClass = Class.forName(className);
        if (HTTPHandler.class.isAssignableFrom(handlerClass)) {
          handler = (HTTPHandler) handlerClass.getConstructor().newInstance();
        } else {
          handler = new HTTP501Handler();
        }
      } catch (Exception e) {
        e.printStackTrace();
        handler = new HTTP501Handler();
      }
      response = handler.handle(request);
      System.out.print(response.toCharArray());
    } catch (Exception e) {
      new InvalidParameterException(
        "Invalid HTTP request recieved from " +
        addr.toString().replace("/", "") +
        " as follows: \r\n" +
        data
      ).printStackTrace();
      e.printStackTrace();
      response = new HTTP401Handler().handle(request);
    }
    return response;
  }
}
