package main.java.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Accueil {

    private JFrame frame;
    private boolean startButtonClicked = false;
    private JProgressBar progressBar;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Accueil accueil = new Accueil();
            accueil.show();
        });
    }

    public void show() {
        frame = new JFrame("Bienvenue : TSURO \n" + "");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon backgroundImage = new ImageIcon("src/main/ressources/fondv1.jpg");
        Image originalImage = backgroundImage.getImage();
        Image resizedImage = originalImage.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        ImageIcon resizedBackgroundImage = new ImageIcon(resizedImage);
        JLabel background = new JLabel(resizedBackgroundImage);


        JButton startButton = new JButton("Appuyer pour commencer");
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        startButton.setFont(buttonFont);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.PLAIN, 16)); // Ajustez la taille de la police
        progressBar.setPreferredSize(new Dimension(1000, 30)); // Ajustez la taille de la barre de progression

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startButtonClicked = true;
                frame.dispose();
                // Lancer le jeu depuis la classe Game
                //Game game = new Game();
                //game.start();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(startButton);

        JPanel progressBarPanel = new JPanel();
        progressBarPanel.setOpaque(false);
        progressBarPanel.setLayout(new BorderLayout());
        progressBarPanel.add(progressBar, BorderLayout.SOUTH);

        background.setLayout(new BorderLayout());
        background.add(buttonPanel, BorderLayout.CENTER);
        background.add(progressBarPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(background);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Animation de la barre de progression
        Timer timer = new Timer(50, new ActionListener() {
            //Timer timer; 
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = progressBar.getValue() + 1;
                if (value > progressBar.getMaximum()) {
                    value = progressBar.getMinimum();
                    //timer.stop(); // Arrête le timer lorsque la valeur atteint 100
                }
                progressBar.setValue(value);
            }
        });
        timer.start();
        
    }

    public boolean isStartButtonClicked() {
        return startButtonClicked;
    }
}
