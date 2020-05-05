package lib;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Utility {

    public static Font loadFont(String path, int style, int size) {
        Font font = null;

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Utility.class.getResourceAsStream(path)).deriveFont(style, size);
        } catch (FontFormatException e) {
            System.out.println("Bad font format.Expecting True Type Font");
        } catch (IOException e) {
            System.out.println("Unable to open font file");
        }

        return font;
    }

    public static ImageIcon loadIcon(String path) {
        return new ImageIcon(Utility.class.getResource(path));
    }
}
