package io.github.geraldtm.http.http.requests.handlers;

import io.github.geraldtm.http.http.requests.HTTPRequest;
import java.util.Optional;

public class HTTP501Handler extends HTTPHandler {

  @Override
  protected String handleRequest(HTTPRequest request) {
    responseLine = getResponseLine(501);
    responseHeaders = getResponseHeaders(Optional.empty());
    responseBody =
      "<h1> 501 Not Implemented </h1> <br> <p> This site has not implemented a response to " +
      request.getMethod() +
      " requests </p>";
    return (
      responseLine +
      responseHeaders +
      blankLine +
      responseBody +
      blankLine +
      blankLine
    );
  }
}
