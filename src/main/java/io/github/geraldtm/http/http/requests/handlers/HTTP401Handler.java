package io.github.geraldtm.http.http.requests.handlers;

import io.github.geraldtm.http.http.requests.HTTPRequest;
import java.util.Optional;

public class HTTP401Handler extends HTTPHandler {

  public HTTP401Handler() {}

  @Override
  protected String handleRequest(HTTPRequest request) {
    responseLine = getResponseLine(401);
    responseHeaders = getResponseHeaders(Optional.empty());
    responseBody = "Bad request as follows:" + request.getRaw();
    return (responseLine + responseHeaders + blankLine + responseBody);
  }
}
