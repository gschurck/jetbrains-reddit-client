package org.example;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

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

        ;
        setHorizontalTextPosition(JLabel.RIGHT);
        setToolTipText(redditPost.getUrl());

        Color backgroundColor = new Color(209, 226, 228);
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