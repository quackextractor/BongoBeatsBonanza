package view;

import controller.GameController;
import service.PNGImagePool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class GameFrame extends JFrame {
    private final int screenWidth = 700; // Assuming initial screen width
    private final int lineSpacing = screenWidth / 4;
    private final int lineWidth = screenWidth / 20;
    private final int firstLineX = (screenWidth - lineSpacing) / 2 - lineWidth / 2;
    private final int secondLineX = firstLineX + lineSpacing;
    private final GameJPanel gameJPanel;
    private java.util.List<PNGImage> activeImages = new ArrayList<>();
    private PNGImagePool pool = new PNGImagePool();
    private Random random = new Random();
    private int misses = 0;
    private int hits = 0;
    private int totalAttempts = 0;
    private int totalAccuracy = 0;

    public GameFrame(String levelName) {
        gameJPanel = new GameJPanel(firstLineX, secondLineX);
        add(gameJPanel);
        setTitle(levelName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                GameController.endGame();
            }
        });


        setLayout(new GridLayout(1, 2)); // Two buttons for two lanes
        JButton lane1Button = new JButton("Lane 1");
        JButton lane2Button = new JButton("Lane 2");
        add(lane1Button);
        add(lane2Button);

        lane1Button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkCatch(1);
            }
        });

        lane2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCatch(2);
            }
        });

        setVisible(true);

        Timer timer = new Timer(50, e -> {
            spawnImage();
            moveImages();
            checkMisses();
            repaint();
        });
        timer.start();
    }
    private void spawnImage() {
        // Randomly choose a lane (1 or 2)
        int lane = random.nextInt(2) + 1;
        PNGImage image = pool.acquire(lane);
        activeImages.add(image);
    }

    private void moveImages() {
        for (PNGImage image : activeImages) {
            image.move();
            if (image.getYPos() <= 0) {
                // Image reached the top, release it back to the pool
                pool.release(image);
            }
        }
        activeImages.removeIf(image -> image.getYPos() <= 0);
    }

    private void checkMisses() {
        for (PNGImage image : activeImages) {
            if (image.getYPos() <= 0) {
                misses++;
            }
        }
    }

    private void checkCatch(int lane) {
        PNGImage nearestImage = null;
        double minDistance = Double.MAX_VALUE;
        for (PNGImage image : activeImages) {
            if (image.getLane() == lane) {
                double distance = Math.abs(image.getYPos() - 0); // Distance from top of the screen
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestImage = image;
                }
            }
        }

        if (nearestImage != null) {
            int accuracy = calculateAccuracy(nearestImage);
            totalAccuracy += accuracy;
            totalAttempts++;
            if (accuracy > 0) {
                hits++;
            }
            activeImages.remove(nearestImage);
            pool.release(nearestImage);
        }
    }

    private int calculateAccuracy(PNGImage image) {
        // Accuracy calculation logic based on how close the image was caught to the top of the screen
        double distance = Math.abs(image.getYPos() - 0); // Distance from top of the screen
        double accuracyPercentage = (1 - (distance / 600)) * 100; // 600 is the height of the screen
        return (int) accuracyPercentage;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (PNGImage image : activeImages) {
            g.setColor(Color.BLACK);
            g.fillRect(image.getLane() * 400 - 200, image.getYPos(), 50, 50); // Placeholder for PNG image
        }
        g.drawString("Misses: " + misses, 10, 20);
        g.drawString("Hits: " + hits, 10, 40);
        g.drawString("Total attempts: " + totalAttempts, 10, 60);
        g.drawString("Total accuracy: " + (totalAttempts == 0 ? 0 : totalAccuracy / totalAttempts) + "%", 10, 80);
    }
}

