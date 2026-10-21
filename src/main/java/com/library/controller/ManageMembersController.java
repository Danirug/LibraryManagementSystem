package com.library.controller;

import com.library.model.Member;
import com.library.service.MemberService;
import com.library.util.AlertUtil;
import com.library.util.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class ManageMembersController {

    @FXML private TextField txtSearch;
    @FXML private TableView<Member> tblMembers;
    @FXML private TableColumn<Member, String> colMemberId;
    @FXML private TableColumn<Member, String> colFullName;
    @FXML private TableColumn<Member, String> colEmail;
    @FXML private TableColumn<Member, String> colPhone;
    @FXML private TableColumn<Member, String> colAddress;

    @FXML private TextField txtMemberId;
    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtAddress;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    private final MemberService memberService = new MemberService();
    private final ObservableList<Member> members = FXCollections.observableArrayList();
    private FilteredList<Member> filteredMembers;

    @FXML
    private void initialize() {
        // The text in quotes must match the getter name: "memberId" -> getMemberId()
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        loadMembers();
        filteredMembers = new FilteredList<>(members, m -> true);
        SortedList<Member> sortedMembers = new SortedList<>(filteredMembers);
        sortedMembers.comparatorProperty().bind(tblMembers.comparatorProperty());
        tblMembers.setItems(sortedMembers);
        tblMembers.setPlaceholder(new Label("No members found"));

        txtSearch.textProperty().addListener((obs, oldText, newText) -> applySearch(newText));

        tblMembers.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldMember, newMember) -> fillForm(newMember));

        // Edit and delete only make sense when a row is selected
        btnUpdate.disableProperty().bind(tblMembers.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(tblMembers.getSelectionModel().selectedItemProperty().isNull());

        txtMemberId.setEditable(false);   // the ID identifies the member, so it can't be changed
        Validator.digitsOnly(txtPhone, 10);
    }

    private void loadMembers() {
        try {
            members.setAll(memberService.getAllMembers());
        } catch (SQLException e) {
            AlertUtil.showDatabaseError(e);
        }
    }

    private void applySearch(String text) {
        String query = text == null ? "" : text.trim().toLowerCase();
        filteredMembers.setPredicate(m -> query.isEmpty()
                || m.getMemberId().toLowerCase().contains(query)
                || m.getFullName().toLowerCase().contains(query)
                || m.getEmail().toLowerCase().contains(query)
                || m.getPhone().contains(query));
    }

    private void fillForm(Member member) {
        Validator.clearInvalid(txtFullName, txtEmail, txtPhone, txtAddress);
        if (member == null) {
            txtMemberId.clear();
            txtFullName.clear();
            txtEmail.clear();
            txtPhone.clear();
            txtAddress.clear();
            return;
        }
        txtMemberId.setText(member.getMemberId());
        txtFullName.setText(member.getFullName());
        txtEmail.setText(member.getEmail());
        txtPhone.setText(member.getPhone());
        txtAddress.setText(member.getAddress());
    }

    @FXML
    private void handleUpdate() {
        Member selected = tblMembers.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Validator.clearInvalid(txtFullName, txtEmail, txtPhone, txtAddress);
        List<String> errors = Validator.validateMemberFields(txtFullName, txtEmail, txtPhone, txtAddress);
        if (!errors.isEmpty()) {
            AlertUtil.showWarning("Invalid Member Details", String.join("\n", errors));
            return;
        }
        if (!AlertUtil.showConfirmation("Save Changes",
                "Update details for member " + selected.getMemberId() + "?")) {
            return;
        }

        Member updated = new Member(selected.getMemberId(), txtFullName.getText().trim(),
                txtEmail.getText().trim(), txtPhone.getText().trim(), txtAddress.getText().trim());
        try {
            memberService.updateMember(updated);
        } catch (SQLException e) {
            AlertUtil.showDatabaseError(e);
            return;
        }
        selected.setFullName(updated.getFullName());
        selected.setEmail(updated.getEmail());
        selected.setPhone(updated.getPhone());
        selected.setAddress(updated.getAddress());
        tblMembers.refresh();
        AlertUtil.showInfo("Member Updated", "Member details were updated successfully.");
    }

    @FXML
    private void handleDelete() {
        Member selected = tblMembers.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            if (memberService.hasActiveLoans(selected.getMemberId())) {
                AlertUtil.showError("Cannot Delete Member", selected.getFullName()
                        + " still has borrowed books.\nPlease return them before deleting this member.");
                return;
            }
            if (AlertUtil.showConfirmation("Delete Member", "Are you sure you want to delete "
                    + selected.getFullName() + " (" + selected.getMemberId() + ")?\nThis cannot be undone.")) {
                memberService.deleteMember(selected.getMemberId());
                members.remove(selected);
                AlertUtil.showInfo("Member Deleted", "The member was deleted.");
            }
        } catch (SQLException e) {
            AlertUtil.showDatabaseError(e);
        }
    }

    @FXML
    private void handleClearSelection() {
        tblMembers.getSelectionModel().clearSelection();
    }
}
