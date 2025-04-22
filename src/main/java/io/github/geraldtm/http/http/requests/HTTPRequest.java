package io.github.geraldtm.http.http.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import javax.print.attribute.standard.Media;

public class HTTPRequest {

  private String raw;
  private String method;
  private String uri;
  private String httpVersion = "1.1";
  private String body;

  public HTTPRequest(String data) {
    parseData(data);
  }

  private boolean parseData(String data) {
    try {
      body = data.split("\r\n\r\n")[1];
      String[] lines = data.split("\r\n");
      String requestLine = lines[0];

      String[] words = requestLine.split(" ");
      method = words[0];
      if (words.length > 1) {
        uri = words[1];
      }
      if (words.length > 2) {
        httpVersion = words[2];
      }

      raw = data;
      return true;
    } catch (Exception e) {
      System.err.println("Server encountered an error while parsing request:");
      e.printStackTrace();
      return false;
    }
  }

  public String getRaw() {
    return raw;
  }

  public String getMethod() {
    return method;
  }

  public String getURI() {
    return uri;
  }

  public String getHTTPVersion() {
    return httpVersion;
  }

  public String getBody() {
    return body;
  }
}
