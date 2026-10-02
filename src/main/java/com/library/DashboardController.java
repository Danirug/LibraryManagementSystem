package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML private VBox sidebar;
    @FXML private Label lblPageTitle;
    @FXML private StackPane contentArea;

    @FXML private Button btnHome;
    @FXML private Button btnAddBook;
    @FXML private Button btnAddMember;
    @FXML private Button btnManageMembers;
    @FXML private Button btnIssueBook;
    @FXML private Button btnReturnBook;
    @FXML private Button btnHistory;

    @FXML
    private void initialize() {
        showHome();   // open the overview when the dashboard first loads
    }

    @FXML private void showHome()          { loadPage("Home.fxml", "Dashboard", btnHome); }
    @FXML private void showAddBook()       { loadPage("AddBook.fxml", "Add Book", btnAddBook); }
    @FXML private void showAddMember()     { loadPage("AddMember.fxml", "Add Member", btnAddMember); }
    @FXML private void showManageMembers() { loadPage("ManageMembers.fxml", "Manage Members", btnManageMembers); }
    @FXML private void showIssueBook()     { loadPage("IssueBook.fxml", "Issue Book", btnIssueBook); }
    @FXML private void showReturnBook()    { loadPage("ReturnBook.fxml", "Return Book", btnReturnBook); }
    @FXML private void showHistory()       { loadPage("BorrowingHistory.fxml", "Borrowing History", btnHistory); }

    @FXML
    private void handleLogout() {
        if (AlertUtil.showConfirmation("Logout", "Are you sure you want to log out?")) {
            SceneManager.switchScene("Login.fxml", "Library Management System - Login");
        }
    }

    private void loadPage(String fxmlFile, String title, Button activeButton) {
        contentArea.getChildren().setAll(SceneManager.loadView(fxmlFile));
        lblPageTitle.setText(title);
        sidebar.getChildren().forEach(node -> node.getStyleClass().remove("active"));
        activeButton.getStyleClass().add("active");
    }
}
