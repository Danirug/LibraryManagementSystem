package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnBookController {

    @FXML private TextField txtSearch;
    @FXML private TableView<BorrowRecord> tblBorrowed;
    @FXML private TableColumn<BorrowRecord, String> colMemberId;
    @FXML private TableColumn<BorrowRecord, String> colMemberName;
    @FXML private TableColumn<BorrowRecord, String> colBookTitle;
    @FXML private TableColumn<BorrowRecord, LocalDate> colIssueDate;
    @FXML private TableColumn<BorrowRecord, LocalDate> colDueDate;
    @FXML private TableColumn<BorrowRecord, String> colStatus;

    @FXML private Label lblMemberName;
    @FXML private Label lblMemberContact;
    @FXML private Label lblBookTitle;
    @FXML private Label lblIssueDate;
    @FXML private Label lblDueDate;
    @FXML private Label lblStatus;
    @FXML private DatePicker dpReturnDate;
    @FXML private Button btnReturn;

    private final DataStore store = DataStore.getInstance();
    private final ObservableList<BorrowRecord> activeRecords = FXCollections.observableArrayList();
    private FilteredList<BorrowRecord> filteredRecords;

    @FXML
    private void initialize() {
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(column -> new StatusCell());

        filteredRecords = new FilteredList<>(activeRecords, r -> true);
        tblBorrowed.setItems(filteredRecords);
        tblBorrowed.setPlaceholder(new Label("No books are currently borrowed"));
        refreshTable();

        txtSearch.textProperty().addListener((obs, oldText, newText) -> {
            String query = newText == null ? "" : newText.trim().toLowerCase();
            filteredRecords.setPredicate(r -> query.isEmpty()
                    || r.getMemberId().toLowerCase().contains(query)
                    || r.getMemberName().toLowerCase().contains(query)
                    || r.getBookTitle().toLowerCase().contains(query));
        });

        dpReturnDate.setValue(LocalDate.now());
        dpReturnDate.setEditable(false);
        dpReturnDate.valueProperty().addListener((obs, oldDate, newDate) -> updateStatus());

        tblBorrowed.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldRecord, newRecord) -> showDetails(newRecord));
        btnReturn.disableProperty().bind(tblBorrowed.getSelectionModel().selectedItemProperty().isNull());
        showDetails(null);
    }

    private void refreshTable() {
        activeRecords.setAll(store.getActiveRecords());
    }

    private void showDetails(BorrowRecord record) {
        if (record == null) {
            lblMemberName.setText("-");
            lblMemberContact.setText("-");
            lblBookTitle.setText("-");
            lblIssueDate.setText("-");
            lblDueDate.setText("-");
            lblStatus.getStyleClass().removeAll("status-overdue", "status-returned");
            lblStatus.setText("Select a borrowed book from the table");
            return;
        }
        lblMemberName.setText(record.getMemberName() + " (" + record.getMemberId() + ")");
        lblMemberContact.setText(record.getMember().getPhone() + "  |  " + record.getMember().getEmail());
        lblBookTitle.setText(record.getBookTitle());
        lblIssueDate.setText(record.getIssueDate().toString());
        lblDueDate.setText(record.getDueDate().toString());
        updateStatus();
    }

    /** Shows whether the book is overdue based on the chosen return date. */
    private void updateStatus() {
        BorrowRecord record = tblBorrowed.getSelectionModel().getSelectedItem();
        lblStatus.getStyleClass().removeAll("status-overdue", "status-returned");
        if (record == null) return;

        LocalDate returnDate = dpReturnDate.getValue() != null ? dpReturnDate.getValue() : LocalDate.now();
        long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
        if (daysLate > 0) {
            lblStatus.setText("OVERDUE by " + daysLate + " day(s)");
            lblStatus.getStyleClass().add("status-overdue");
        } else {
            lblStatus.setText("On time");
            lblStatus.getStyleClass().add("status-returned");
        }
    }

    @FXML
    private void handleReturnBook() {
        BorrowRecord record = tblBorrowed.getSelectionModel().getSelectedItem();
        LocalDate returnDate = dpReturnDate.getValue();
        if (record == null) return;

        if (returnDate == null) {
            AlertUtil.showWarning("Missing Date", "Please choose a return date.");
            return;
        }
        if (returnDate.isBefore(record.getIssueDate())) {
            AlertUtil.showError("Invalid Date",
                    "Return date cannot be before the borrowed date (" + record.getIssueDate() + ").");
            return;
        }
        if (returnDate.isAfter(LocalDate.now())) {
            AlertUtil.showError("Invalid Date", "Return date cannot be in the future.");
            return;
        }

        long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
        String message = "Return \"" + record.getBookTitle() + "\" from " + record.getMemberName() + "?";
        if (daysLate > 0) {
            message += "\n\nThis book is " + daysLate + " day(s) overdue.";
        }
        if (!AlertUtil.showConfirmation("Confirm Return", message)) return;

        store.returnBook(record, returnDate);
        AlertUtil.showInfo("Book Returned", "The book was returned successfully.");
        txtSearch.clear();
        refreshTable();
        dpReturnDate.setValue(LocalDate.now());
    }
}
