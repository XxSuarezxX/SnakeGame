package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SnakeMain {
    private static JFrame menuFrame;
    private static Choice difficultyChoice;
    private static JFrame gameFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                menuFrame = new JFrame("Snake Game Menu");
                menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                menuFrame.setResizable(false);
                menuFrame.setSize(600, 600);
                menuFrame.setLocationRelativeTo(null);

                JPanel menuPanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };

                menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

                JLabel titleLabel = new JLabel("SNAKE GAME");
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
                titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JButton highScoresButton = new JButton("Mejores Puntajes");
                highScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                highScoresButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedDifficulty = difficultyChoice.getSelectedItem();
                        showHighScores(selectedDifficulty);
                    }
                });

                JPanel difficultyPanel = new JPanel();
                difficultyPanel.setOpaque(false);
                difficultyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JLabel difficultyLabel = new JLabel("Dificultad");
                difficultyLabel.setForeground(Color.WHITE);
                difficultyLabel.setFont(new Font("Arial", Font.BOLD, 16));
                difficultyChoice = new Choice();
                difficultyChoice.add("Fácil");
                difficultyChoice.add("Medio");
                difficultyChoice.add("Difícil");
                difficultyChoice.setPreferredSize(new Dimension(100, 20));
                difficultyPanel.add(difficultyLabel);
                difficultyPanel.add(difficultyChoice);
                JButton startButton = new JButton("Iniciar a Jugar");
                startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                startButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        menuFrame.dispose();
                        String selectedDifficulty = difficultyChoice.getSelectedItem();
                        startSnakeGame(selectedDifficulty, menuFrame);
                    }
                });

                JButton creditsButton = new JButton("Créditos");
                creditsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                creditsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(menuFrame, "David Alejandro Suarez Cardenas \n" +
                                "202129225 \n" +
                                "Facultad de Ingeniería \n" +
                                "Ingeniería de Sistemas \n" +
                                "2023 \n" +
                                "PROGRAMACIÓN III \n", "Créditos", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                JPanel imagePanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Image img = new ImageIcon("src/resources/logoUPTC.png").getImage();
                        int x = (getWidth() - img.getWidth(null)) / 2;
                        int y = (getHeight() - img.getHeight(null)) / 2;
                        g.drawImage(img, x, y, this);
                    }
                };

                imagePanel.setPreferredSize(new Dimension(300, 200));
                imagePanel.setOpaque(false);
                menuPanel.add(imagePanel);
                menuPanel.add(Box.createVerticalStrut(20));
                menuPanel.add(titleLabel);
                menuPanel.add(Box.createVerticalStrut(20));
                menuPanel.add(highScoresButton);
                menuPanel.add(Box.createVerticalStrut(20));
                menuPanel.add(difficultyPanel);
                menuPanel.add(Box.createVerticalStrut(10));
                menuPanel.add(startButton);
                menuPanel.add(Box.createVerticalStrut(20));
                menuPanel.add(creditsButton);
                menuPanel.add(Box.createVerticalStrut(20));
                menuFrame.add(menuPanel);
                menuFrame.setVisible(true);
            }
        });
    }

    private static void showHighScores(String difficulty) {
        JFrame highScoresFrame = new JFrame("Mejores Puntajes");
        JTextArea highScoresArea = new JTextArea(20, 50);
        highScoresArea.setEditable(false);
        try {
            File scoresFile = new File("scores.txt");
            Scanner scanner = new Scanner(scoresFile);
            while (scanner.hasNextLine()) {
                highScoresArea.append(scanner.nextLine() + "\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            highScoresArea.setText("No hay puntajes guardados.");
        }

        highScoresFrame.add(new JScrollPane(highScoresArea));
        highScoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        highScoresFrame.setResizable(false);
        highScoresFrame.setSize(400, 300);
        highScoresFrame.setLocationRelativeTo(null);
        highScoresFrame.setVisible(true);
        highScoresFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                highScoresFrame.setVisible(false);
                menuFrame.setVisible(true);
            }
        });
    }

    private static void startSnakeGame(String difficulty, JFrame menuFrame) {
        if (gameFrame != null) {
            gameFrame.dispose();
        }
        int initialDelay = 120;
        if ("Medio".equals(difficulty)) {
            initialDelay = 80;
        } else if ("Difícil".equals(difficulty)) {
            initialDelay = 50;
        }

        gameFrame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame(menuFrame, initialDelay);
        gameFrame.add(game);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        gameFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = gameFrame.getContentPane().getWidth();
                int height = gameFrame.getContentPane().getHeight();
                game.resizeGameArea(width, height);
            }
        });

        gameFrame.setSize(game.getGameWidth(), game.getGameHeight());
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
}
