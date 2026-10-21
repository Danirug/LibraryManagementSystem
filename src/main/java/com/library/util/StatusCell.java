package com.library.util;

import com.library.model.BorrowRecord;
import javafx.scene.control.TableCell;

/** Table cell that colours Borrowed / Returned / Overdue. */
public class StatusCell extends TableCell<BorrowRecord, String> {
    @Override
    protected void updateItem(String status, boolean empty) {
        super.updateItem(status, empty);
        getStyleClass().removeAll("status-borrowed", "status-returned", "status-overdue");
        if (empty || status == null) {
            setText(null);
        } else {
            setText(status);
            getStyleClass().add("status-" + status.toLowerCase());
        }
    }
}
