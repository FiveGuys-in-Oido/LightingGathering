package com.tuk.lightninggathering;

import java.util.List;

public class Post {
    private String title;
    private String category;
    private String date;
    private String location;
    private String createMemberKey;
    private Double latitude;
    private Double longitude;
    private String description;
    private Integer maxMemberCount;
    private List<String> memberKeys;

    public Post(String title, String category, String date, String location, String createMemberKey, Double latitude, Double longitude, String description, Integer maxMemberCount, List<String> memberKeys) {
        this.title = title;
        this.category = category;
        this.date = date;
        this.location = location;
        this.createMemberKey = createMemberKey;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.maxMemberCount = maxMemberCount;
        this.memberKeys = memberKeys;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getCreateMemberKey() {
        return createMemberKey;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMaxMemberCount() {
        return maxMemberCount;
    }

    public List<String> getMemberKeys() {
        return memberKeys;
    }
}

