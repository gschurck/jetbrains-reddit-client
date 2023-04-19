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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class RedditClient extends JFrame implements ListSelectionListener {
    private final ArrayList<RedditPost> redditPosts;
    private final JPanel basePanel;
    private JLabel spinner;


    private final JScrollBar verticalScrollBar;
    private static RedditPostPanel selectedPanel = null;

    public RedditClient() {
        super("Reddit Client");
        redditPosts = new ArrayList<>();
        basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));


        JPanel loadingPanel = createLoadingPanel();
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(basePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        verticalScrollBar = scrollPane.getVerticalScrollBar();

        add(scrollPane, BorderLayout.CENTER);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel topLabel = new JLabel("/r/aww");
        topLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(topLabel);

        add(topPanel, BorderLayout.NORTH);
        add(loadingPanel, BorderLayout.SOUTH);


        // Set the window properties
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 1000);
        setLocationRelativeTo(null);

        setVisible(true);
        addListeners();

        // Load the first posts
        loadPosts(3, null);
    }

    private JPanel createLoadingPanel() {
        try {
            spinner = new JLabel(new ImageIcon(new URL("https://media.tenor.com/wpSo-8CrXqUAAAAi/loading-loading-forever.gif")));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        spinner.setVisible(false);
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
        JPanel loadingPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        loadingPanel.add(spinner, gbc);
        return loadingPanel;
    }

    private void addListeners() {
        verticalScrollBar.addAdjustmentListener(e -> {
            boolean scrollbarAtBottom = verticalScrollBar.getValue() == verticalScrollBar.getMaximum() - verticalScrollBar.getVisibleAmount();
            if (scrollbarAtBottom && !spinner.isVisible()) {
                System.out.println("Hit bottom of list");
                int postsListSize = redditPosts.size();
                if (postsListSize > 0) {
                    RedditPost afterRedditPost = redditPosts.get(postsListSize - 1);
                    loadPosts(3, afterRedditPost);
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
        if (afterRedditPost != null) {
            urlParameters += "&after=" + afterRedditPost.name();
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
                redditPostsList = gsonRedditPost.fromJson(jsonArray, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (RedditPost post : redditPostsList) {
                System.out.println(post.title());
                redditPosts.add(post);
                basePanel.add(new RedditPostPanel(post));
            }
            spinner.setVisible(false);
        }).start();
    }


    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RedditClient().setVisible(true));
        ;
    }
}

