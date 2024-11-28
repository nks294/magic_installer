package nks.magic.emw.style;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class AwesomeProgressBarStyle extends BasicProgressBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new AwesomeProgressBarStyle();
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        JProgressBar progressBar = (JProgressBar) c;
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경 색상 그리기 (기본 상태)
        g2D.setColor(AwesomeStyle.mainColor.darker());
        g2D.fill(new Rectangle2D.Double(0, 0, progressBar.getWidth(), progressBar.getHeight()));

        // 진행된 부분 색상 그리기
        g2D.setColor(AwesomeStyle.btnColor);
        double percentComplete = progressBar.getPercentComplete();
        if (percentComplete >= 0) {
            int progressWidth = (int) (progressBar.getWidth() * percentComplete);
            g2D.fill(new Rectangle2D.Double(0, 0, progressWidth, progressBar.getHeight()));
        }

        // 테두리 그리기
        g2D.setColor(AwesomeStyle.btnBorderColor);
        g2D.draw(new Rectangle2D.Double(0, 0, progressBar.getWidth() - 1, progressBar.getHeight() - 1));

        // 텍스트 그리기 (진행률 표시)
        if (progressBar.isStringPainted()) {
            g2D.setColor(AwesomeStyle.textColor);
            String progressText = progressBar.getString();
            FontMetrics fm = g2D.getFontMetrics();
            int textWidth = fm.stringWidth(progressText);
            int textHeight = fm.getHeight();
            int x = (progressBar.getWidth() - textWidth) / 2;
            int y = (progressBar.getHeight() + textHeight) / 2 - fm.getDescent() - 1;
            g2D.drawString(progressText, x, y);
        }
    }
}