package io.github.geraldtm.http.http.requests.handlers;

import io.github.geraldtm.http.http.requests.HTTPRequest;
import java.util.Optional;

public class HTTPPOSTHandler extends HTTPHandler {

  public HTTPPOSTHandler() {}

  @Override
  protected String handleRequest(HTTPRequest request) {
    if (request.getBody().equals("") || request.getBody().equals("\r\n")) {
      responseLine = getResponseLine(400);
    } else {
      responseLine = getResponseLine(200);
    }
    responseHeaders = getResponseHeaders(Optional.empty());
    return (responseLine + responseHeaders + blankLine + responseBody);
  }
}
