package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class AddBookController {

    @FXML private TextField txtBookId;
    @FXML private TextField txtTitle;
    @FXML private TextField txtAuthor;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private TextField txtPublishedYear;
    @FXML private TextField txtQuantity;

    private final DataStore store = DataStore.getInstance();

    @FXML
    private void initialize() {
        cmbCategory.getItems().addAll("Fiction", "Non-Fiction", "Science", "Technology",
                "History", "Biography", "Children", "Reference");
        Validator.digitsOnly(txtPublishedYear, 4);
        Validator.digitsOnly(txtQuantity, 3);
    }

    @FXML
    private void handleAddBook() {
        Validator.clearInvalid(txtBookId, txtTitle, txtAuthor, cmbCategory, txtPublishedYear, txtQuantity);
        List<String> errors = new ArrayList<>();

        String bookId = txtBookId.getText().trim();
        if (bookId.isEmpty()) {
            errors.add("Book ID / ISBN is required.");
            Validator.markInvalid(txtBookId);
        } else if (store.findBookById(bookId) != null) {
            errors.add("A book with ID \"" + bookId + "\" already exists.");
            Validator.markInvalid(txtBookId);
        }
        if (Validator.isBlank(txtTitle.getText())) {
            errors.add("Book title is required.");
            Validator.markInvalid(txtTitle);
        }
        if (Validator.isBlank(txtAuthor.getText())) {
            errors.add("Author is required.");
            Validator.markInvalid(txtAuthor);
        }
        if (cmbCategory.getValue() == null) {
            errors.add("Please select a category.");
            Validator.markInvalid(cmbCategory);
        }
        int currentYear = Year.now().getValue();
        Integer year = Validator.parseInt(txtPublishedYear.getText());
        if (year == null || year < 1450 || year > currentYear) {
            errors.add("Published year must be between 1450 and " + currentYear + ".");
            Validator.markInvalid(txtPublishedYear);
        }
        Integer quantity = Validator.parseInt(txtQuantity.getText());
        if (quantity == null || quantity < 1) {
            errors.add("Quantity must be at least 1.");
            Validator.markInvalid(txtQuantity);
        }

        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Please Fix the Following", String.join("\n", errors));
            return;
        }

        Book book = new Book(bookId, txtTitle.getText().trim(), txtAuthor.getText().trim(),
                cmbCategory.getValue(), year, quantity);
        store.getBooks().add(book);
        AlertUtil.showInfo("Book Added", "\"" + book.getTitle() + "\" was added successfully.");
        handleClear();
    }

    @FXML
    private void handleClear() {
        txtBookId.clear();
        txtTitle.clear();
        txtAuthor.clear();
        cmbCategory.setValue(null);
        txtPublishedYear.clear();
        txtQuantity.clear();
        Validator.clearInvalid(txtBookId, txtTitle, txtAuthor, cmbCategory, txtPublishedYear, txtQuantity);
        txtBookId.requestFocus();
    }
}
