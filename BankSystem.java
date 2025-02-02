public class BankSystem {
    public static void main(String[] args) {
        Bank bank = new Bank("Central Bank");

        Branch branch1 = new Branch("Downtown Branch", bank);
        Branch branch2 = new Branch("Uptown Branch", bank);

        bank.addBranch(branch1);
        bank.addBranch(branch2);

        Deposit deposit1 = new Deposit("Alice Smith", 1000, branch1);
        Deposit deposit2 = new Deposit("Bob Johnson", 1500, branch1);
        Deposit deposit3 = new Deposit("Charlie Brown", 2000, branch2);
        Deposit deposit4 = new Deposit("Diana Prince", 2500, branch2);

        branch1.addDeposit(deposit1);
        branch1.addDeposit(deposit2);
        branch2.addDeposit(deposit3);
        branch2.addDeposit(deposit4);

        System.out.println("Deposits in " + branch1.getName() + ":");
        for (Deposit d : branch1.getDeposits()) {
            System.out.println(d.getDepositorName() + " - " + d.getDepositAmount());
        }
        System.out.println("Total deposit amount: " + branch1.getTotalDepositAmount());

        deposit1.replenishAccount(500);
        System.out.println("After replenishment:");
        for (Deposit d : branch1.getDeposits()) {
            System.out.println(d.getDepositorName() + " - " + d.getDepositAmount());
        }
        System.out.println("Total deposit amount: " + branch1.getTotalDepositAmount());

        Deposit foundDeposit = bank.findDepositByName("Charlie Brown");
        if (foundDeposit != null) {
            System.out.println("Found deposit: " + foundDeposit.getDepositorName() + " - " + foundDeposit.getDepositAmount() + ", Branch: " + foundDeposit.getBranch().getName());
        } else {
            System.out.println("Deposit not found.");
        }
    }
}
