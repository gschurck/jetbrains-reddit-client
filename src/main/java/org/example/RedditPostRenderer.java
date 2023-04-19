package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class RedditPostRenderer extends DefaultListCellRenderer {


    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof RedditPost post) {

            // Set the text and icon of the list cell
            setText(post.getTitle());
            try {
                setIcon(new ImageIcon(new URL(post.getThumbnail())));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            ;
            setHorizontalTextPosition(JLabel.RIGHT);
            // Set the tool tip text of the list cell to the post URL
            setToolTipText(post.getUrl());

            Color backgroundColor = RedditClient.getHoveredJListIndex() == index ? new Color(209, 226, 228) : Color.WHITE;
            setBackground(backgroundColor);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2, 5, 0));

            // Create the first button
            JButton openButton = new JButton("Open");
            openButton.addActionListener(e -> {
                try {
                    Desktop.getDesktop().browse(new URL(post.getUrl()).toURI());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
            buttonPanel.add(openButton, BorderLayout.CENTER);

            // Create the second button
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                // Add your code to save the post here
                System.out.println("Saving post: " + post.getTitle());
            });
            buttonPanel.add(saveButton, BorderLayout.EAST);
            add(buttonPanel);
        }


        return this;
    }
}