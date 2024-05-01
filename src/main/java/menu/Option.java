package main.java.menu;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Option {
private static Clip clip; // Déclarer la variable Clip pour le son
private static boolean soundMuted = false; // Indicateur de son coupé

public static void gererClicSurBoutonOption(JFrame frame, Clip clips) {
frame.getContentPane().removeAll();

ImageIcon backgroundIcon = new ImageIcon("src/main/resources/fond.png");
JLabel background = new JLabel(backgroundIcon);
background.setLayout(new BorderLayout());
frame.setContentPane(background);

clip = clips;

// Créer un bouton pour augmenter le volume
JButton increaseVolumeButton = new JButton("Augmenter le volume");
increaseVolumeButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
adjustVolume(0.1f); // Augmenter le volume de 10%
}
});

// Créer un bouton pour diminuer le volume
JButton decreaseVolumeButton = new JButton("Diminuer le volume");
decreaseVolumeButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
adjustVolume(-0.1f); // Diminuer le volume de 10%
}
});

// Créer un bouton pour couper ou réactiver le son
JButton muteButton = new JButton("Couper le son");
muteButton.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
if (clip != null) {
if (soundMuted) {
clip.start(); // Réactiver le son
muteButton.setText("Couper le son");
muteButton.setForeground(Color.black); // Enlever le style rouge
soundMuted = false;
} else {
clip.stop(); // Couper le son
muteButton.setText("Réactiver le son");
muteButton.setForeground(Color.red); // Appliquer le style rouge
soundMuted = true;
}
}
}
});

// Ajouter les boutons au panneau avec un gestionnaire de disposition approprié
JPanel buttonPanel = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout
buttonPanel.setOpaque(false); // Rendre le panneau transparent pour voir le fond d'écran
GridBagConstraints gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.gridy = 0;
gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants
buttonPanel.add(increaseVolumeButton, gbc); // Ajout du bouton augmenter le volume
gbc.gridy = 1;
buttonPanel.add(decreaseVolumeButton, gbc); // Ajout du bouton diminuer le volume
gbc.gridy = 2;
buttonPanel.add(muteButton, gbc); // Ajout du bouton couper ou réactiver le son
// Ajouter les boutons au panneau avec un gestionnaire de disposition approprié
background.add(buttonPanel, BorderLayout.CENTER); // Ajout du panneau au fond d'écran

frame.revalidate();
frame.repaint();
}

private static void adjustVolume(float value) {
if (clip != null) {
FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
float currentVolume = gainControl.getValue();
float newVolume = currentVolume + value;
gainControl.setValue(newVolume);
}
}
}
