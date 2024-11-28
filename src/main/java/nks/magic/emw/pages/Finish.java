package nks.magic.emw.pages;

import javax.swing.JLabel;
import javax.swing.JPanel;

import nks.magic.emw.Screen;
import nks.magic.emw.style.AwesomeStyle;

public class Finish extends JPanel {
  private static final long serialVersionUID = 9202767316546599584L;

  private JLabel finish;
  private JLabel hint;

  public Finish() {
    this.setBackground(AwesomeStyle.secondaryColor);
    this.setSize(Screen.PAGE_WIDTH, Screen.PAGE_HEIGHT);
    this.setLayout(null);

    finish = new JLabel("작업 종료 ");
    finish.setFont(AwesomeStyle.body);
    finish.setForeground(AwesomeStyle.textColor);
    finish.setBounds(10, 0, 400, 40);
    add(finish);

    hint = new JLabel();
    hint.setText(""
        + "<HTML>설치가 모두 완료되었습니다."
        + "<BR>시작 버튼을 클릭하면 바로 마인크래프트를 실행합니다."
        + "<BR>만약 원치 않으시면 오른쪽 위 X를 눌러 프로그램을 종료해주세요."
        + "<BR>모드 간편설치기 by.294(nks294) | Source: adwonghk"
        + "</HTML>"
        + "");
    hint.setFont(AwesomeStyle.body);
    hint.setForeground(AwesomeStyle.textColor);
    hint.setBounds(10, 30, 400, 160);
    add(hint);

  }
}
