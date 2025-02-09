package com.bankingsystem.models;

public class Bank {
    private int id;
    private String name;

    // Полный конструктор
    public Bank(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Конструктор без ID (для создания нового банка)
    public Bank(String name) {
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "🏦 " + id + ": " + name;
    }
}