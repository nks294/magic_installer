package nks.magic.emw.pages;

import javax.imageio.ImageIO;
import java.io.InputStream;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import nks.magic.emw.Main;
import nks.magic.emw.Screen;
import nks.magic.emw.style.AwesomeBorder;
import nks.magic.emw.style.AwesomeButton;
import nks.magic.emw.style.AwesomeStyle;

public class Hello extends JPanel {
    private static final long serialVersionUID = 7994513795896060482L;

    private ImageIcon imageIcon;
    private JLabel image;
    private JLabel welcome;
    private JLabel welcome2;

    private JButton btnChoice1;
    private JButton btnChoice2;
    public boolean isChoice1Selected = true;
    public boolean isChoice2Selected = false;

    private JButton btnChooseFolder;
    private JFileChooser chooser;
    private JLabel path;

    public Hello() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("magic/emw/img.jpg");

        this.setBackground(AwesomeStyle.secondaryColor);
        this.setSize(Screen.PAGE_WIDTH, Screen.PAGE_HEIGHT);
        this.setLayout(null);
        this.setFocusable(true);

        if (is != null) {
            try {
                imageIcon = new ImageIcon(ImageIO.read(is).getScaledInstance(133, 131, Image.SCALE_SMOOTH));
                image = new JLabel(imageIcon);
                image.setBounds(260, -15, 134, 187);
                add(image);
            } catch (Exception e) {
                System.out.println("이미지 파일 로드 실패: " + e.getMessage());
                image = new JLabel();
                image.setOpaque(true);
                image.setForeground(AwesomeStyle.textColor);
                image.setBounds(260, -15, 134, 187);
                add(image);
            }
        } else {
            image = new JLabel();
            System.out.println("이미지 파일을 찾지 못했습니다.");
            image.setOpaque(true);
            image.setForeground(AwesomeStyle.textColor);
            image.setBounds(260, -15, 134, 187);
            add(image);
        }

        welcome = new JLabel("안녕하세요? 모드 간편설치 마법사를");
        welcome.setFont(AwesomeStyle.body);
        welcome.setForeground(AwesomeStyle.textColor);
        welcome.setBounds(10, -5, 400, 40);
        add(welcome);

        welcome2 = new JLabel("실행해주셔서 감사합니다.");
        welcome2.setFont(AwesomeStyle.body);
        welcome2.setForeground(AwesomeStyle.textColor);
        welcome2.setBounds(10, 15, 400, 40);
        add(welcome2);

        btnChoice1 = new JButton("전체 설치");
        btnChoice1.setForeground(AwesomeStyle.textColor);
        btnChoice1.setUI(new AwesomeButton());
        btnChoice1.setBorder(new AwesomeBorder(5, false));
        btnChoice1.setFont(AwesomeStyle.body);
        btnChoice1.setFocusPainted(false);
        btnChoice1.setBounds(10, 57, 114, 85);
        updateButtonStyle(btnChoice1, isChoice1Selected);
        btnChoice1.addActionListener(e -> {
            isChoice1Selected = true;
            isChoice2Selected = false;
            updateButtonStyle(btnChoice1, isChoice1Selected);
            updateButtonStyle(btnChoice2, isChoice2Selected);
        });
        add(btnChoice1);

        btnChoice2 = new JButton("모드만 설치");
        btnChoice2.setForeground(AwesomeStyle.textColor);
        btnChoice2.setUI(new AwesomeButton());
        btnChoice2.setBorder(new AwesomeBorder(5, false));
        btnChoice2.setFont(AwesomeStyle.body);
        btnChoice2.setFocusPainted(false);
        btnChoice2.setBounds(136, 57, 114, 85);
        updateButtonStyle(btnChoice2, isChoice2Selected);
        btnChoice2.addActionListener(e -> {
            isChoice1Selected = false;
            isChoice2Selected = true;
            updateButtonStyle(btnChoice1, isChoice1Selected);
            updateButtonStyle(btnChoice2, isChoice2Selected);
        });
        add(btnChoice2);

        path = new JLabel(Main.defualtMCLocation);
        path.setBackground(AwesomeStyle.mainColor);
        path.setForeground(AwesomeStyle.textColor);
        path.setBorder(new AwesomeBorder(5, false));
        path.setOpaque(true);
        path.setVerticalAlignment(JLabel.CENTER);
        path.setHorizontalAlignment(JLabel.LEFT);
        path.setBounds(10, 154, 288, 36);
        path.setBorder(new EmptyBorder(0, 10, 0, 0));
        add(path);

        btnChooseFolder = new JButton("폴더 선택");
        btnChooseFolder.setBackground(AwesomeStyle.secondaryColor);
        btnChooseFolder.setForeground(AwesomeStyle.textColor);
        btnChooseFolder.setUI(new AwesomeButton());
        btnChooseFolder.setBorder(new AwesomeBorder(5, false));
        btnChooseFolder.setFont(AwesomeStyle.body);
        btnChooseFolder.setFocusPainted(false);
        btnChooseFolder.setBounds(307, 154, 88, 36);
        add(btnChooseFolder);

        btnChooseFolder.addActionListener(e -> {
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("마인크래프트 설치 경로를 선택해주세요.");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileHidingEnabled(false);

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                System.out.println("현재 경로: " + chooser.getCurrentDirectory());
                System.out.println("선택된 파일 : " + chooser.getSelectedFile());
                Main.defualtMCLocation = chooser.getSelectedFile().toPath().toString();
                path.setText(Main.defualtMCLocation);
            } else {
                System.out.println("선택된 폴더가 없습니다.");
            }
        });
    }
    
    private void updateButtonStyle(JButton button, boolean isSelected) {
        if (isSelected) {
            button.setBorder(new AwesomeBorder(5, true));
            button.setBackground(AwesomeStyle.secondaryColor.darker());
        } else {
            button.setBorder(new AwesomeBorder(5, false));
            button.setBackground(AwesomeStyle.secondaryColor);
        }
    }
}