# Magic Installer
> 취미로 운영중인 서버를 위해 제작한 모드팩 간편 설치기입니다.

![magic-screenshot-1](https://github.com/user-attachments/assets/9e36ff8c-3588-467b-8272-21bbdc6aaa1f)

## Reference
adwonghk 님의 소스 **[ (Repository Link) ](https://github.com/adwonghk/minecraft-custom-modpack-installer)** 를 기반으로 제작하였습니다.  

## 사용 기술
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

## 외부 리소스
- `7za.exe` (7-Zip 명령줄 도구) - [다운로드 링크](https://www.7-zip.org/download.html)
- `elevate.exe` (관리자 권한 요청 도구) - [다운로드 링크](https://code.kliu.org/misc/elevate/)
- `NotoSansKR-Medium.ttf` (한글 폰트) - [다운로드 링크](https://fonts.google.com/noto/specimen/Noto+Sans+KR)

## 주요 변경 사항
- **압축 해제 방식 변경**
  - 기존 `AutoUnzipper.java`에서 관리자 권한 문제를 해결하기 위해 `elevate.exe`를 사용.
  - 7-Zip 명령줄 도구(`7za.exe`)를 활용하여 새로운 압축 해제 클래스 `AutoUnzipper2.java`를 작성.
- **디자인 변경**
  - 프로젝트 내 `NotoSansKR-Medium.ttf` 폰트를 추가하여 한글 폰트를 적용.
  - AwesomeStyle(Awesome*.java) 파일로 마인크래프트 느낌의 디자인으로 변경
  - 아이콘 파일(`ico`) 추가로 프로그램 아이콘 변경.
  
## 기능
- 마인크래프트 실행중 여부 감지
- `config.json` 파일을 읽어 설치기의 버전 및 공지사항 불러오기
- 모드팩.zip 파일 자동 다운로드 및 압축 풀기
- `launcher` 설정 파일 수정 (기존 파일은 자동으로 백업)
- 모드만 설치 또는 포지(Forge)까지 설치 선택 가능
- 경로 직접 지정 가능
- 스타일 커스터마이징 가능

### Clone project (https)
```powershell
git clone https://github.com/nks294/magic_installer.git
```
### Go to project
```powershell
cd magic_installer
```
### Clean Project (gradle)
```powershell
./gradlew clean
```
### Build Jar File (gradle)
```powershell
./gradlew shadowJar
```

### Authors
- Adrian Wong - Initial work - [adwonghk](https://github.com/adwonghk)
- Jo Hee Sung - Modifications and edits - [nks294](https://github.com/nks294)

## ScreenShots
![magic-screenshot-2](https://github.com/user-attachments/assets/8b8a1ebc-8870-4089-a249-e8322d1a1f4f)  
![magic-screenshot-3](https://github.com/user-attachments/assets/2a12c470-c28f-4ba7-8930-69d45853584c)  
![magic-screenshot-4](https://github.com/user-attachments/assets/35a3a5ef-7bd7-4866-b52a-560110e7611b)  
![magic-screenshot-5](https://github.com/user-attachments/assets/a461c627-b336-4258-ba81-8af022e94381)  
