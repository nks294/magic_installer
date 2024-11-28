package nks.magic.emw.style;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.FontMetrics;
import javax.swing.AbstractButton;

public class AwesomeRadioButton extends BasicRadioButtonUI {

    public AwesomeRadioButton() {
    }

    public static ComponentUI createUI(JComponent c) {
        return new AwesomeRadioButton();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        JRadioButton radioButton = (JRadioButton) c;
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 입체적인 버튼 스타일 적용
        if (radioButton.getModel().isPressed() || radioButton.getModel().isArmed()) {
            drawButton(g2D, radioButton, AwesomeStyle.mainColor, AwesomeStyle.btnBorderColor.brighter(), true);
        } else {
            drawButton(g2D, radioButton, AwesomeStyle.mainColor, AwesomeStyle.btnBorderColor, false);
        }

        // 텍스트 그리기
        g2D.setColor(AwesomeStyle.textColor);
        String text = radioButton.getText();
        FontMetrics fm = g2D.getFontMetrics();
        int textHeight = fm.getHeight();
        int x = 20;  // 라디오 버튼 네모 크기와 여백 고려하여 텍스트 위치 설정
        int y = (radioButton.getHeight() + textHeight) / 2 - fm.getDescent() - 1;
        g2D.drawString(text, x, y);

        // 라디오 버튼 네모 상자 그리기
        drawRadioButtonBox(g2D, radioButton);
    }

    // 입체적인 테두리를 그리는 메소드
    private void drawButton(Graphics2D g2D, JRadioButton radioButton, Color backgroundColor, Color borderColor, boolean pressed) {
        int width = radioButton.getWidth();
        int height = radioButton.getHeight();

        // 배경 색상 설정
        g2D.setColor(backgroundColor);
        g2D.fill(new Rectangle2D.Double(0, 0, width, height));

        // 테두리 그리기
        if (pressed) {
            g2D.setColor(borderColor.darker());
            g2D.draw(new Rectangle2D.Double(1, 1, width - 2, height - 2));
        } else {
            g2D.setColor(borderColor);
            g2D.draw(new Rectangle2D.Double(0, 0, width - 1, height - 1));
        }

        // 밝은 테두리 (위쪽과 왼쪽)
        g2D.setColor(borderColor.brighter());
        g2D.drawLine(0, 0, width - 2, 0);  // 상단
        g2D.drawLine(0, 0, 0, height - 2);  // 왼쪽

        // 어두운 테두리 (아래쪽과 오른쪽)
        g2D.setColor(borderColor.darker());
        g2D.drawLine(1, height - 1, width - 1, height - 1);  // 하단
        g2D.drawLine(width - 1, 1, width - 1, height - 1);  // 오른쪽
    }

    // 라디오 버튼 상자를 그리는 메소드
    private void drawRadioButtonBox(Graphics2D g2D, JRadioButton radioButton) {
        int boxSize = 12;  // 네모 크기 설정
        int x = 2;  // 라디오 버튼이 왼쪽에 위치하도록 조정
        int y = (radioButton.getHeight() - boxSize) / 2;

        // 라디오 버튼 외곽선 (네모 테두리)
        g2D.setColor(AwesomeStyle.btnBorderColor);
        g2D.draw(new Rectangle2D.Double(x, y, boxSize, boxSize));

        // 선택 여부에 따른 내부 색상
        if (radioButton.isSelected()) {
            g2D.setColor(new Color(130, 222, 137));  // 선택된 경우 초록색으로 채움
            g2D.fill(new Rectangle2D.Double(x + 2, y + 2, boxSize - 4, boxSize - 4));  // 내부 사각형
        } else {
            g2D.setColor(AwesomeStyle.mainColor);
            g2D.fill(new Rectangle2D.Double(x + 2, y + 2, boxSize - 4, boxSize - 4));  // 선택되지 않았을 때
        }
    }

    @Override
    public void paintButtonPressed(Graphics g, AbstractButton b) {
        paint(g, b);
    }
}