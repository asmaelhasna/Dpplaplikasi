package com.siskema.gryffindor.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.siskema.gryffindor.model.*;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataService {
    private static final String DB_FILE = "siskema_data.json";
    private Database database;
    private Gson gson;

    public DataService() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        loadData();
    }

    private void loadData() {
        try (Reader reader = new FileReader(DB_FILE)) {
            database = gson.fromJson(reader, Database.class);
            if (database == null) {
                initializeEmptyDatabase();
            }
        } catch (FileNotFoundException e) {
            initializeEmptyDatabase();
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
            initializeEmptyDatabase();
        }
    }

    private void initializeEmptyDatabase() {
        database = new Database();
        // Buat user default untuk testing
        database.getUsers().add(new User("mhs", "123", "Mahasiswa Satu", UserRole.MAHASISWA, null));
        database.getUsers().add(new User("ukm", "123", "UKM Mapala", UserRole.PENYELENGGARA, "Mapala"));
        database.getUsers().add(new User("pkm", "123", "Admin PKM", UserRole.PKM, null));
        
        // Buat data kegiatan default
        Activity act1 = new Activity("Webinar Teknologi", "Mapala", "20 Nov 2025", "Aula FT", "Deskripsi webinar...");
        act1.setStatus(ActivityStatus.APPROVED); // Anggap sudah di-approve
        database.getActivities().add(act1);
    }

    public synchronized void saveData() {
        try (Writer writer = new FileWriter(DB_FILE)) {
            gson.toJson(database, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User authenticateUser(String username, String password) {
        for (User user : database.getUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // --- METHOD BARU DITAMBAHKAN ---
    public User getUserByUsername(String username) {
        return database.getUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    // --- AKHIR METHOD BARU ---

    public void addUser(User user) {
        database.getUsers().add(user);
        saveData();
    }
    
    public void addActivity(Activity activity) {
        database.getActivities().add(activity);
        saveData();
    }
    
    public void updateActivity(Activity activity) {
        Optional<Activity> existing = database.getActivities().stream()
                .filter(a -> a.getId().equals(activity.getId()))
                .findFirst();
        
        if (existing.isPresent()) {
            int index = database.getActivities().indexOf(existing.get());
            database.getActivities().set(index, activity);
            saveData();
        }
    }

    // --- Methods untuk mengambil data ---

    public List<Activity> getAllActivities() {
        return database.getActivities();
    }

    public List<Activity> getApprovedActivities() {
        return database.getActivities().stream()
                .filter(a -> a.getStatus() == ActivityStatus.APPROVED)
                .collect(Collectors.toList());
    }
    
    public List<Activity> getPendingActivities() {
        return database.getActivities().stream()
                .filter(a -> a.getStatus() == ActivityStatus.PENDING_APPROVAL)
                .collect(Collectors.toList());
    }
    
    public List<Activity> getActivitiesByOrganizer(String organizerName) {
        return database.getActivities().stream()
                .filter(a -> a.getOrganizerName().equals(organizerName))
                .collect(Collectors.toList());
    }
}