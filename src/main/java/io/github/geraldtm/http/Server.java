package io.github.geraldtm.http;

import io.github.geraldtm.http.tcp.TCPServer;

public class Server {

  public static void main(String args[]) {
    TCPServer server = new TCPServer();
    try {
      server.start();
    } catch (Exception e) {
      System.err.println("The server encountered an error \n" + e);
    }
  }
}
