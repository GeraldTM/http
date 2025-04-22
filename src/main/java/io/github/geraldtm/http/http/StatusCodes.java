package io.github.geraldtm.http.http;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.JSONObject;

public class StatusCodes {

  public HashMap<Integer, StatusCode> statusCodes = new HashMap<
    Integer,
    StatusCode
  >();

  public StatusCodes(String path) {
    path = this.getClass().getResource(path).getPath();
    String jsonData;
    try {
      // load json file as string
      jsonData = new String(Files.readAllBytes(Paths.get(path)));
      //parse as json array
      JSONObject statusCodesJson = new JSONObject(jsonData);
      for (String key : statusCodesJson.keySet()) {
        JSONObject statusCodeJson = statusCodesJson.getJSONObject(key);
        try {
          int code = statusCodeJson.getInt("code");
          String description = statusCodeJson.getString("description");
          String message = statusCodeJson.getString("message");
          StatusCode statusCode = new StatusCode(code, description, message);
          statusCodes.put(code, statusCode);
        } catch (Exception e) {
          System.err.println(
            "Error Parsing JSONObject " + statusCodeJson.toString() + "\n"
          );
          e.printStackTrace();
          continue;
        }
      }
    } catch (Exception e) {
      System.err.println(
        "Error loading status code json file from " + path + "\n"
      );
      e.printStackTrace();
      System.exit(1);
    }
  }

  public StatusCode getStatusCode(int code) {
    return statusCodes.get(code);
  }

  public class StatusCode {

    private int code;
    private String dsc;
    private String msg;

    public StatusCode(int code, String description, String message) {
      this.code = code;
      this.dsc = description;
      this.msg = message;
    }

    public int getCode() {
      return code;
    }

    public String getMessage() {
      return msg;
    }

    public String getDescription() {
      return dsc;
    }
  }
}
