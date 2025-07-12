package io.github.geraldtm.http.http.requests.handlers;

import io.github.geraldtm.http.http.requests.HTTPRequest;
import java.util.Optional;

public class HTTP500Handler extends HTTPHandler {

  public HTTP500Handler() {}

  @Override
  protected String handleRequest(HTTPRequest request) {
    responseLine = getResponseLine(500);
    responseHeaders = getResponseHeaders(Optional.empty());
    return (responseLine + responseHeaders + blankLine + responseBody);
  }
}
