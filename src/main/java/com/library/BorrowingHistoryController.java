package com.library;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class BorrowingHistoryController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbStatusFilter;
    @FXML private TableView<BorrowRecord> tblHistory;
    @FXML private TableColumn<BorrowRecord, String> colMemberId;
    @FXML private TableColumn<BorrowRecord, String> colBookTitle;
    @FXML private TableColumn<BorrowRecord, LocalDate> colIssueDate;
    @FXML private TableColumn<BorrowRecord, LocalDate> colDueDate;
    @FXML private TableColumn<BorrowRecord, LocalDate> colReturnDate;
    @FXML private TableColumn<BorrowRecord, String> colStatus;
    @FXML private Label lblRecordCount;

    private final DataStore store = DataStore.getInstance();
    private FilteredList<BorrowRecord> filteredRecords;

    @FXML
    private void initialize() {
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(column -> new StatusCell());

        cmbStatusFilter.getItems().addAll("All", "Borrowed", "Returned", "Overdue");
        cmbStatusFilter.setValue("All");

        filteredRecords = new FilteredList<>(store.getRecords(), r -> true);
        SortedList<BorrowRecord> sortedRecords = new SortedList<>(filteredRecords);
        sortedRecords.comparatorProperty().bind(tblHistory.comparatorProperty());
        tblHistory.setItems(sortedRecords);
        tblHistory.setPlaceholder(new Label("No borrowing records found"));

        txtSearch.textProperty().addListener((obs, oldText, newText) -> applyFilters());
        cmbStatusFilter.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        applyFilters();
    }

    private void applyFilters() {
        String query = txtSearch.getText() == null ? "" : txtSearch.getText().trim().toLowerCase();
        String status = cmbStatusFilter.getValue();

        filteredRecords.setPredicate(r -> {
            boolean matchesText = query.isEmpty()
                    || r.getMemberId().toLowerCase().contains(query)
                    || r.getMemberName().toLowerCase().contains(query)
                    || r.getBookTitle().toLowerCase().contains(query);
            boolean matchesStatus = status == null || status.equals("All") || status.equals(r.getStatus());
            return matchesText && matchesStatus;
        });
        lblRecordCount.setText("Showing " + filteredRecords.size() + " of "
                + store.getRecords().size() + " record(s)");
    }
}
