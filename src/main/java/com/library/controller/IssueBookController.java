package com.library.controller;

import com.library.model.Book;
import com.library.model.Member;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.MemberService;
import com.library.util.AlertUtil;
import com.library.util.Validator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssueBookController {

    private static final int LOAN_PERIOD_DAYS = 14;

    @FXML private ComboBox<Member> cmbMember;
    @FXML private ComboBox<Book> cmbBook;
    @FXML private Label lblAvailability;
    @FXML private DatePicker dpIssueDate;
    @FXML private DatePicker dpDueDate;

    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final BorrowService borrowService = new BorrowService();

    @FXML
    private void initialize() {
        try {
            cmbMember.setItems(FXCollections.observableArrayList(memberService.getAllMembers()));
        } catch (SQLException e) {
            AlertUtil.showDatabaseError(e);
        }
        loadAvailableBooks();

        // Force picking dates from the calendar (avoids typed dates that never get committed)
        dpIssueDate.setEditable(false);
        dpDueDate.setEditable(false);

        // When the issue date changes, suggest a due date 14 days later
        dpIssueDate.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) dpDueDate.setValue(newDate.plusDays(LOAN_PERIOD_DAYS));
        });
        resetDates();

        cmbBook.valueProperty().addListener((obs, oldBook, book) ->
                lblAvailability.setText(book == null ? ""
                        : "Available copies: " + book.getAvailableQuantity() + " of " + book.getQuantity()));
    }

    private void loadAvailableBooks() {
        try {
            cmbBook.setItems(FXCollections.observableArrayList(bookService.getAvailableBooks()));
        } catch (SQLException e) {
            AlertUtil.showDatabaseError(e);
        }
    }

    private void resetDates() {
        dpIssueDate.setValue(LocalDate.now());
        dpDueDate.setValue(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));
    }

    @FXML
    private void handleIssueBook() {
        Member member = cmbMember.getValue();
        Book book = cmbBook.getValue();
        LocalDate issueDate = dpIssueDate.getValue();
        LocalDate dueDate = dpDueDate.getValue();

        Validator.clearInvalid(cmbMember, cmbBook, dpIssueDate, dpDueDate);
        List<String> errors = new ArrayList<>();
        if (member == null) {
            errors.add("Please select a member.");
            Validator.markInvalid(cmbMember);
        }
        if (book == null) {
            errors.add("Please select a book.");
            Validator.markInvalid(cmbBook);
        }
        if (issueDate == null) {
            errors.add("Please choose an issue date.");
            Validator.markInvalid(dpIssueDate);
        } else if (issueDate.isAfter(LocalDate.now())) {
            errors.add("Issue date cannot be in the future.");
            Validator.markInvalid(dpIssueDate);
        }
        if (dueDate == null) {
            errors.add("Please choose a due date.");
            Validator.markInvalid(dpDueDate);
        } else if (issueDate != null && !dueDate.isAfter(issueDate)) {
            errors.add("Due date must be after the issue date.");
            Validator.markInvalid(dpDueDate);
        }
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Cannot Issue Book", String.join("\n", errors));
            return;
        }

        try {
            if (borrowService.isBookBorrowedBy(member, book)) {
                AlertUtil.showWarning("Already Borrowed",
                        member.getFullName() + " already has a copy of \"" + book.getTitle() + "\".");
                return;
            }

            boolean confirmed = AlertUtil.showConfirmation("Confirm Issue",
                    "Issue \"" + book.getTitle() + "\" to " + member.getFullName() + "?\nDue date: " + dueDate);
            if (!confirmed) return;

            borrowService.issueBook(member, book, issueDate, dueDate);
            AlertUtil.showInfo("Book Issued",
                    "\"" + book.getTitle() + "\" has been issued to " + member.getFullName() + ".");
            handleClear();
        } catch (SQLException e) {
            AlertUtil.showDatabaseError(e);
        }
    }

    @FXML
    private void handleClear() {
        cmbMember.setValue(null);
        cmbBook.setValue(null);
        loadAvailableBooks();
        resetDates();
        lblAvailability.setText("");
        Validator.clearInvalid(cmbMember, cmbBook, dpIssueDate, dpDueDate);
    }
}
