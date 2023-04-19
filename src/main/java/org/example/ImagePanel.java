package org.example;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private final ImageIcon image;

    public ImagePanel(ImageIcon image) {
        this.image = image;
        setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.paintIcon(this, g, 0, 0);
    }
}