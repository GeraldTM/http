package io.github.geraldtm.http;

import io.github.geraldtm.http.http.HTTPServer;

public class Server {

  public static void main(String args[]) {
    HTTPServer server = new HTTPServer();
    try {
      server.start();
    } catch (Exception e) {
      System.err.println("Server encountered an error");
      e.printStackTrace();
      System.exit(1);
    }
  }
}
