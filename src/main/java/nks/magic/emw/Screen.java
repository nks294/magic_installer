package nks.magic.emw;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import nks.magic.emw.auto.AutoUpdater;
import nks.magic.emw.config.ConfigLoader;
import nks.magic.emw.pages.Finish;
import nks.magic.emw.pages.Hello;
import nks.magic.emw.pages.Install;
import nks.magic.emw.pages.Warn;
import nks.magic.emw.style.AwesomeBorder;
import nks.magic.emw.style.AwesomeButton;
import nks.magic.emw.style.AwesomeProgressBarStyle;
import nks.magic.emw.style.AwesomeStyle;

public class Screen extends JPanel {
    private static final long serialVersionUID = -2486623228027275624L;

    public static final int PAGE_WIDTH = 420;
    public static final int PAGE_HEIGHT = 200;

    private JProgressBar progressBar;
    private ArrayList<JPanel> stages = new ArrayList<JPanel>();
    private Hello welcome;
    private Install autoDownloadInstall;
    private int pageIndex = 0;
    private boolean isInstalled = false;

    private JButton btnNext, btnLast;

    private Timer timer;

    private AutoUpdater autoUpdater;

    public Screen(ConfigLoader config) {
        config.load();

        // 스크린 사이즈 설정
        this.setSize(420, 300);
        this.setLayout(null);

        // 프로그램 배경색 지정
        this.setBackground(AwesomeStyle.mainColor);

        // 첫번째 페이지 로딩
        welcome = new Hello();
        stages.add(welcome);
        this.add(stages.get(pageIndex));

        // 진행바 생성
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setBounds(150, 210, 245, 42);
        progressBar.setMaximum(100000);

        progressBar.setEnabled(false);
        progressBar.setUI(new AwesomeProgressBarStyle());
        progressBar.setBorder(new AwesomeBorder(5, false));

        createButtons();

        // 버튼, 진행바 배치
        this.add(progressBar);
        this.add(btnNext);
        this.add(btnLast);

        // 다음 버튼 클릭했을 때
        btnNext.addActionListener(e -> {
            // 첫번째 페이지: 설치 방법 선택
            if (pageIndex == 0) {
                removeAllPages();
                if (welcome.isChoice1Selected || welcome.isChoice2Selected) {
                    if (isMinecraftInstalled(welcome.isChoice1Selected)) {
                        addAutoPages(config);
                    }
                }
            // 마지막 페이지일 경우, 마인크래프트 실행버튼
            } else if (pageIndex == stages.size() - 1) {
                launchMinecraft();
                System.exit(0);
            } else if (pageIndex == 2) {
                if (!isInstalled) {
                    Install installPage = (Install) stages.get(2);
                    installPage.startInstall(this, progressBar);
                    return;
                }
            }

            this.remove(stages.get(pageIndex));
            pageIndex++;

            // 번호에 맞는 스테이지 불러오고 새로고침
            this.add(stages.get(pageIndex));
            repaint();

            // 첫번째 페이지 아니면 이전버튼 활성화
            if (pageIndex != 0) {
                btnLast.setEnabled(true);
                btnLast.setText("이전");
            }

            // 설치 페이지에서 다음 버튼 설치로 변경, 이전버튼 비활성화
            if (pageIndex == 2) {
                btnNext.setText("설치");
            }

            // 마지막 페이지일때 버튼 텍스트 종료로 변경
            if (pageIndex == stages.size() - 1) {
                progressBar.setVisible(false);
                btnLast.setEnabled(false);
                btnLast.setVisible(false);
                btnNext.setBounds(10, 210, 385, 42);
                btnNext.setText("마인크래프트 실행!");
            }
        });

        if (pageIndex == 0) {
            btnLast.setText("종료");
        }

        // 이전 버튼 클릭했을 때
        btnLast.addActionListener(e -> {
            if (pageIndex == 0) {
                cancleInstall();
            } else {
                this.remove(stages.get(pageIndex));
                pageIndex--;

                this.add(stages.get(pageIndex));
                repaint();

                // 첫번째 페이지면 종료 버튼으로 변경
                if (pageIndex == 0) {
                    btnLast.setText("종료");
                }

                // 마지막 페이지가 아니면 다음으로 변경
                if (pageIndex != 2) {
                    btnNext.setText("다음");
                }
            }
        });

        // 로딩문구
        timer = new Timer(200, e -> {
            checkUpdate(config);
        });

        timer.start();
    }

    // 버튼 생성 메소드
    private void createButtons() {
        btnLast = new JButton("이전");
        btnLast.setBounds(10, 210, 60, 42);
        btnLast.setBorder(new AwesomeBorder(5, false));
        btnLast.setBackground(AwesomeStyle.secondaryColor);
        btnLast.setFont(AwesomeStyle.body);
        btnLast.setUI(new AwesomeButton());
        btnLast.setFocusPainted(false);
        btnLast.setForeground(AwesomeStyle.textColor);

        btnNext = new JButton("다음");
        btnNext.setBounds(80, 210, 60, 42);
        btnNext.setEnabled(false);
        btnNext.setBorder(new AwesomeBorder(5, false));
        btnNext.setBackground(AwesomeStyle.secondaryColor);
        btnNext.setFont(AwesomeStyle.body);
        btnNext.setUI(new AwesomeButton());
        btnNext.setFocusPainted(false);
        btnNext.setForeground(AwesomeStyle.textColor);
    }

    // 아 설치 안해요
    private void cancleInstall() {
        int result = JOptionPane.showOptionDialog(null,
                "294 마인크래프트 월드 설치를 취소하시겠어요?",
                "설치를 취소하실건가요? ( ´•︵•` )",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[] { "네", "아니오" },
                "아니오");

        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // 자동 설치 옵션
    private void addAutoPages(ConfigLoader config) {
        String forgeVer = config.parser.mc_version + "-forge-" + config.parser.forge_version;
        stages.add(1, new Warn(config.parser.mc_version, config.parser.forge_version, config.parser.forge_link, config.parser.emw_notice));
        autoDownloadInstall = new Install(this, config.parser.modpack_link, progressBar, btnNext, forgeVer, welcome);
        stages.add(2, autoDownloadInstall);
        stages.add(new Finish());
    }

    // 페이지 클리어 메소드
    private void removeAllPages() {
        stages.removeIf(page -> (page != welcome));
    }

    // 버튼 활성화, 비활성화, 텍스트 변경 메소드들
    public void setLbtEnabled(boolean enable) {
        btnLast.setEnabled(enable);
    }

    public void setRbtEnabled(boolean enable) {
        btnNext.setEnabled(enable);
    }

    public void setLbtText(String text) {
        btnLast.setText(text);
    }

    public void setRbtText(String text) {
        btnNext.setText(text);
    }

    public void setIsInstalled(boolean installed) {
        isInstalled = installed;
    }

    // 업데이트 체크 후 있으면 팝업띄우기
    private void checkUpdate(ConfigLoader config) {
        if (config.success) {
            if (Main.version.compareTo(new Version(config.parser.version)) >= 0) {
                System.out.println("현재 최신 버전입니다.");
                btnNext.setEnabled(true);
                timer.stop();
            } else if (Main.version.compareTo(new Version(config.parser.version)) < 0) {
                String versionHint = "설치기를 업데이트할게요!";
                JOptionPane.showMessageDialog(
                    this,
                    versionHint,
                    "새로운 버전 발견!",
                    JOptionPane.INFORMATION_MESSAGE
                );
                autoUpdater = new AutoUpdater(config.parser.update_link);
                autoUpdater.startUpdate();
                timer.stop();
            }
        }
    }

    // 마인크래프트 실행 메소드
    private void launchMinecraft() {
        String[] possiblePaths = {
                "C:\\XboxGames\\Minecraft Launcher\\Content\\Minecraft.exe",
                "C:\\Program Files (x86)\\Minecraft Launcher\\MinecraftLauncher.exe",
                "C:\\Program Files\\Minecraft Launcher\\MinecraftLauncher.exe"
        };

        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    Runtime.getRuntime().exec(file.getAbsolutePath());
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        int result = JOptionPane.showOptionDialog(null,
                "마인크래프트를 직접 실행해야하다니 좋았쓰!",
                "마인크래프트 런처를 찾지 못했어요 ( ´•︵•` )",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                new Object[] { "좋아쓰!" },
                "좋아쓰!");

        if (result == 0) {
            System.exit(0);
        }
    }

    private boolean isMinecraftInstalled(Boolean isChoice1) {
        File file = new File(Main.defualtMCLocation + "\\launcher_profiles.json");
    
        if (isChoice1) {
            if (!file.exists()) {
                int result = JOptionPane.showOptionDialog(null,
                        "이 경로에서 파일을 찾지 못했어요.\n마인크래프트를 아직 설치하지 않으셨나요?\n혹시라도 CurseForge 와 같은 프로그램을 사용중이시라면\n 그 프로그램의 경로가 아닌 실제 마인크래프트 설치 경로를 지정해주세요.",
                        "마인크래프트를 설치하셨나요? ( ´•︵•` )",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        new Object[] { "설치/확인 하고 다시올게요" },
                        "설치/확인 하고 다시올게요");
    
                if (result == 0) {
                    System.exit(0);
                }
                System.exit(0);
            }
        } else {
            File modsFolder = new File(Main.defualtMCLocation + "\\mods");
            if (!modsFolder.exists() || !modsFolder.isDirectory()) {
                int result = JOptionPane.showOptionDialog(null,
                        "이 경로에서 mods 폴더를 찾지 못했어요.\n마인크래프트를 아직 설치하지 않으셨나요?\n혹시라도 CurseForge 와 같은 프로그램을 사용중이시라면\n그 프로그램의 경로가 아닌 실제 마인크래프트 설치 경로를 지정해주세요.",
                        "mods 폴더가 없어요 ( ´•︵•` )",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        new Object[] { "설치/확인 하고 다시올게요" },
                        "설치/확인 하고 다시올게요");
    
                if (result == 0) {
                    System.exit(0);
                }
                System.exit(0);
            }
        }
        return true;
    }
}
