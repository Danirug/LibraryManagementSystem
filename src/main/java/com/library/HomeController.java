package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {

    @FXML private Label lblTotalBooks;
    @FXML private Label lblTotalMembers;
    @FXML private Label lblBorrowed;
    @FXML private Label lblOverdue;

    @FXML
    private void initialize() {
        DataStore store = DataStore.getInstance();
        lblTotalBooks.setText(String.valueOf(store.getBooks().size()));
        lblTotalMembers.setText(String.valueOf(store.getMembers().size()));
        lblBorrowed.setText(String.valueOf(store.countCurrentlyBorrowed()));
        lblOverdue.setText(String.valueOf(store.countOverdue()));
    }
}
