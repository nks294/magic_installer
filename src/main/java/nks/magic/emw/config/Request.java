package nks.magic.emw.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

public class Request {

  public static final String URL = "설정 파일이 보관된 서버 경로/config.json";
  private String charset = "UTF-8";

  public boolean isRequesting = false;
  public boolean success = false;
  public boolean error = false;

  private String responseBody;

  public Request() {
    URLConnection connection = null;
    InputStream response = null;

    try {
      isRequesting = true;

      connection = new URL(URL).openConnection();
      connection.setRequestProperty("Accept-Charset", charset);
      connection.setConnectTimeout(10000);
      connection.setReadTimeout(10000);

      response = connection.getInputStream();

      StringBuilder responseBodyBuilder = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, charset))) {
        String line;
        while ((line = reader.readLine()) != null) {
          responseBodyBuilder.append(line).append("\n");
        }
      }

      this.responseBody = responseBodyBuilder.toString();
      success = true;

    } catch (MalformedURLException e) {
      error = true;
      JOptionPane.showMessageDialog(null, "오류가 발생했습니다. 이 화면을 캡처해서 운영자에게 문의해주세요.\n 에러내용: " + e);
      e.printStackTrace();
    } catch (IOException e) {
      error = true;
      JOptionPane.showMessageDialog(null, "오류가 발생했습니다. 이 화면을 캡처해서 운영자에게 문의해주세요.\n 에러내용: " + e);
      e.printStackTrace();
    }
  }

  public String getResponseBody() {
    return responseBody;
  }
}
