package io.github.geraldtm.http.http.requests.handlers;

import io.github.geraldtm.http.http.StatusCodes;
import io.github.geraldtm.http.http.StatusCodes.StatusCode;
import io.github.geraldtm.http.http.requests.HTTPRequest;
import java.util.Optional;

/**
 * HTTPHandler
 */
public abstract class HTTPHandler {

  public String responseLine;
  public String responseHeaders;
  public String responseBody;
  public static final String blankLine = "\r\n";
  public static StatusCodes statusCodes = new StatusCodes(
    "/http_status_codes.json"
  );
  public static String headers;
  public static String HTTPVersion;
  public static boolean isInit = false;

  public static void initialize(String[] headers, String HTTPVersion) {
    for (String header : headers) {
      if (HTTPHandler.headers == null) {
        HTTPHandler.headers = header + "\r\n";
      }
      HTTPHandler.headers += header + "\r\n";
    }
    HTTPHandler.HTTPVersion = HTTPVersion;
    isInit = true;
  }

  public final String handle(HTTPRequest data) {
    if (isInit) {
      return handleRequest(data);
    } else {
      throw new IllegalStateException(
        "HTTPHandler was not initialized. HTTPHandler.initialize() was never called"
      );
    }
  }

  protected abstract String handleRequest(HTTPRequest data);

  public static final String getResponseLine(int code) {
    StatusCode reason = statusCodes.getStatusCode(code);
    return ("VERSION CODE REASON \r\n").replace("VERSION", HTTPVersion)
      .replace("CODE", String.valueOf(code))
      .replace("REASON", reason.getMessage());
  }

  public static final String getResponseHeaders(
    Optional<String[]> extraHeaders
  ) {
    String responseHeaders = headers;
    if (extraHeaders.isPresent()) {
      for (String header : extraHeaders.get()) {
        responseHeaders += header + "\r\n";
      }
    }
    return responseHeaders;
  }
}
