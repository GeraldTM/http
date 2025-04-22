package io.github.geraldtm.http.tcp;

import java.io.*;
import java.net.*;
import java.nio.CharBuffer;
import java.util.Optional;

public class TCPServer {

  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  public String host;
  public InetAddress addr;
  public int port;

  /**
   * TCP Server class:
   * call {@code TCPServer.start();} to begin listening
   * @param host hostname of the server
   * @param port port to listen on
   */
  public TCPServer(String host, int port) {
    this.host = host;
    this.port = port;
    try {
      addr = InetAddress.getByName(host);
      serverSocket = new ServerSocket(port, 1, addr);
      System.out.println("socket created at " + host + ":" + port);
    } catch (Exception e) {
      System.err.println(
        "The Server encountered an error while initializing the server socket at " +
        host +
        ":" +
        port +
        "\n"
      );
      e.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * TCP Server class with host {@value "127.0.0.1"} and port {@value 8888}:
   * call {@code TCPServer.start();} to begin listening
   */
  public TCPServer() {
    this("127.0.0.1", 8888);
  }

  public void start() throws IOException {
    try {
      System.out.println("listening at " + host + ":" + port);
      clientSocket = serverSocket.accept(); // Connect to client
    } catch (Exception e) {
      System.err.println(
        "The Server encountered an error while listening at " +
        host +
        ":" +
        port +
        "\n"
      );
      e.printStackTrace();
      System.exit(1);
    }

    out = new PrintWriter(clientSocket.getOutputStream(), true);
    in = new BufferedReader(
      new InputStreamReader(clientSocket.getInputStream())
    );

    char[] data = new char[1024];
    System.out.println(
      "got client connection from: " +
      clientSocket.getRemoteSocketAddress().toString().replace("/", "")
    );
    while (Optional.of(in.read(data)).isPresent()) { //Loop while client is connected
      String response = handleRequest(String.valueOf(data));
      out.print(response);
    }
    System.out.println("client disconnected...shutting down");
  }

  public String handleRequest(String data) {
    System.out.println(data);
    return "recieved packet: " + data;
  }
}
