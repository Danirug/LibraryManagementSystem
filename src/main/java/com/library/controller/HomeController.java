package com.library.controller;

import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.MemberService;
import com.library.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class HomeController {

    @FXML private Label lblTotalBooks;
    @FXML private Label lblTotalMembers;
    @FXML private Label lblBorrowed;
    @FXML private Label lblOverdue;

    @FXML
    private void initialize() {
        BorrowService borrowService = new BorrowService();
        try {
            lblTotalBooks.setText(String.valueOf(new BookService().getAllBooks().size()));
            lblTotalMembers.setText(String.valueOf(new MemberService().getAllMembers().size()));
            lblBorrowed.setText(String.valueOf(borrowService.countCurrentlyBorrowed()));
            lblOverdue.setText(String.valueOf(borrowService.countOverdue()));
        } catch (SQLException e) {
            AlertUtil.showDatabaseError(e);
        }
    }
}
