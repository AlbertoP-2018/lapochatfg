package com.tfdevelopment.pochaonlinetfd.server.object.news;

public class NewsServer {
    private String title;
    private String date;
    private String text;

    public NewsServer(String title, String date, String text){
        this.title = title;
        this.date = date;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
