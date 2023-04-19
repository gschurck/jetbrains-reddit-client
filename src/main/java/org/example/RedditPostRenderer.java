package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class RedditPostRenderer extends JLabel implements ListCellRenderer<RedditPost> {


    @Override
    public Component getListCellRendererComponent(JList<? extends RedditPost> jList, RedditPost redditPost, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
//        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        RedditPostPanel panel = new RedditPostPanel(redditPost);
        // add jlabel to panel

        // Set the text and icon of the list cell
//        setText(redditPost.getTitle());
        String thumbnail = redditPost.getThumbnail();
        thumbnail = !Objects.equals(thumbnail, "nsfw") ? thumbnail : "https://img.freepik.com/premium-vector/nsfw-sign-safe-work-censorship-vector-stock-illustration_100456-8356.jpg?w=150";
        try {
            setIcon(new ImageIcon(new URL(thumbnail)));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ;
        setHorizontalTextPosition(JLabel.RIGHT);
        // Set the tool tip text of the list cell to the post URL
        setToolTipText(redditPost.getUrl());

        Color backgroundColor = RedditClient.getHoveredJListIndex() == index ? new Color(209, 226, 228) : Color.WHITE;
        panel.setBackground(backgroundColor);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 5, 0));

        // Create the first button
        JButton openButton = new JButton("Open");
        openButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URL(redditPost.getUrl()).toURI());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        buttonPanel.add(openButton, BorderLayout.CENTER);

        // Create the second button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Add your code to save the post here
            System.out.println("Saving post: " + redditPost.getTitle());
        });
        buttonPanel.add(saveButton, BorderLayout.EAST);
        add(buttonPanel);

        panel.add(this);
        return panel;
//        return this;
    }


}