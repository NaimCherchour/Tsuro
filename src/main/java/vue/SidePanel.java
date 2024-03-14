package main.java.vue;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SidePanel extends JPanel {
    private GameBoardUI gameBoardUI;

    public SidePanel(GameBoardUI gameBoardUI) throws IOException {
        this.gameBoardUI = gameBoardUI;
        setPreferredSize(new Dimension(200, 600)); // Set fixed width and adjust height automatically
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        for (int i = 0; i < 3; i++) {
            add(Box.createVerticalGlue());
            TuilePanel tuilePanel = new TuilePanel();
            add(tuilePanel);

            // Add rigid area to reduce spacing between tile panel and button
            add(Box.createRigidArea(new Dimension(0, 10))); // Adjust the height as needed

            JButton bouton = new JButton("Rotate");
            bouton.setAlignmentX(Component.CENTER_ALIGNMENT); // Align button to the center horizontally
            bouton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rotateTile();
                }
            });
            add(bouton);
        }
    }

    // Method to rotate the tile and repaint
    private void rotateTile() {
        // Call the rotateTile method in the GameBoardUI class
        gameBoardUI.rotateTile();
    }
}

