package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class RedditClient extends JFrame implements ListSelectionListener {
    private ArrayList<RedditPost> redditPosts;
    private JPanel basePanel;
    private JLabel spinner;
    private JButton linkButton;
    private JButton commentsButton;
    private JLabel thumbnailLabel;
    private ImageIcon thumbnailPlaceholder;
    private RedditPost selectedPost;

    private JScrollBar verticalScrollBar;
    private static RedditPostPanel selectedPanel = null;

    public RedditClient() {
        super("Reddit Client");
        redditPosts = new ArrayList<>();
        basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));

        // Initialize the UI components
//        postListModel = new DefaultListModel<>();
//        postList = new JList<>(postListModel);
//        postList.setModel(postListModel);
//        postList.setCellRenderer(new RedditPostRenderer());
//        postList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        postList.addListSelectionListener(this);

        try {
            spinner = new JLabel(new ImageIcon(new URL("https://media.tenor.com/wpSo-8CrXqUAAAAi/loading-loading-forever.gif")));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        spinner.setVisible(false);

        linkButton = new JButton("Open Link");
        linkButton.addActionListener(e -> openUrl(selectedPost.getUrl()));
        linkButton.setVisible(false);

        commentsButton = new JButton("Open Comments");
        commentsButton.addActionListener(e -> openUrl(selectedPost.getPermalink()));
        commentsButton.setVisible(true);

        thumbnailLabel = new JLabel(thumbnailPlaceholder);
        add(thumbnailLabel, BorderLayout.WEST);
        thumbnailLabel.setVisible(true);

        try {
            ImageIcon spinnerIcon = new ImageIcon(new URL("https://discuss.wxpython.org/uploads/default/original/2X/6/6d0ec30d8b8f77ab999f765edd8866e8a97d59a3.gif"));
            int width = 50;
            int height = 50;
            spinnerIcon.setImage(spinnerIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
            spinner = new JLabel(spinnerIcon);
            spinner.setVisible(false);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        JPanel loadingPanel = new JPanel(new GridBagLayout()); // Update the layout manager to GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        loadingPanel.add(spinner, gbc); // Add spinner label to loadingPanel
//        add(loadingPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(basePanel);
//        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        verticalScrollBar = scrollPane.getVerticalScrollBar();

        add(scrollPane, BorderLayout.CENTER);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Reddit Client"));
        add(topPanel, BorderLayout.NORTH);

//        buttonPanel.add(linkButton);
//        buttonPanel.add(commentsButton);

        add(loadingPanel, BorderLayout.SOUTH);

//        add(thumbnailLabel, BorderLayout.WEST);

        // Set the window properties
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        addListeners();

        // Load the posts
        loadPosts(3, null);
    }

    private void addListeners() {
//        basePanel.addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mouseMoved(MouseEvent me) {
//                System.out.println(me);
////                int index = postList.locationToIndex(me.getPoint());
//                Point p = new Point(me.getX(), me.getY());
//                int index = basePanel.locationToIndex(p);
//                if (index != hoveredJListIndex) {
//                    hoveredJListIndex = index;
//                    postList.repaint();
//                }
//            }
//        });

//        postList.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent me) {
//                System.out.println(me);
//                Point p = new Point(me.getX(), me.getY());
//                int index = postList.locationToIndex(p);
//                if (index != hoveredJListIndex) {
//                    hoveredJListIndex = index;
//                    postList.repaint();
//                }
//            }
//        });

        verticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                boolean scrollbarAtBottom = verticalScrollBar.getValue() == verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount();
                if (scrollbarAtBottom && !spinner.isVisible()) {
                    System.out.println("Hit bottom of list");
//                    postListModel.addElement(new RedditPost("Loading...", "test", "https://picsum.photos/200", "dfdf", "dfdf"));
                    int postsList = redditPosts.size();
                    System.out.println(postsList);
                    if (postsList > 0) {
                        System.out.println("reddit: " + redditPosts.get(postsList - 1));

                        RedditPost afterRedditPost = redditPosts.get(postsList - 1);
                        System.out.println(afterRedditPost);
                        loadPosts(3, afterRedditPost);
                    }

                }
            }
        });
    }

    public static RedditPostPanel getSelectedPanel() {
        return selectedPanel;
    }

    public static void setSelectedPanel(RedditPostPanel selectedPanel) {
        RedditClient.selectedPanel = selectedPanel;
    }

    private String buildURLParams(int limit, RedditPost afterRedditPost) {
        String urlParameters = "?limit=" + limit;
        System.out.println(afterRedditPost);
        if (afterRedditPost != null) {
            urlParameters += "&after=" + afterRedditPost.getName();
        }
        return urlParameters;
    }

    private InputStreamReader makeRequest(String urlParameters) throws IOException {
        if (urlParameters == null) {
            urlParameters = "";
        }
        System.out.println("https://www.reddit.com/r/aww/hot.json" + urlParameters);
        URL jsonUrl = new URL("https://www.reddit.com/r/aww/hot.json" + urlParameters);
        URLConnection connection = jsonUrl.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        return new InputStreamReader(connection.getInputStream());
    }

    private void loadPosts(int limit, RedditPost afterRedditPost) {
        new Thread(() -> {
            spinner.setVisible(true);
            System.out.println("Starting thread...");
            System.out.println("Loading posts...");
            ArrayList<RedditPost> redditPostsList = new ArrayList<>();
            java.lang.reflect.Type listType = new TypeToken<List<RedditPost>>() {
            }.getType();
            String urlParameters = buildURLParams(limit, afterRedditPost);
            Gson gson = new Gson();
            Gson gsonRedditPost = new GsonBuilder().registerTypeAdapter(RedditPost.class, new RedditPostDeserializer()).create();

            try {
                InputStreamReader reader = makeRequest(urlParameters);
                JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                JsonArray jsonArray = jsonObject.getAsJsonObject("data").getAsJsonArray("children");
                System.out.println(jsonArray);
                redditPostsList = gsonRedditPost.fromJson(jsonArray, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(redditPostsList);
            for (RedditPost post : redditPostsList) {
                System.out.println(post.getTitle());
                redditPosts.add(post);
                basePanel.add(new RedditPostPanel(post));
            }
            System.out.println("Ending thread...");
            spinner.setVisible(false);
        }).start();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
//        System.out.println("valueChanged");
//        System.out.println(e);
//        if (!e.getValueIsAdjusting()) {
//            // Get the selected post
//            selectedPost = postList.getSelectedValue();
//            if (selectedPost != null) {
//                // Show the spinner while the thumbnail is loading
//                thumbnailLabel.setIcon(thumbnailPlaceholder);
//                spinner.setVisible(true);
//
//                // Load the thumbnail in a background thread
//                new Thread(() -> {
//                    try {
//                        ImageIcon thumbnailIcon = new ImageIcon(new URL(selectedPost.getThumbnail()));
//                        SwingUtilities.invokeLater(() -> {
//                            thumbnailLabel.setIcon(thumbnailIcon);
//                            spinner.setVisible(false);
//                        });
//                    } catch (MalformedURLException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                }).start();
//
//                // Update the buttons visibility
//                linkButton.setVisible(true);
//                commentsButton.setVisible(true);
//            } else {
//                // No post selected, hide the buttons
//                linkButton.setVisible(false);
//                commentsButton.setVisible(false);
//                thumbnailLabel.setIcon(thumbnailPlaceholder);
//            }
//        }
    }


    private void openUrl(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new java.net.URI(url));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

