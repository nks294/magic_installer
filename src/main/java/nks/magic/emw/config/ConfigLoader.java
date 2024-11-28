package nks.magic.emw.config;

public class ConfigLoader implements Runnable {

  private Request request;
  public Parser parser;
  public boolean success = false;
  public boolean error = false;

  public synchronized void load() {
    Thread thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run() {
    System.out.println("서버에 정보를 요청합니다.");
    request = new Request();
    error = request.error;
    parser = new Parser(request.getResponseBody());
    if (request.isRequesting && request.success) {
      System.out.println("요청 성공");
      success = true;
    } else if (error) {
      System.out.println("요청 실패");
      success = false;
    }
  }

}
