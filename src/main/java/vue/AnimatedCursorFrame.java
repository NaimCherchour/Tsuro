package main.java.vue;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Classe AnimatedCursorFrame étendant JFrame.
 * Cette classe est conçue pour créer une fenêtre avec des curseurs animés personnalisés.
 */
public class AnimatedCursorFrame extends JFrame {
    private Cursor defaultCursor;  // Curseur par défaut de la fenêtre
    private Cursor hoverCursor;    // Curseur lors du survol

    /**
     * Constructeur de la classe AnimatedCursorFrame.
     * Crée des curseurs personnalisés à partir des chemins d'images fournis.
     *
     * @param defaultCursorPath Chemin vers l'image du curseur par défaut.
     * @param hoverCursorPath Chemin vers l'image du curseur de survol.
     */
    public AnimatedCursorFrame(String defaultCursorPath, String hoverCursorPath) {
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            // Chargement des images des curseurs à partir des chemins spécifiés
            BufferedImage defaultCursorImage = ImageIO.read(new File(defaultCursorPath));
            BufferedImage hoverCursorImage = ImageIO.read(new File(hoverCursorPath));
            // Création des curseurs personnalisés à partir des images
            this.defaultCursor = toolkit.createCustomCursor(defaultCursorImage, new Point(0, 0), "defaultCursor");
            this.hoverCursor = toolkit.createCustomCursor(hoverCursorImage, new Point(0, 0), "hoverCursor");
        } catch (IOException e) {
            e.printStackTrace();  // Affichage des erreurs en cas de problème de lecture des fichiers
        }
    }

    /**
     * Obtient le curseur par défaut.
     *
     * @return Le curseur par défaut de la fenêtre.
     */
    public Cursor getDefaultCursor() {
        return defaultCursor;
    }

    /**
     * Obtient le curseur de survol.
     *
     * @return Le curseur utilisé lors du survol d'éléments interactifs de la fenêtre.
     */
    public Cursor getHoverCursor() {
        return hoverCursor;
    }

    /**
     * Arrête l'animation du curseur sur la fenêtre spécifiée en rétablissant le curseur par défaut.
     *
     * @param frame La fenêtre sur laquelle arrêter l'animation du curseur.
     */
    public void stopAnimation(JFrame frame) {
        frame.setCursor(Cursor.getDefaultCursor());  // Réinitialisation au curseur par défaut du système
    }
}
