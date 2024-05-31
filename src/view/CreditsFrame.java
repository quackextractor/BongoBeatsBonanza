package view;

import service.ErrorLogger;
import service.MusicPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class CreditsFrame extends JFrame {

    private final JPanel creditsPanel;
    private final MusicPlayer fxPlayer;

    public CreditsFrame() {
        setTitle("Credits");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        fxPlayer = new MusicPlayer(false, "resources/sounds/exit.wav");

        creditsPanel = new JPanel();
        creditsPanel.setLayout(new BoxLayout(creditsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(creditsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(40);
        scrollPane.getVerticalScrollBar().setBlockIncrement(60);
        add(scrollPane, BorderLayout.CENTER);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        FancyJLabel titleLabel = new FancyJLabel("Credits", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                fxPlayer.playDefault();
            }
        });

        setVisible(true);
    }

    public void addCredit(String text, String imagePath, int imageWidth, int imageHeight, String hyperlink, String body, String license) {
        CreditEntry creditEntry = new CreditEntry(text, imagePath, imageWidth, imageHeight, hyperlink, body, license);
        creditsPanel.add(creditEntry);
        creditsPanel.revalidate();
        creditsPanel.repaint();
    }

    public void addTitle(String title) {
        TitleEntry titleEntry = new TitleEntry(title);
        creditsPanel.add(titleEntry);
        creditsPanel.revalidate();
        creditsPanel.repaint();
    }

    private static class CreditEntry extends JPanel {
        public CreditEntry(String text, String imagePath, int imageWidth, int imageHeight, String hyperlink, String body, String license) {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 20, 20, 20));

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

            JLabel textLabel = getjLabel(text, hyperlink);
            textPanel.add(textLabel);
            textPanel.add(Box.createVerticalStrut(10));

            JLabel bodyLabel = new JLabel("<html><div style='width: 300px;'>" + body + "</div></html>");
            bodyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            textPanel.add(bodyLabel);
            textPanel.add(Box.createVerticalStrut(10));

            JLabel licenseLabel = new JLabel("<html><i>" + license + "</i></html>");
            licenseLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            textPanel.add(licenseLabel);

            add(textPanel, BorderLayout.WEST);

            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH));
                JLabel imageLabel = new JLabel(imageIcon);
                add(imageLabel, BorderLayout.EAST);
            }
        }
    }


    private static class TitleEntry extends JPanel {
        public TitleEntry(String title) {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 20, 20, 20));

            FancyJLabel titleLabel = new FancyJLabel(title, JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setShadowOffset(2);
            add(titleLabel, BorderLayout.CENTER);
        }
    }

    private static JLabel getjLabel(String text, String hyperlink) {
        JLabel textLabel = new JLabel("<html><a href='" + hyperlink + "'>" + text + "</a></html>");
        textLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        textLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        textLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (hyperlink.startsWith("mailto:")) {
                        Desktop.getDesktop().mail(new URI(hyperlink));
                    } else {
                        Desktop.getDesktop().browse(new URI(hyperlink));
                    }
                } catch (Exception ex) {
                    ErrorLogger.logStackTrace(ex);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                textLabel.setText("<html><a href='" + hyperlink + "' style='color:blue;'>" + text + "</a></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                textLabel.setText("<html><a href='" + hyperlink + "'>" + text + "</a></html>");
            }
        });
        return textLabel;
    }
}
