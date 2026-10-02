package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class AddMemberController {

    @FXML private TextField txtMemberId;
    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextArea txtAddress;

    private final DataStore store = DataStore.getInstance();

    @FXML
    private void initialize() {
        Validator.digitsOnly(txtPhone, 10);
    }

    @FXML
    private void handleRegister() {
        Validator.clearInvalid(txtMemberId, txtFullName, txtEmail, txtPhone, txtAddress);
        List<String> errors = new ArrayList<>();

        String memberId = txtMemberId.getText().trim();
        if (memberId.isEmpty()) {
            errors.add("Member ID is required.");
            Validator.markInvalid(txtMemberId);
        } else if (store.findMemberById(memberId) != null) {
            errors.add("Member ID \"" + memberId + "\" is already registered.");
            Validator.markInvalid(txtMemberId);
        }
        errors.addAll(Validator.validateMemberFields(txtFullName, txtEmail, txtPhone, txtAddress));

        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Please Fix the Following", String.join("\n", errors));
            return;
        }

        Member member = new Member(memberId, txtFullName.getText().trim(), txtEmail.getText().trim(),
                txtPhone.getText().trim(), txtAddress.getText().trim());
        store.getMembers().add(member);
        AlertUtil.showInfo("Member Registered", member.getFullName() + " was registered successfully.");
        handleClear();
    }

    @FXML
    private void handleClear() {
        txtMemberId.clear();
        txtFullName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        Validator.clearInvalid(txtMemberId, txtFullName, txtEmail, txtPhone, txtAddress);
        txtMemberId.requestFocus();
    }
}
