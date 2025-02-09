package com.bankingsystem.models;

public class Deposit {
    private int id;
    private double amount;
    private int branchId;
    private int depositorId;

    // Полный конструктор
    public Deposit(int id, double amount, int branchId, int depositorId) {
        this.id = id;
        this.amount = amount;
        this.branchId = branchId;
        this.depositorId = depositorId;
    }

    // Конструктор без ID (для добавления в базу)
    public Deposit(double amount, int branchId, int depositorId) {
        this.amount = amount;
        this.branchId = branchId;
        this.depositorId = depositorId;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public int getBranchId() { return branchId; }
    public int getDepositorId() { return depositorId; }

    public void setId(int id) { this.id = id; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setBranchId(int branchId) { this.branchId = branchId; }
    public void setDepositorId(int depositorId) { this.depositorId = depositorId; }

    @Override
    public String toString() {
        return "Deposit ID: " + id + ", Amount: " + amount +
                ", Branch ID: " + branchId + ", Depositor ID: " + depositorId;
    }
}