package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.User;
import com.siskema.gryffindor.model.UserRole;
import com.siskema.gryffindor.service.DataService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class UKMListStage extends VBox {

    private DataService dataService;
    private VBox listPanel;
    private User currentUser;
    private List<User> allUKM;

    public UKMListStage(User user) {
        this.currentUser = user;
        this.dataService = new DataService();
        this.allUKM = dataService.getAllUKM();
        
        setPadding(new Insets(20));
        setSpacing(20);

        Label title = new Label("Cari UKM / Organisasi");
        title.setFont(UIConstants.FONT_TITLE);

        TextField searchField = new TextField();
        searchField.setPromptText("Cari UKM (misal: Mapala, BEM)...");
        StyleUtil.styleRoundedTextField(searchField);
        searchField.setMaxWidth(400);
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterList(newVal));

        listPanel = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(listPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setBorder(Border.EMPTY);

        getChildren().addAll(title, searchField, scrollPane);
        renderList(allUKM);
    }

    private void filterList(String query) {
        if (query == null || query.isEmpty()) {
            renderList(allUKM);
            return;
        }
        String q = query.toLowerCase();
        List<User> filtered = allUKM.stream()
                .filter(u -> (u.getOrganizationName() != null && u.getOrganizationName().toLowerCase().contains(q)) || 
                             u.getFullName().toLowerCase().contains(q))
                .collect(Collectors.toList());
        renderList(filtered);
    }

    private void renderList(List<User> ukms) {
        listPanel.getChildren().clear();
        if (ukms.isEmpty()) {
            listPanel.getChildren().add(new Label("Tidak ditemukan UKM."));
        } else {
            for (User ukm : ukms) {
                listPanel.getChildren().add(createUKMCard(ukm));
            }
        }
    }

    private HBox createUKMCard(User ukm) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E6E6E6; -fx-border-radius: 8;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("🏫");
        icon.setFont(UIConstants.FONT_TITLE);

        VBox info = new VBox(5);
        String orgName = ukm.getOrganizationName() != null ? ukm.getOrganizationName() : "Tanpa Nama Organisasi";
        Label name = new Label(orgName);
        name.setFont(UIConstants.FONT_SUBTITLE);
        name.setTextFill(Color.web(UIConstants.HEX_TEXT_DARK));
        
        Label pic = new Label("PJ: " + ukm.getFullName());
        pic.setFont(UIConstants.FONT_SMALL);
        pic.setTextFill(Color.web(UIConstants.HEX_TEXT_LIGHT));
        
        info.getChildren().addAll(name, pic);
        HBox.setHgrow(info, Priority.ALWAYS);

        Button detailBtn = new Button("Lihat Kegiatan");
        StyleUtil.styleSecondaryButton(detailBtn);
        detailBtn.setOnAction(e -> {
            // Buka Detail UKM di Window baru
            new UKMDetailStage(ukm).show();
        });

        card.getChildren().addAll(icon, info, detailBtn);
        return card;
    }
}