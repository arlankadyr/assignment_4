import java.util.ArrayList;
import java.util.List;

class Bank {
    private String name;
    private List<Branch> branches = new ArrayList<>();

    public Bank(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void addBranch(Branch branch) {
        branches.add(branch);
    }

    public Deposit findDepositByName(String depositorName) {
        for (Branch branch : branches) {
            for (Deposit deposit : branch.getDeposits()) {
                if (deposit.getDepositorName().equals(depositorName)) {
                    return deposit;
                }
            }
        }
        return null;
    }
}
