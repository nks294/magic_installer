package nks.magic.emw.pages;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import org.json.JSONObject;
import org.json.JSONTokener;

import nks.magic.emw.Main;
import nks.magic.emw.Screen;
import nks.magic.emw.auto.AutoDownloader;
import nks.magic.emw.auto.AutoUnzipperV2;
import nks.magic.emw.style.AwesomeBorder;
import nks.magic.emw.style.AwesomeStyle;

public class Install extends JPanel {
    private static final long serialVersionUID = -8324611571879837373L;
    private static final String IMG_PATH = "/magic/emw/profile.png";
    private File profilesFile = new File(Main.defualtMCLocation + "\\launcher_profiles.json");
    private File backupFile = new File(Main.defualtMCLocation + "\\launcher_profiles_backup.json");

    private JLabel hint;
    private JTextArea console;
    private StringBuilder messages = new StringBuilder("");

    private AutoDownloader downloader;
    private Timer timer;
    private Hello hello;

    private enum states {
        onReady, downloadMods, removeOldMods, unzipMods, removeModsZip, onFinish, editFiles
    };

    private states currentState = states.onReady;

    public Install(Screen screen, String modpackURL, JProgressBar progressBar, JButton btn, String forgeVer,
            Hello hello) {
        this.setBackground(AwesomeStyle.secondaryColor);
        this.setSize(Screen.PAGE_WIDTH, Screen.PAGE_HEIGHT);
        this.setLayout(null);
        this.hello = hello;

        progressBar.setMaximum(100000);
        progressBar.setValue(0);
        downloader = new AutoDownloader(modpackURL, false, progressBar);

        hint = new JLabel("설치 버튼을 클릭하면 설치를 시작합니다.");
        hint.setFont(AwesomeStyle.body);
        hint.setForeground(AwesomeStyle.textColor);
        hint.setBounds(10, -5, 400, 40);
        add(hint);

        console = new JTextArea();
        console.setEditable(false);
        console.setFont(AwesomeStyle.body);
        console.setBackground(AwesomeStyle.mainColor);
        console.setForeground(AwesomeStyle.textColor);
        console.setBorder(new AwesomeBorder(3, false));
        console.setBounds(10, 33, 385, 87);

        JScrollPane jScrollPane = new JScrollPane(console);
        jScrollPane.setBackground(AwesomeStyle.mainColor);
        jScrollPane.setForeground(AwesomeStyle.mainColor);
        jScrollPane.setBounds(10, 33, 385, 157);
        add(jScrollPane);

        if (currentState == states.onReady) {
            currentState = states.downloadMods;
        }

        timer = new Timer(100, e -> {
            if (currentState == states.downloadMods) {
                downloadMods(progressBar);
            } else if (currentState == states.removeOldMods) {
                removeOldMods();
            } else if (currentState == states.unzipMods) {
                unzipMods();
            } else if (currentState == states.editFiles) {
                editFiles(forgeVer);
            } else if (currentState == states.removeModsZip) {
                removeModZip();
            } else if (currentState == states.onFinish) {
                onFinish(screen);
            }
            console.setText(messages.toString());
        });
    }

    // 설치 호출받고 설치 시작하는 메소드
    public void startInstall(Screen screen, JProgressBar progressBar) {
        progressBar.setBackground(new Color(32, 57, 64));
        progressBar.setForeground(new Color(130, 222, 137));
        currentState = states.downloadMods;
        screen.setLbtEnabled(false);
        screen.setRbtEnabled(false);
        timer.start();
    }

    // 다운로드 메소드
    private void downloadMods(JProgressBar progressBar) {
        if (!downloader.isDownloading) {
            downloader.startDownload();
        } else {
            messages.replace(0, messages.length(),
                    "다운로드 중 " + downloader.getDownloadedMB() + "MB / " + downloader.getCompleteFileSizeMB() + "MB ");
        }

        if (downloader.success) {
            messages.append("\n다운로드를 완료했습니다.\n");
            progressBar.setForeground(new Color(192, 192, 192));
            progressBar.setBackground(new Color(211, 211, 211));
            timer.stop();
            currentState = states.removeOldMods;
            timer.start();
        }
    }

    // mods 폴더 비우는 메소드
    private void removeOldMods() {
        int numFiles = 0;
        int numLibraries = 0;
        messages.append("기존 파일들을 삭제합니다.\n");
        System.out.println("기존 파일들을 삭제합니다... ");
        File modsFolder = new File(Main.defualtMCLocation + "\\" + "mods");
        if (!modsFolder.exists()) {
            modsFolder.mkdir();
        } else {
            for (File file : modsFolder.listFiles()) {
                if (!file.isDirectory()) {
                    numFiles++;
                    messages.append("기존 파일 삭제: " + file.getName() + "\n");
                    file.delete();
                }
            }
        }

        File librariesFolder = new File(Main.defualtMCLocation + "\\" + "libraries");
        if (!librariesFolder.exists()) {
            librariesFolder.mkdir();
        } else {
            for (File file : librariesFolder.listFiles()) {
                if (!file.isDirectory()) {
                    numLibraries++;
                    messages.append("기존 파일 삭제: " + file.getName() + "\n");
                    file.delete();
                }
            }
        }

        messages.append("기존 파일 " + numFiles + numLibraries + " 개 를 삭제완료.\n");
        System.out.println("기존 파일 " + numFiles + numLibraries + " 개 삭제.");

        timer.stop();
        currentState = states.unzipMods;
        timer.start();
    }

    // 다운로드 받은 모드팩 압축푸는 메소드
    private void unzipMods() {
        System.out.println("설치 시작");
        try {
            boolean extractModsOnly = hello.isChoice2Selected;
            AutoUnzipperV2 unzipper = new AutoUnzipperV2(Main.tempDir + "emw.zip", Main.defualtMCLocation + "\\",
                    extractModsOnly); // 플래그 전달
            List<String> filesNames = unzipper.getFilesName();
            int fileCount = filesNames.size() - 1;

            if (extractModsOnly) {
                messages.append("설치를 시작합니다...\n");
                messages.append("설치 경로: \n");
                messages.append(Main.defualtMCLocation + "\\mods\n");
                if (!filesNames.isEmpty()) {
                    for (String filename : filesNames) {
                        messages.append("모드 파일 복사: " + filename + "\n");
                    }
                }
                messages.append("모드 " + fileCount + " 개를 설치했습니다.\n");
                System.out.println("모드 " + fileCount + " 개를 설치했습니다.\n");
                timer.stop();
                currentState = states.onFinish;
                timer.start();
            } else {
                messages.append("설치를 시작합니다...\n");
                messages.append("설치 경로: \n");
                messages.append(Main.defualtMCLocation + "\n");
                if (!filesNames.isEmpty()) {

                    for (String filename : filesNames) {
                        messages.append("파일 생성 - " + filename + "\n");
                    }
                }
                messages.append("파일 " + fileCount + " 개를 설치했습니다.\n");
                System.out.println("파일 " + fileCount + " 개를 설치했습니다");
                timer.stop();
                currentState = states.editFiles;
                timer.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            messages.append("설치중 오류 발생: ").append(e.getMessage()).append("\n");
            timer.stop();
            currentState = states.onFinish;
            timer.start();
        }
    }

    // launcher-profiles.json 파일 수정 메소드
    private void editFiles(String forgeVer) {
        if (profilesFile.exists()) {
            messages.append("launcher_profiles 파일을 백업합니다... ");
            try {
                Files.copy(profilesFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                messages.append("완료\n");
            } catch (IOException e) {
                messages.append("\n백업 중 오류가 발생했습니다.\n");
                System.out.println("런처 프로필 백업중 오류 발생: " + e.getMessage());
            }
        }

        try {
            FileReader fileReader = new FileReader(profilesFile);
            JSONTokener tokener = new JSONTokener(fileReader);
            JSONObject jsonObject = new JSONObject(tokener);
            fileReader.close();

            JSONObject profiles = jsonObject.optJSONObject("profiles");
            if (profiles == null) {
                profiles = new JSONObject();
                jsonObject.put("profiles", profiles);
            }

            if (profiles.has("forge")) {
                JSONObject forgeProfile = profiles.getJSONObject("forge");
                forgeProfile.put("icon", "data:image/png;base64," + getBase64Image());
                forgeProfile.put("lastVersionId", forgeVer);
            } else {
                JSONObject forgeProfile = new JSONObject();
                forgeProfile.put("icon", "data:image/png;base64," + getBase64Image());
                forgeProfile.put("lastVersionId", forgeVer);
                forgeProfile.put("name", "forge");
                forgeProfile.put("resolution", new JSONObject().put("height", 720).put("width", 1280));
                forgeProfile.put("type", "custom");

                profiles.put("forge", forgeProfile);
            }

            messages.append("런처 프로필을 업데이트합니다... ");
            FileWriter fileWriter = new FileWriter(profilesFile);
            fileWriter.write(jsonObject.toString(4));
            fileWriter.close();
            messages.append("완료\n");
            timer.stop();
            currentState = states.removeModsZip;
            timer.start();
        } catch (IOException e) {
            messages.append("\n런처 프로필 수정중 오류 발생.").append(e.getMessage()).append("\n");
            System.out.println("런처 프로필 수정중 오류가 발생했습니다.: " + e.getMessage());
            timer.stop();
            currentState = states.onFinish;
            timer.start();
        }
    }

    // 다운로드 받은 임시파일 삭제하는 메소드
    private void removeModZip() {
        File file = new File(Main.tempDir + "emw.zip");
        if (file.delete()) {
            System.out.println("임시 파일을 삭제합니다.");
            messages.append("임시 파일을 삭제합니다... ");
        } else {
            System.out.println("임시 파일 삭제 실패, 위 화면을 캡처해 운영자에게 문의해주세요.");
            messages.append("\n임시 파일 삭제 실패, 위 화면을 캡처해 운영자에게 문의해주세요.\n");
        }
        messages.append("완료\n");
        currentState = states.onFinish;
    }

    // 설치 완료 화면
    private void onFinish(Screen screen) {
        messages.append("설치가 모두 완료되었습니다.\n");
        messages.append("다음 버튼을 클릭해주세요.\n");

        timer.stop();
        screen.setIsInstalled(true);
        screen.setLbtEnabled(false);
        screen.setRbtEnabled(true);
        screen.setRbtText("다음");
    }

    // 런처 프로필 이미지 base64 형식으로 변환
    private String getBase64Image() throws IOException {
        InputStream imageStream = getClass().getResourceAsStream(IMG_PATH);
        if (imageStream == null) {
            throw new IOException("이미지를 찾을 수 없습니다.");
        }

        BufferedImage bufferedImage = ImageIO.read(imageStream);
        if (bufferedImage == null) {
            throw new IOException("이미지를 읽을 수 없습니다.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();
        baos.close();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}