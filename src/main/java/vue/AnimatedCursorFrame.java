package main.java.vue;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;




/**
 * Cette classe représente une fenêtre personnalisée qui gère des curseurs animés.
 * Elle permet de charger des images pour le curseur par défaut et le curseur survolé,
 * et de créer des curseurs personnalisés à partir de ces images.
 */
public class AnimatedCursorFrame extends JFrame {
    
    /**
     * Curseur par défaut.
     */
    private Cursor defaultCursor;
    
    /**
     * Curseur survolé.
     */
    private Cursor hoverCursor;

    /**
     * Constructeur de la classe AnimatedCursorFrame.
     * @param defaultCursorPath Le chemin d'accès à l'image du curseur par défaut.
     * @param hoverCursorPath Le chemin d'accès à l'image du curseur survolé.
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


    /* 
    public AnimatedCursorFrame(String defaultCursorPath, String hoverCursorPath, int cursorWidth, int cursorHeight) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image defaultCursorImage = toolkit.getImage(defaultCursorPath).getScaledInstance(cursorWidth, cursorHeight, Image.SCALE_DEFAULT);
        Image hoverCursorImage = toolkit.getImage(hoverCursorPath).getScaledInstance(cursorWidth, cursorHeight, Image.SCALE_DEFAULT);
        this.defaultCursor = toolkit.createCustomCursor(defaultCursorImage, new Point(0, 0), "defaultCursor");
        this.hoverCursor = toolkit.createCustomCursor(hoverCursorImage, new Point(0, 0), "hoverCursor");
    }
*/

    /**
     * Obtient le curseur par défaut.
     * @return Le curseur par défaut.
     */
    public Cursor getDefaultCursor() {
        return defaultCursor;
    }

    /**
     * Obtient le curseur survolé.
     * @return Le curseur survolé.
     */
    public Cursor getHoverCursor() {
        return hoverCursor;
    }

    /**
     * Arrête l'animation du curseur en rétablissant le curseur par défaut.
     * @param frame La JFrame pour laquelle l'animation du curseur doit être arrêtée.
     */
    public void stopAnimation(JFrame frame) {
        // Cette méthode peut être utilisée pour réinitialiser le curseur par défaut lorsque la fenêtre est fermée ou modifiée.
        frame.setCursor(Cursor.getDefaultCursor());
    }
}
