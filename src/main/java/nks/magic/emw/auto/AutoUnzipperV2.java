package nks.magic.emw.auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;

public class AutoUnzipperV2 {
    private List<String> filesNames = new ArrayList<>();
    private File sevenZa = extractResource("/magic/emw/7za.exe");
    public boolean extractModsOnly;

    // 생성자: extractModsOnly 파라미터로 모드만 추출할지 여부 결정
    public AutoUnzipperV2(String fileZip, String destDir, boolean extractModsOnly) throws IOException {
        this.extractModsOnly = extractModsOnly;

        String[] command = {
            sevenZa.getAbsolutePath(),
            "x", fileZip,
            "-o" + destDir,
            "-aoa", "-bb3"
        };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("- ")) {
                    String[] parts = line.split(" ");
                    if (parts.length > 1) {
                        String extractedFile = parts[1];

                        if (extractModsOnly) {
                            if (extractedFile.startsWith("mods/") || extractedFile.startsWith("mods\\")) {
                                filesNames.add(extractedFile);
                            }
                        } else {
                            filesNames.add(extractedFile);  // 전체 설치 시 모든 파일 추가
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private File extractResource(String resourcePath) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        File tempFile = File.createTempFile("7za", ".exe");
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

    public List<String> getFilesName() {
        return filesNames;
    }
}
