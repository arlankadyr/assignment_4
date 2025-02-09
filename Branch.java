package com.bankingsystem.models;

public class Branch {
    private int id;
    private String name;
    private int bankId;

    // Полный конструктор
    public Branch(int id, String name, int bankId) {
        this.id = id;
        this.name = name;
        this.bankId = bankId;
    }

    // Конструктор без ID (для создания нового филиала)
    public Branch(String name, int bankId) {
        this.name = name;
        this.bankId = bankId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getBankId() { return bankId; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBankId(int bankId) { this.bankId = bankId; }

    @Override
    public String toString() {
        return "🏢 " + id + ": " + name + " (Банк ID: " + bankId + ")";
    }
}