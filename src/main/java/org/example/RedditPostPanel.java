package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class RedditPostPanel extends JPanel {
    private RedditPost post;
    private JButton linkButton;
    private JButton commentsButton;

    public RedditPostPanel(RedditPost post) {
        super(new BorderLayout());
        this.post = post;

//        JLabel titleLabel = new JLabel(post.getTitle());
//        add(titleLabel, BorderLayout.NORTH);

        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(0, 200));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linkButton = new JButton("Open Link");
//        linkButton.addActionListener(e -> openUrl(post.getUrl()));
        linkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Open Link");
            }
        });
        buttonPanel.add(linkButton);

        commentsButton = new JButton("Open Comments");
        commentsButton.addActionListener(e -> openUrl(post.getPermalink()));
        buttonPanel.add(commentsButton);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMinimumSize(new Dimension(0, 200));

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getCommentsButton() {
        return commentsButton;
    }

    public JButton getLinkButton() {
        return linkButton;
    }

    public void showButtons() {
        linkButton.setVisible(true);
        commentsButton.setVisible(true);
    }

    public void hideButtons() {
        linkButton.setVisible(false);
        commentsButton.setVisible(false);
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
