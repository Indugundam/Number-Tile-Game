package org.example;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class NumberTileGame {

    static final int MAX_COLUMNS = 3;
    static final int MAX_ROWS = 3;
    static JFrame jfMainWindow;
    static JButton jbutton = new JButton("Retry");
    static JPanel jpTiles[][]; // fixed
    static int boards[][][] = {
            {
                    {7, 8, 2},
                    {5, 6, 1},
                    {4, 3, 0},
            },
            {
                    {5, 8, 7},
                    {3, 6, 2},
                    {4, 1, 0},
            },
            {
                    {8, 6, 2},
                    {5, 4, 7},
                    {1, 3, 0},
            },
            {
                    {8, 6, 5},
                    {3, 2, 7},
                    {4, 1, 0},
            },
    };
    static int board[][];
    static Color tileBorderColor = new Color(187, 173, 160);
    static Color tileBackgroundColor = new Color(205, 193, 180);
    static JLabel jlTimer = new JLabel();
    static JTextField jtfMoves = new JTextField();
    static Timer timer;
    static int noOfMoves = 0;
    static int seconds;

    public static void main(String[] args) {

        Random random = new Random();
        board = boards[random.nextInt(boards.length)];

        jfMainWindow = new JFrame("Number Tile Game - Indu Gundam");
        jfMainWindow.setSize(400, 500);
        jfMainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfMainWindow.setLayout(null);

        jpTiles = new JPanel[MAX_ROWS][MAX_COLUMNS];

        for (int i = 0, y = 50; i < MAX_ROWS; i++, y += 50) {
            for (int j = 0, x = 80; j < MAX_COLUMNS; j++, x += 50) {
                jpTiles[i][j] = new JPanel();
                jpTiles[i][j].setBorder(BorderFactory.createLineBorder(tileBorderColor, 5));
                jpTiles[i][j].setBackground(tileBackgroundColor);
                jpTiles[i][j].setSize(45, 45);
                jpTiles[i][j].setLocation(x, y);
                jpTiles[i][j].putClientProperty("i", i);
                jpTiles[i][j].putClientProperty("j", j);

                JLabel jlNumber = new JLabel("0");
                jlNumber.setFont(new Font("Arial", Font.BOLD, 28));
                jpTiles[i][j].add(jlNumber);

                JLabel jlMoves = new JLabel("No. of moves:");
                jlMoves.setSize(200, 50);
                jlMoves.setFont(new Font("Arial", Font.BOLD, 20));
                jlMoves.setLocation(50, 300);
                jfMainWindow.add(jlMoves);

                jtfMoves.setSize(60, 30);
                jtfMoves.setFont(new Font("Arial", Font.BOLD, 20));
                jtfMoves.setLocation(190, 310);
                jtfMoves.setEditable(false);
                jfMainWindow.add(jtfMoves);

                jpTiles[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        noOfMoves++;
                        jtfMoves.setText(String.valueOf(noOfMoves));
                        process(e);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });

                jfMainWindow.add(jpTiles[i][j]);
            }
        }

        updateBoard();

        jlTimer.setText("Timer");
        jlTimer.setSize(200, 50);
        jlTimer.setFont(new Font("Arial", Font.BOLD, 20));
        jlTimer.setLocation(50, 250);
        jfMainWindow.add(jlTimer);

        jbutton.setLocation(100, 400);
        jbutton.setSize(120, 40);
        jbutton.setFont(new Font("Arial", Font.BOLD, 20));
        jfMainWindow.add(jbutton);

        jbutton.addActionListener((e) -> {
            retryGame();
        });

        timer = new Timer(1000, (e) -> {
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;
            jlTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
            seconds++;
        });
        timer.start();

        jfMainWindow.setVisible(true);
    }

    private static void retryGame() {
        // Reset the board with a new random setup
        Random random = new Random();
        board = boards[random.nextInt(boards.length)];

        // Reset the number of moves
        noOfMoves = 0;
        jtfMoves.setText(String.valueOf(noOfMoves));

        // Reset the timer
        seconds = 0;
        jlTimer.setText("00:00:00");
        timer.restart();

        // Update the board display
        updateBoard();
    }

    private static void updateBoard() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                JLabel jlNumber = (JLabel) (jpTiles[i][j].getComponent(0));
                if (board[i][j] != 0)
                    jlNumber.setText(board[i][j] + "");
                else
                    jlNumber.setText("");
            }
        }
    }

    public static void process(MouseEvent e) {
        if (e.getSource() instanceof JPanel) {
            JPanel clickedPanel = (JPanel) e.getSource();
            int i = (int) clickedPanel.getClientProperty("i");
            int j = (int) clickedPanel.getClientProperty("j");

            update(i, j);
        }
    }

    public static void update(int i, int j) {

        // left
        if (j - 1 >= 0) {
            if (board[i][j - 1] == 0) {
                swap(i, j, i, j - 1);
                updateBoard();
                playSound();
                findWinner();
                return;
            }
        }

        // right
        if (j + 1 < MAX_COLUMNS) {
            if (board[i][j + 1] == 0) {
                swap(i, j, i, j + 1);
                updateBoard();
                playSound();
                findWinner();
                return;
            }
        }

        // top
        if (i - 1 >= 0) {
            if (board[i - 1][j] == 0) {
                swap(i, j, i - 1, j);
                updateBoard();
                playSound();
                findWinner();
                return;
            }
        }

        // bottom
        if (i + 1 < MAX_ROWS) {
            if (board[i + 1][j] == 0) {
                swap(i, j, i + 1, j);
                updateBoard();
                playSound();
                findWinner();
                return;
            }
        }
    }

    private static void swap(int i, int j, int i1, int j1) {
        int temp = board[i][j];
        board[i][j] = board[i1][j1];
        board[i1][j1] = temp;
    }

    private static void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(NumberTileGame.class.getClassLoader().getResourceAsStream("Click.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void findWinner() {
        int winnerBoard[][] = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0},
        };

        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (board[i][j] != winnerBoard[i][j]) {
                    return;
                }
            }
        }

        timer.stop(); // Stop the timer

        JOptionPane.showMessageDialog(null, "You won the game");
    }
}
