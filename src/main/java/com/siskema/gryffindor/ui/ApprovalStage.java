package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.Activity;
import com.siskema.gryffindor.model.ActivityStatus;
import com.siskema.gryffindor.model.User;
import com.siskema.gryffindor.service.DataService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class ApprovalStage extends VBox {

    private DataService dataService;
    private VBox listPanel;
    private User currentUser;

    public ApprovalStage(User user) {
        this.currentUser = user;
        this.dataService = new DataService();
        
        setPadding(new Insets(20));
        setSpacing(20);

        Label title = new Label("Persetujuan Kegiatan");
        title.setFont(UIConstants.FONT_TITLE);

        listPanel = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(listPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setBorder(Border.EMPTY);

        getChildren().addAll(title, scrollPane);
        loadPendingActivities();
    }

    private void loadPendingActivities() {
        listPanel.getChildren().clear();
        List<Activity> pending = dataService.getPendingActivities();

        if (pending.isEmpty()) {
            listPanel.getChildren().add(new Label("Tidak ada kegiatan yang menunggu persetujuan."));
        } else {
            for (Activity activity : pending) {
                listPanel.getChildren().add(createApprovalCard(activity));
            }
        }
    }

    private HBox createApprovalCard(Activity activity) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E6E6E6; -fx-border-radius: 8;");
        card.setAlignment(Pos.CENTER_LEFT);

        VBox info = new VBox(5);
        
        Label title = new Label(activity.getName());
        title.setFont(UIConstants.FONT_SUBTITLE);
        // --- PERBAIKAN WARNA TEKS ---
        title.setTextFill(Color.web(UIConstants.HEX_TEXT_DARK)); 
        
        Label detail = new Label("Oleh: " + activity.getOrganizerName() + " | Tgl: " + activity.getDate());
        detail.setFont(UIConstants.FONT_SMALL);
        detail.setTextFill(Color.web(UIConstants.HEX_TEXT_LIGHT));
        
        info.getChildren().addAll(title, detail);
        HBox.setHgrow(info, Priority.ALWAYS);

        HBox btnBox = new HBox(10);
        Button rejectBtn = new Button("Tolak");
        rejectBtn.setStyle("-fx-background-color: #9A242C; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5;");
        rejectBtn.setOnAction(e -> {
            activity.setStatus(ActivityStatus.REJECTED);
            dataService.updateActivity(activity);
            loadPendingActivities();
        });

        Button approveBtn = new Button("Setujui");
        approveBtn.setStyle("-fx-background-color: #009600; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5;");
        approveBtn.setOnAction(e -> {
            activity.setStatus(ActivityStatus.APPROVED);
            dataService.updateActivity(activity);
            loadPendingActivities();
        });

        btnBox.getChildren().addAll(rejectBtn, approveBtn);
        card.getChildren().addAll(info, btnBox);
        return card;
    }
}