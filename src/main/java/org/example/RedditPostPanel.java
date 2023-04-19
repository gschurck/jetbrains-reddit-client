package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;


public class RedditPostPanel extends JPanel {
    private final RedditPost post;

    private final JPanel buttonPanel;
    private final JButton linkButton;
    private final JButton commentsButton;

    private final JPanel postHeader;

    ImageIcon imageIcon;

    private static final Color SELECTED_BACKGROUND_COLOR = new Color(209, 226, 228);


    public RedditPostPanel(RedditPost post) {
        super(new BorderLayout());
        this.post = post;
        postHeader = new JPanel(new BorderLayout());


        JLabel titleLabel = new JLabel(post.title());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        postHeader.add(titleLabel, BorderLayout.CENTER);
        add(postHeader, BorderLayout.NORTH);
        JLabel votes = new JLabel(post.score() + " votes");
        votes.setFont(new Font("Arial", Font.PLAIN, 16));
        votes.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        postHeader.add(votes, BorderLayout.EAST);


        String thumbnail = post.thumbnail();

        // handle nsfw images
        thumbnail = !Objects.equals(thumbnail, "nsfw") ? thumbnail : "https://img.freepik.com/premium-vector/nsfw-sign-safe-work-censorship-vector-stock-illustration_100456-8356.jpg?w=150";
        try {
            imageIcon = new ImageIcon(new URL(thumbnail));
            setBaseHeight();
            ImagePanel image = new ImagePanel(imageIcon);
            image.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
            postHeader.add(image, BorderLayout.WEST);
            if (imageLoaded()) {
                image.setVisible(true);
                titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            } else {
                image.setVisible(false);
                titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 160, 0, 0));
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        linkButton = new JButton("Link");
        linkButton.setFont(new Font("Arial", Font.BOLD, 15));
        cleanButton(linkButton);
        linkButton.addActionListener(e -> openUrl(post.url()));
        buttonPanel.add(linkButton);
        linkButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        commentsButton = new JButton("Comments");
        commentsButton.setFont(new Font("Arial", Font.BOLD, 15));
        cleanButton(commentsButton);
        commentsButton.addActionListener(e -> openUrl(post.permalink()));
        buttonPanel.add(commentsButton);
        commentsButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        buttonPanel.setMinimumSize(new Dimension(0, 200));
        buttonPanel.setVisible(false);
        setBackgroundAllChilds(Color.WHITE);
        addListeners();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!isSelected())
                    setBackgroundAllChilds(SELECTED_BACKGROUND_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (!isSelected())
                    setBackgroundAllChilds(Color.WHITE);

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isSelected()) {
                    unselectPost();
                } else {
                    selectPost();
                }
                System.out.println("Clicked");
            }
        });
    }

    private boolean isSelected() {
        return RedditClient.getSelectedPanel() != null && Objects.equals(RedditClient.getSelectedPanel().post.name(), post.name());
    }

    private void setBackgroundAllChilds(Color color) {
        this.setBackground(color);
        postHeader.setBackground(color);
        buttonPanel.setBackground(color);
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectPost() {
        setBackgroundAllChilds(SELECTED_BACKGROUND_COLOR);
        int additionalHeight = imageLoaded() ? 0 : 30;
        setPreferredSize(new Dimension(0, getDefaultHeight() + additionalHeight));
        showButtons();
        RedditPostPanel selectedPanel = RedditClient.getSelectedPanel();
        if (selectedPanel != null) {
            selectedPanel.unselectPost();
        }
        RedditClient.setSelectedPanel(RedditPostPanel.this);
    }

    private void unselectPost() {
//        setBackgroundAllChilds(Color.WHITE);
        setBackgroundAllChilds(Color.WHITE);
        setBaseHeight();
        hideButtons();
        RedditClient.setSelectedPanel(null);
    }

    private int getDefaultHeight() {
        if (imageLoaded()) { // if image is loaded increase size
            return 200;
        } else {
            return 60;
        }
    }

    private void setBaseHeight() {
        if (imageLoaded()) { // if image is loaded decrease size
            setPreferredSize(new Dimension(100, getDefaultHeight() - 40));
        } else {
            setPreferredSize(new Dimension(100, getDefaultHeight() + 10));
        }
    }

    private boolean imageLoaded() {
        return imageIcon.getImageLoadStatus() == 8;
    }


    private void cleanButton(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }


    public void showButtons() {
        buttonPanel.setVisible(true);
    }

    public void hideButtons() {
        buttonPanel.setVisible(false);
    }
}
