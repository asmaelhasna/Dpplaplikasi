package com.siskema.gryffindor.model;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<User> users;
    private List<Activity> activities;

    public Database() {
        this.users = new ArrayList<>();
        this.activities = new ArrayList<>();
    }

    // Getters
    public List<User> getUsers() { return users; }
    public List<Activity> getActivities() { return activities; }
}