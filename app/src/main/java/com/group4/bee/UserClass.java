package com.group4.bee;

public class UserClass {
    private String name;
    private String email;
    private String avatarURL;

    public UserClass(String name, String email, String avatarURL) {
        this.name = name;
        this.email = email;
        this.avatarURL = avatarURL;
    }

    public String getName () {
        return this.name;
    }

    public String getEmail () {
        return this.email;
    }

    public String getAvatarURL () {
        return this.avatarURL;
    }
}