package nks.magic.emw.auto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
// import java.nio.channels.Channels;
// import java.nio.channels.ReadableByteChannel;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import nks.magic.emw.Main;

public class AutoDownloader implements Runnable {

  private String url;
  private String filename = "modpack.zip";
  private Thread thread;
  public boolean success = false;
  public boolean isDownloading = false;
  private boolean selfUpdate = false;
  private JProgressBar jProgressBar;
  private double lastDownloadedMB;
  private double completeFileSizeMB;

  public AutoDownloader(String url, boolean selfUpdate, JProgressBar jProgressBar) {
    this.url = url;
    this.selfUpdate = selfUpdate;
    this.jProgressBar = jProgressBar;

    thread = new Thread(this);
  }

  public void startDownload() {
    isDownloading = true;
    thread.start();
  }

  @Override
  public void run() {
    System.out.print("다운로드 시작. ");
    try {
      URL download = new URL(url);
      // ReadableByteChannel rbc = Channels.newChannel(download.openStream());
      HttpURLConnection httpConnection = (HttpURLConnection) (download.openConnection());
      long completeFileSize = httpConnection.getContentLength();

      FileOutputStream fileOut = null;
      if (selfUpdate) {
        filename = "Magic.jar";
        fileOut = new FileOutputStream(System.getProperty("user.dir") + "\\" + filename);
      } else {
        fileOut = new FileOutputStream(Main.tempDir + filename);
      }

      BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
      BufferedOutputStream bout = new BufferedOutputStream(fileOut, 1024);

      byte[] data = new byte[1024];

      long downloadedFileSize = 0;
      int x = 0;
      lastDownloadedMB = 0;
      completeFileSizeMB = BigDecimal.valueOf((double) completeFileSize / 1024.0d / 1024.0d)
          .setScale(3, RoundingMode.HALF_UP).doubleValue();

      System.out.print("파일 크기: " + completeFileSizeMB + "MB\n");
      while ((x = in.read(data, 0, 1024)) >= 0) {
        double downloadedMB = BigDecimal.valueOf((double) downloadedFileSize / 1024.0d / 1024.0d)
            .setScale(3, RoundingMode.HALF_UP).doubleValue();
        if (downloadedMB - lastDownloadedMB > 0) {
          System.out.print("\r");
          System.out.print("다운로드 중... " + downloadedMB + "MB/" + completeFileSizeMB + "MB");
          lastDownloadedMB = downloadedMB;
        }

        downloadedFileSize += x;
        final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);

        if (jProgressBar != null) {
          SwingUtilities.invokeLater(() -> jProgressBar.setValue(currentProgress));
        }

        bout.write(data, 0, x);
      }
      // fileOut.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
      // fileOut.flush();
      // rbc.close();
      bout.flush();
      fileOut.close();
      in.close();

      System.out.println("다운로드 완료.");
      success = true;

    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "오류가 발생했습니다. 스크린샷을 찍어 운영자에게 보내주세요.\n 오류 내용: " + e);
      e.printStackTrace();
    }
  }

  public double getDownloadedMB() {
    return lastDownloadedMB;
  }

  public double getCompleteFileSizeMB() {
    return completeFileSizeMB;
  }

}
