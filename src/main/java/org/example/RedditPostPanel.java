package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;


public class RedditPostPanel extends JPanel {
    private RedditPost post;

    private JPanel buttonPanel;
    private JButton linkButton;
    private JButton commentsButton;

    ImageIcon imageIcon;
    private ImagePanel image;

    private static final Color SELECTED_BACKGROUND_COLOR = new Color(209, 226, 228);


    public RedditPostPanel(RedditPost post) {
        super(new BorderLayout());
        this.post = post;
        JPanel postHeader = new JPanel(new BorderLayout());


        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        String thumbnail = post.getThumbnail();

        // handle nsfw images
        thumbnail = !Objects.equals(thumbnail, "nsfw") ? thumbnail : "https://img.freepik.com/premium-vector/nsfw-sign-safe-work-censorship-vector-stock-illustration_100456-8356.jpg?w=150";
        try {
            imageIcon = new ImageIcon(new URL(thumbnail));
            setBaseHeight();
            image = new ImagePanel(imageIcon);
            image.setBackground(Color.WHITE);
            add(image, BorderLayout.CENTER);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        setBackground(Color.WHITE);
//        setMinimumSize(new Dimension(0, 300));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        linkButton = new JButton("Open Link");
//        linkButton.addActionListener(e -> openUrl(post.getUrl()));
        linkButton.addActionListener(e -> openUrl(post.getUrl()));
        buttonPanel.add(linkButton);

        commentsButton = new JButton("Open Comments");
        commentsButton.addActionListener(e -> openUrl(post.getPermalink()));
        buttonPanel.add(commentsButton);
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMinimumSize(new Dimension(0, 200));
        buttonPanel.setVisible(false);
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
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean isSelected() {
        return RedditClient.getSelectedPanel() != null && Objects.equals(RedditClient.getSelectedPanel().post.getName(), post.getName());
    }

    private void setBackgroundAllChilds(Color color) {
        this.setBackground(color);
        image.setBackground(color);
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
        setPreferredSize(new Dimension(0, getDefaultHeight() + 10));
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
            return 50;
        }
    }

    private boolean imageLoaded() {
        return imageIcon.getImageLoadStatus() == 8;
    }

    private void setBaseHeight() {
        if (imageLoaded()) { // if image is loaded decrease size
            setPreferredSize(new Dimension(100, getDefaultHeight() - 30));
        } else {
            setPreferredSize(new Dimension(100, getDefaultHeight() + 10));
        }
    }


    public JButton getCommentsButton() {
        return commentsButton;
    }

    public JButton getLinkButton() {
        return linkButton;
    }

    public void showButtons() {
        buttonPanel.setVisible(true);
    }

    public void hideButtons() {
        buttonPanel.setVisible(false);
    }
}
