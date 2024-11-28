package nks.magic.emw.auto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

// 기존 소스의 압축해제 클래스
// 파일을 덮어씌우는 방식으로 변경하면서 관리자 권한 문제 발생
// UnzipperV2 에서 elevate와 7za 를 사용하여 임시조치

public class AutoUnzipper {

  public String filesName;

  public AutoUnzipper(String fileZip, String destDirPath) throws IOException {
    File destDir = new File(destDirPath);
    byte[] buffer = new byte[1024];
    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip))) {
      System.out.println(zis.getNextEntry());
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        // 파일 객체 생성
        File newFile = newFile(destDir, zipEntry);
        // 이미 파일이 존재하는데
        if (newFile.exists()) {
          // 그 파일이 폴더 형태라면
          if (newFile.isDirectory()) {
            // 폴더 안의 파일들의 리스트를 불러오고
            File[] files = newFile.listFiles();
            // 폴더 안에 아무것도 없다면
            if (files != null) {
              // 각 파일들 마다
              for (File f : files) {
                System.out.println("기존 폴더 비우기 " + f);
                // 삭제를 시도하고, 못지우면 로그 찍기
                if (!f.delete()) {
                  throw new IOException("파일 삭제 실패: " + f.getPath());
                }
              }
            }
            // 폴더가 아니면
          } else {
            // 삭제를 시도하고, 못지우면 로그찍기
            System.out.println("기존 파일 삭제 " + newFile);
            if (!newFile.delete()) {
              throw new IOException("파일 삭제 실패: " + newFile.getPath());
            }
          }
        }
        File parentDir = newFile.getParentFile();
        if (!parentDir.exists()) {
          parentDir.mkdirs();
        }
        if (!zipEntry.isDirectory()) {
          try (FileOutputStream fos = new FileOutputStream(newFile)) {
            int len;
            while ((len = zis.read(buffer)) > 0) {
              fos.write(buffer, 0, len);
            }
          }
        }
        zis.closeEntry();
        zipEntry = zis.getNextEntry();
        zis.close();
      }
    }
  }

  private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
    File destFile = new File(destinationDir, zipEntry.getName());

    String destDirPath = destinationDir.getCanonicalPath();
    String destFilePath = destFile.getCanonicalPath();

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
      throw new IOException("항목이 대상 폴더 바깥에 있습니다.: " + zipEntry.getName());
    }

    System.out.println("파일 생성 - " + destFile.getName());
    filesName += destFile.getName() + " ";

    return destFile;
  }

}

// if (zipEntry.isDirectory()) {
// if (!newFile.isDirectory() && !newFile.mkdirs()) {
// throw new IOException("폴더 만들기에 실패하였습니다.: " + newFile);
// }
// } else {
// File parentDir = newFile.getParentFile();
// if (!parentDir.exists()) {
// parentDir.mkdirs();
// }
// try () {
// }
// }