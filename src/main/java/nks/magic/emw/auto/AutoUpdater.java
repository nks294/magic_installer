package nks.magic.emw.auto;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import nks.magic.emw.Main;

public class AutoUpdater implements Runnable {

  private AutoDownloader downloader;
  private String url;

  private Thread thread;
  private Timer timer;

  public AutoUpdater(String url) {
    if (url != null) {
      this.url = url;
    } else {
      return;
    }

    downloader = new AutoDownloader(this.url, true, null);

    timer = new Timer(500, e -> {
      if (downloader.success) {
        System.out.println("[마법사 업데이트] - 새로운 설치기를 다운로드 받습니다.");
        timer.stop();
        
        // 업데이트된 파일 실행
        try {
          System.out.println("[마법사 업데이트] - 새로운 설치기를 만날 시간: " + "java -jar"
              + new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath()
              + "\\" + "emwMagic.jar");
          Runtime.getRuntime()
              .exec("java -jar " + System.getProperty("user.dir") + "\\" + "emwMagic.jar");
        } catch (IOException | URISyntaxException e1) {
          JOptionPane.showMessageDialog(null, "마법사 업데이트중 에러가 발생하였습니다.\n이 화면을 스크린샷 찍어 운영자에게 보내주세요.\n 에러내용: " + e);
          e1.printStackTrace();
        }

        // 기존 프로그램 종료
        System.exit(0);
      }
    });
  }

  public void startUpdate() {
    thread = new Thread(this);
    thread.start();
    timer.start();
  }

  @Override
  public void run() {
    downloader.startDownload();
  }

}
