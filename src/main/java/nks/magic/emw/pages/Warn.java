package nks.magic.emw.pages;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import nks.magic.emw.Screen;
import nks.magic.emw.style.AwesomeBorder;
import nks.magic.emw.style.AwesomeStyle;

// 경고, 안내 메시지 출력 클래스
public class Warn extends JPanel {
    private static final long serialVersionUID = -8324611571879837373L;

    private JLabel tip;
    private JTextArea tip2;

    public Warn(String mcVersion, String forgeVersion, String forgeLink, String notice) {
        this.setBackground(AwesomeStyle.secondaryColor);
        this.setSize(Screen.PAGE_WIDTH, Screen.PAGE_HEIGHT);
        this.setLayout(null);

        // 안내 메시지 출력
        tip = new JLabel();
        tip.setText("<HTML><B><center>안내 메시지</center></HTML>");
        tip.setFont(AwesomeStyle.body);
        tip.setForeground(AwesomeStyle.textColor);
        tip.setBounds(54, 5, 400, 40);
        add(tip);

        // config.json 에서 불러온 공지 메시지 출력
        tip2 = new JTextArea();
        tip2.setEditable(false);
        tip2.setFont(AwesomeStyle.body);
        tip2.setBackground(AwesomeStyle.mainColor);
        tip2.setForeground(AwesomeStyle.textColor);
        tip2.setBorder(new AwesomeBorder(3, false));
        tip2.setLineWrap(true);
        tip2.setWrapStyleWord(true);
        tip2.setText(notice);
        tip2.setBounds(10, 53, 385, 77);

        // 스크롤 패널 설정
        JScrollPane tipScroll = new JScrollPane(tip2);
        tipScroll.setBackground(AwesomeStyle.mainColor);
        tipScroll.setForeground(AwesomeStyle.mainColor);
        tipScroll.setBounds(10, 53, 385, 137);
        add(tipScroll);

        // 스크롤을 최상단으로 설정
        scrollToTop(tipScroll);
    }

    // 스크롤을 최상단으로 설정하는 메소드
    private void scrollToTop(JScrollPane scrollPane) {
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMinimum());
        });
    }
}
