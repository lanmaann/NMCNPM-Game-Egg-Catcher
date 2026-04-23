package util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImageLoader {

    public static Image load(String path) {
        URL url = ImageLoader.class.getResource(path);

        if (url == null) {
            System.out.println("❌ Missing image: " + path);
            return new ImageIcon(new byte[0]).getImage(); // fallback
        }

        return new ImageIcon(url).getImage();
    }
}