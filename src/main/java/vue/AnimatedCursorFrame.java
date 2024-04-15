package main.java.vue;

import javax.swing.*;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

public class AnimatedCursorFrame extends JFrame {
    private Cursor defaultCursor;
    private Cursor hoverCursor;

    public AnimatedCursorFrame(String defaultCursorPath, String hoverCursorPath) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image defaultCursorImage = toolkit.getImage(defaultCursorPath);
        Image hoverCursorImage = toolkit.getImage(hoverCursorPath);
        this.defaultCursor = toolkit.createCustomCursor(defaultCursorImage, new Point(0, 0), "defaultCursor");
        this.hoverCursor = toolkit.createCustomCursor(hoverCursorImage, new Point(0, 0), "hoverCursor");
    }

    public Cursor getDefaultCursor() {
        return defaultCursor;
    }

    public Cursor getHoverCursor() {
        return hoverCursor;
    }

    public void stopAnimation(JFrame frame) {
        // This method can be used to reset the cursor to default when the window is closed or changed.
        frame.setCursor(Cursor.getDefaultCursor());
    }
}
