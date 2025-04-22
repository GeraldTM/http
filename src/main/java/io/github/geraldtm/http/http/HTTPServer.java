package io.github.geraldtm.http.http;

import io.github.geraldtm.http.tcp.TCPServer;

public class HTTPServer extends TCPServer {

  public HTTPServer(String host, int port) {
    super(host, port);
  }

  public HTTPServer() {
    super();
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
    if (request.getRaw() != null) {
      System.out.println(request.getMethod());
    }
    return "resp";
  }
}
