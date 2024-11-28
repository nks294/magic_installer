package nks.magic.emw.style;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

public class AwesomeButton extends BasicButtonUI {

    public AwesomeButton() {
    }

    public static ComponentUI createUI(JComponent c) {
        return new AwesomeButton();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (button.getModel().isArmed() || button.getModel().isPressed()) {
            drawButton(g2D, button, AwesomeStyle.mainColor, AwesomeStyle.btnBorderColor.brighter(), true);
        } else {
            drawButton(g2D, button, AwesomeStyle.mainColor.brighter(), AwesomeStyle.btnBorderColor, false);
        }

        g2D.setColor(AwesomeStyle.textColor);
        FontMetrics fm = g2D.getFontMetrics();
        String text = button.getText();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int x = (button.getWidth() - textWidth) / 2;
        int y = (button.getHeight() + textHeight) / 2 - fm.getDescent() - 1;
        g2D.drawString(text, x, y);
    }

    private void drawButton(Graphics2D g2D, AbstractButton button, Color backgroundColor, Color borderColor, boolean pressed) {
        int width = button.getWidth();
        int height = button.getHeight();

        // 배경 색상 설정
        g2D.setColor(backgroundColor);
        g2D.fill(new Rectangle2D.Double(0, 0, width, height));  // 직각 모서리로 설정

        // 테두리 그리기
        if (pressed) {
            g2D.setColor(borderColor.darker());
            g2D.draw(new Rectangle2D.Double(1, 1, width - 2, height - 2));  // 눌렸을 때 테두리
        } else {
            g2D.setColor(borderColor);
            g2D.draw(new Rectangle2D.Double(0, 0, width - 1, height - 1));  // 기본 테두리
        }
    }

    @Override
    public void paintButtonPressed(Graphics g, AbstractButton b) {
        paint(g, b);
    }
}