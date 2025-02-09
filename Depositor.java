package com.bankingsystem.models;

public class Depositor {
    private int id;
    private String fullName;
    private String contact;

    // Полный конструктор
    public Depositor(int id, String fullName, String contact) {
        this.id = id;
        this.fullName = fullName;
        this.contact = contact;
    }

    // Конструктор без ID (для создания нового вкладчика)
    public Depositor(String fullName, String contact) {
        this.fullName = fullName;
        this.contact = contact;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getContact() { return contact; }

    public void setId(int id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return "👤 " + id + ": " + fullName + " (📞 " + contact + ")";
    }
}