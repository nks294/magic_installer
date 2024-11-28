package nks.magic.emw.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.border.Border;

public class AwesomeBorder implements Border {

    private int stroke;  // 테두리 두께
    private Color lightEdgeColor;  // 밝은 테두리 색상
    private Color transparentDarkOverlay;  // 투명한 검정색 오버레이
    private boolean isPressed;  // 버튼이 눌렸는지 여부에 따라 테두리 반전

    // 생성자
    public AwesomeBorder(int stroke, boolean isPressed) {
        this.stroke = 3;
        this.isPressed = isPressed;  // 눌렸는지 여부
        this.lightEdgeColor = new Color(255, 255, 255, 100);  // 기본 밝은 테두리 색상
        this.transparentDarkOverlay = new Color(0, 0, 0, 100);  // 기본 어두운 테두리 색상
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(stroke, stroke, stroke + 3, stroke);  // 하단에 더 많은 공간을 줌
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        // 버튼이 눌렸을 때 상단/좌측과 하단/우측의 테두리 색상 반전
        Color topLeftColor = isPressed ? transparentDarkOverlay : lightEdgeColor;
        Color bottomRightColor = isPressed ? lightEdgeColor : transparentDarkOverlay;

        // 상단 및 좌측 테두리
        g2d.setStroke(new BasicStroke(stroke));
        g2d.setColor(topLeftColor);
        g2d.drawLine(x, y, x + width - 1, y);  // 상단 테두리
        g2d.drawLine(x, y, x, y + height - 1);  // 좌측 테두리

        // 하단 테두리 (더 두껍게)
        g2d.setStroke(new BasicStroke(stroke + 3));
        g2d.setColor(bottomRightColor);
        g2d.drawLine(x, y + height - 1, x + width - 1, y + height - 1);  // 하단 테두리

        // 우측 테두리
        g2d.setStroke(new BasicStroke(stroke));
        g2d.drawLine(x + width - 1, y, x + width - 1, y + height - 1);  // 우측 테두리
    }
}
