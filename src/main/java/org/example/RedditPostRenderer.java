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
//        JPanel wrapper = new JPanel(new BorderLayout());
        // minimal dimensions
//        wrapper.add(panel, BorderLayout.CENTER);
        panel.add(this);
        setText(redditPost.getTitle());
        String thumbnail = redditPost.getThumbnail();

        thumbnail = !Objects.equals(thumbnail, "nsfw") ? thumbnail : "https://img.freepik.com/premium-vector/nsfw-sign-safe-work-censorship-vector-stock-illustration_100456-8356.jpg?w=150";
        try {
            ImageIcon imageIcon = new ImageIcon(new URL(thumbnail));
            if (imageIcon.getImageLoadStatus() == 4) { // if image is not loaded increase size
//                setPreferredSize(new Dimension(100, 100));
                panel.setPreferredSize(new Dimension(100, 100));
            } else {
                panel.setPreferredSize(new Dimension(100, 150));
            }
            setIcon(imageIcon);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ;
        setHorizontalTextPosition(JLabel.RIGHT);
        setToolTipText(redditPost.getUrl());

        Color backgroundColor = RedditClient.getHoveredJListIndex() == index ? new Color(209, 226, 228) : Color.WHITE;
        this.setBackground(backgroundColor);
        panel.setBackground(backgroundColor);
        if (isSelected) {
            panel.showButtons();
        } else {
            panel.hideButtons();
        }
        panel.getLinkButton().addActionListener(e -> System.out.println("Open Link"));
        panel.getCommentsButton().addActionListener(e -> System.out.println("Open Comments"));

//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JButton test = new JButton("Test");
//        buttonPanel.add(test);
//        wrapper.add(buttonPanel, BorderLayout.NORTH);
//        buttonPanel.setBackground(backgroundColor);
//        test.addActionListener(e -> System.out.println("Test"));
        return panel;
//        return this;
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}