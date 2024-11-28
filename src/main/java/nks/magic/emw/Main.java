package nks.magic.emw;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.awt.Image;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import nks.magic.emw.config.ConfigLoader;

public class Main {
  public static final Version version = new Version("2.0.1");
  // 기본 마인크래프트 설치 경로 저장
  public static String defualtMCLocation = System.getenv("APPDATA") + "\\.minecraft";
  // 임시 파일들 저장 위치 지정
  public static String tempDir = System.getProperty("java.io.tmpdir");
  // 폰트 파일 등록을 위한 GraphicsEnvironment 불러오기
  GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
  // 로고 이미지 파일
  private Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/magic/emw/logo.png"));

  public Main() {
    addCustomFont();
    isWindows();
    isMinecraftRunning();
    checkJava();
    JFrame frame = new JFrame();
    frame.setIconImage(image);
    frame.setSize(420, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setTitle("모드 간편설치기 v " +version.get());
    frame.setContentPane(new Screen(new ConfigLoader()));
    frame.setVisible(true);

    try {
      System.out.println(
          "현재 경로: "
              + new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  // NotoSansKR 폰트 등록
  private void addCustomFont() {
    try {
      InputStream is = getClass().getClassLoader().getResourceAsStream("magic/emw/NotoSansKR-Medium.ttf");
      if (is == null) {
        System.out.println("폰트 파일을 찾을 수 없습니다.");
        return;
      }
      Font font = Font.createFont(Font.TRUETYPE_FONT, is);
      GE.registerFont(font);
    } catch (FontFormatException | IOException e) {
      System.out.println("폰트 등록중 에러 발생: " + e.getMessage());
    }
  }

  // 운영체제 확인 메소드, 현재는 Windows 만 감지
  private void isWindows() {
    String os = System.getProperty("os.name").toLowerCase();
    System.out.println("운영체제 확인: " + (os.indexOf("win") >= 0) + " (" + os + ")");
    if (!(os.indexOf("win") >= 0)) {
      int result = JOptionPane.showOptionDialog(null,
          "현재 치즈볼 the wizard는 윈도우에서만 활동합니다.",
          "지원되지 않는 운영 체제 ( ´•︵•` )",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.ERROR_MESSAGE,
          null,
          new Object[] { "저런... 종료할게요" },
          "저런... 종료할게요");
      if (result == 0) {
        System.exit(1);
      }
    }
  }

  // 자바 버전 체크
  private void checkJava() {
    String javaVersion = System.getProperty("java.version");
    String[] versionParts = javaVersion.split("\\.");
    int majorVersion = Integer.parseInt(versionParts[0]);
    System.out.println("자바 버전 확인: " + majorVersion);
  }

  // 마인크래프트가 실행중인지 체크
  private void isMinecraftRunning() {
    try {
      Process process = Runtime.getRuntime().exec("wmic process where \"name='javaw.exe'\" get ProcessId,CommandLine");
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line = reader.readLine();
      System.out.println("마인크래프트 실행중 여부 확인: " + (line.contains("minecraft")));
      while ((line = reader.readLine()) != null) {
        if (line.contains("minecraft")) {
          int result = JOptionPane.showConfirmDialog(null,
              "마인크래프트를 종료하신 후 다시 실행해주세요",
              "이런, 마인크래프트가 실행중인 것 같아요 ( ´•︵•` )",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.WARNING_MESSAGE);
          if (result == JOptionPane.OK_OPTION) {
            System.exit(0);
          }
        }
      }
    } catch (IOException e) {
      System.out.println("마인크래프트 실행 확인 오류: " + e.getMessage());
    }
  }

  private static File extractResource(String resourcePath) throws IOException {
    InputStream inputStream = Main.class.getResourceAsStream(resourcePath);
    if (inputStream == null) {
      throw new IOException("Resource not found: " + resourcePath);
    }

    File tempFile = File.createTempFile("emwMagic", ".exe");
    tempFile.deleteOnExit();

    try (OutputStream outputStream = new FileOutputStream(tempFile)) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    }

    return tempFile;
  }

  private static boolean isRunningAsAdmin() {
    String os = System.getProperty("os.name").toLowerCase();
    if (os.contains("win")) {
      try {
        Process process = Runtime.getRuntime().exec("net session");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.readLine() != null;
      } catch (IOException e) {
        return false;
      }
    }
    return false;
  }

  public static void main(String[] args) {
    try {
      String elevateFile = extractResource("/magic/emw/elevate.exe").getAbsolutePath();
      boolean isAdmin = isRunningAsAdmin();

      if (!isAdmin) {
        String jarPath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

        // elevate 를 사용해서 관리자 권한으로 실행
        String[] command = {
            "cmd.exe", "/c", "start", "/b", "\"\"", elevateFile, "javaw", "-jar", jarPath
        };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        process.waitFor();

        System.exit(0);
      } else {
        new Main();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
