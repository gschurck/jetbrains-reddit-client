package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class RedditPostPanel extends JPanel {
    private RedditPost post;
    private JButton linkButton;
    private JButton commentsButton;

    public RedditPostPanel(RedditPost post) {
        super(new BorderLayout());
        this.post = post;

        JLabel titleLabel = new JLabel(post.getTitle());
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linkButton = new JButton("Open Link");
        linkButton.addActionListener(e -> openUrl(post.getUrl()));
        buttonPanel.add(linkButton);

        commentsButton = new JButton("Open Comments");
        commentsButton.addActionListener(e -> openUrl(post.getPermalink()));
        buttonPanel.add(commentsButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
