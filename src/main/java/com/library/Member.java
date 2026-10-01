package com.library;

public class Member {
    private final String memberId;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    public Member(String memberId, String fullName, String email, String phone, String address) {
        this.memberId = memberId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getMemberId() { return memberId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return memberId + " - " + fullName;
    }
}