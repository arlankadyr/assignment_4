class Deposit {
    private String depositorName;
    private double depositAmount;
    private Branch branch;

    public Deposit(String depositorName, double depositAmount, Branch branch) {
        this.depositorName = depositorName;
        this.depositAmount = depositAmount;
        this.branch = branch;
    }

    public String getDepositorName() {
        return depositorName;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public Branch getBranch() {
        return branch;
    }

    public void replenishAccount(double amount) {
        if (amount > 0) {
            this.depositAmount += amount;
        }
    }
}
