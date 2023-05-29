package com.tuk.lightninggathering;

public class Post {
    private String title;
    private String date;
    private String location;
    private String participants;

    public Post(String title, String date, String location, String participants) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.participants = participants;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getParticipants() {
        return participants;
    }
}

