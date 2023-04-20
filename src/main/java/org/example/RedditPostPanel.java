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

    private final JPanel postHeader;

    ImageIcon imageIcon;

    ImagePanel image;

    JLabel titleLabel;

    private static final Color SELECTED_BACKGROUND_COLOR = new Color(209, 226, 228);


    public RedditPostPanel(RedditPost post) {
        super(new BorderLayout());
        this.post = post;
        postHeader = new JPanel(new BorderLayout());

        titleLabel = new JLabel(post.title());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        postHeader.add(titleLabel, BorderLayout.CENTER);
        add(postHeader, BorderLayout.NORTH);
        JLabel votes = new JLabel(post.score() + " votes");
        votes.setFont(new Font("Arial", Font.PLAIN, 16));
        votes.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        postHeader.add(votes, BorderLayout.EAST);

        loadImage();

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton linkButton = new JButton("Link");
        linkButton.setFont(new Font("Arial", Font.BOLD, 15));
        cleanButton(linkButton);
        linkButton.addActionListener(e -> openUrl(post.url()));
        buttonPanel.add(linkButton);
        linkButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton commentsButton = new JButton("Comments");
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

    private void loadImage() {
        String thumbnail = post.thumbnail();

        // handle nsfw images
        thumbnail = !Objects.equals(thumbnail, "nsfw") ? thumbnail : "https://img.freepik.com/premium-vector/nsfw-sign-safe-work-censorship-vector-stock-illustration_100456-8356.jpg?w=150";

        // load image
        try {
            imageIcon = new ImageIcon(new URL(thumbnail));
            setBaseHeight();
            image = new ImagePanel(imageIcon);

            if (!imageLoaded()) {
                imageIcon = new ImageIcon("src/main/resources/video-player.png");
                Image image_temp = imageIcon.getImage();
                Image newimg = image_temp.getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH);
                setBaseHeight();
                imageIcon = new ImageIcon(newimg);
                image = new ImagePanel(imageIcon);
                System.out.println("Image not loaded");
                System.out.println(imageLoaded());
            }

            image.setVisible(true);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            image.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
            postHeader.add(image, BorderLayout.WEST);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

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
        image.setBackground(color);
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
