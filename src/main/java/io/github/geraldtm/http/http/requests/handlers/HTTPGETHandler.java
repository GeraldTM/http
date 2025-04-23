package io.github.geraldtm.http.http.requests.handlers;

import io.github.geraldtm.http.http.requests.HTTPRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class HTTPGETHandler extends HTTPHandler {

  public HTTPGETHandler() {}

  @Override
  protected String handleRequest(HTTPRequest request) {
    String filename = request.getURI();

    if (filename.equals("/")) {
      filename = "/index.html";
    }

    try {
      String path = this.getClass().getResource(filename).getPath();
      responseBody = new String(Files.readAllBytes(Paths.get(path)));
      responseLine = getResponseLine(200);
    } catch (Exception e) {
      responseLine = getResponseLine(404);
      responseBody =
        "<h1>404 Not Found</h1> <br> <p> file " + filename + " not found";
    }
    responseHeaders = getResponseHeaders(Optional.empty());

    return (responseLine + responseHeaders + blankLine + responseBody);
  }
}
