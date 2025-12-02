package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.User;
import com.siskema.gryffindor.model.UserRole;
import com.siskema.gryffindor.service.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BaseStage extends Stage {

    private static BaseStage instance;
    protected User currentUser;
    protected BorderPane rootLayout;

    public static BaseStage getInstance() {
        return instance;
    }

    public BaseStage(User user) {
        instance = this;
        this.currentUser = user;
        setTitle("Siskema Gryffindor");

        setMaximized(true);

        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: " + UIConstants.HEX_BACKGROUND + ";");

        rootLayout.setTop(createTopBar());
        rootLayout.setRight(createRightMenu());
        rootLayout.setCenter(new StackPane(new Label("Loading...")));

        Scene scene = new Scene(rootLayout, 1200, 720);
        setScene(scene);
    }

    public void setView(Node viewContent) {
        rootLayout.setCenter(viewContent);
    }

    private HBox createTopBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(10, 20, 10, 20));
        top.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");
        top.setAlignment(Pos.CENTER_LEFT);
        top.setSpacing(10);

        Label logo = new Label("🛡️");
        logo.setFont(Font.font(24));
        
        Label appTitle = new Label("Siskema Gryffindor");
        appTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label profileName = new Label(currentUser.getFullName() + " 👤");
        profileName.setFont(Font.font("SansSerif", 16));

        top.getChildren().addAll(logo, appTitle, spacer, profileName);
        return top;
    }

    private VBox createRightMenu() {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(20));
        menu.setPrefWidth(220);
        menu.setStyle("-fx-background-color: " + UIConstants.HEX_PRIMARY + ";");

        menu.getChildren().add(createMenuItem("🏠", "Beranda", () -> {
            setView(new DashboardStage(currentUser));
        }));

        if (currentUser.getRole() == UserRole.MAHASISWA) {
            menu.getChildren().add(createMenuItem("🏫", "Cari UKM", () -> {
                setView(new UKMListStage(currentUser));
            }));
            menu.getChildren().add(createMenuItem("📅", "Kegiatan", () -> {
                setView(new ActivityListStage(currentUser));
            }));
        }

        if (currentUser.getRole() == UserRole.PENYELENGGARA) {
            menu.getChildren().add(createMenuItem("🛠️", "Kelola Kegiatan", () -> {
                setView(new ManageActivityStage(currentUser));
            }));
        }

        if (currentUser.getRole() == UserRole.PKM) {
            menu.getChildren().add(createMenuItem("✅", "Persetujuan", () -> {
                setView(new ApprovalStage(currentUser));
            }));
            menu.getChildren().add(createMenuItem("👥", "Kelola Akun", () -> {
                setView(new ManageAccountStage(currentUser));
            }));
            menu.getChildren().add(createMenuItem("📊", "Cetak Laporan", () -> {
                setView(new ReportStage(currentUser));
            }));
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        menu.getChildren().add(spacer);

        menu.getChildren().add(createMenuItem("🚪", "Keluar", () -> {
            SessionManager.getInstance().logout();
            new LoginStage().show();
            this.close();
        }));

        return menu;
    }

    private HBox createMenuItem(String icon, String text, Runnable action) {
        HBox item = new HBox(10);
        item.setPadding(new Insets(10));
        item.setAlignment(Pos.CENTER_LEFT);
        item.setCursor(javafx.scene.Cursor.HAND);

        Label iconLbl = new Label(icon);
        iconLbl.setTextFill(Color.WHITE);
        iconLbl.setFont(Font.font(18));

        Label textLbl = new Label(text);
        textLbl.setTextFill(Color.WHITE);
        textLbl.setFont(UIConstants.FONT_NORMAL);

        item.getChildren().addAll(iconLbl, textLbl);

        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: " + UIConstants.HEX_PRIMARY_DARK + ";"));
        item.setOnMouseExited(e -> item.setStyle("-fx-background-color: transparent;"));

        item.setOnMouseClicked(e -> action.run());

        return item;
    }
}