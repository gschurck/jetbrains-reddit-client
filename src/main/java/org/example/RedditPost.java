package org.example;


public record RedditPost(String name, String title, String thumbnail, String url, String permalink, int score) {

    @Override
    public String toString() {
        return name + " | " + title;
    }
}
