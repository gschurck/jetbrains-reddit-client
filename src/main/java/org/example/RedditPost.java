package org.example;


public class RedditPost {
    private final String name;
    private final String title;
    private final String thumbnail;
    private final String url;
    private final String permalink;


    public RedditPost(String name, String title, String thumbnail, String url, String permalink) {
        this.name = name;
        this.title = title;
        this.thumbnail = thumbnail;
        this.url = url;
        this.permalink = permalink;
    }

    public String getName() {
        return name;
    }


    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public String getPermalink() {
        return permalink;
    }

    @Override
    public String toString() {
        return name + " | " + title;
    }


}
