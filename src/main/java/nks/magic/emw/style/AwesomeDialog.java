package nks.magic.emw.style;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AwesomeDialog extends JDialog {

    public AwesomeDialog(String title, String message) {
        setTitle(title);
        setModal(true);
        setSize(new Dimension(300, 150));
        setLocationRelativeTo(null);
        
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2D = (Graphics2D) g;
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2D.setColor(AwesomeStyle.mainColor);
                g2D.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPanel);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(AwesomeStyle.textColor);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2D = (Graphics2D) g;
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2D.setColor(AwesomeStyle.secondaryColor);
                g2D.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        buttonPanel.setLayout(new BorderLayout());
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.setUI(new AwesomeButton());
        okButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton, BorderLayout.CENTER);
    }

    public static void showDialog(String title, String message) {
        AwesomeDialog dialog = new AwesomeDialog(title, message);
        dialog.setVisible(true);
    }
}