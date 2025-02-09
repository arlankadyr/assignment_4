package com.bankingsystem.gui;

import com.bankingsystem.models.*;
import com.bankingsystem.services.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BankingSystemGUI extends JFrame {
    private final BankService bankService;
    private final BranchService branchService;
    private final DepositorService depositorService;
    private final DepositService depositService;

    private final DefaultListModel<String> bankListModel = new DefaultListModel<>();
    private final DefaultListModel<String> branchListModel = new DefaultListModel<>();
    private final DefaultListModel<String> depositorListModel = new DefaultListModel<>();
    private final DefaultListModel<String> depositListModel = new DefaultListModel<>();

    private final JList<String> bankList = createStyledList(bankListModel);
    private final JList<String> branchList = createStyledList(branchListModel);
    private final JList<String> depositorList = createStyledList(depositorListModel);
    private final JList<String> depositList = createStyledList(depositListModel);

    public BankingSystemGUI() {
        bankService = new BankService();
        branchService = new BranchService();
        depositorService = new DepositorService();
        depositService = new DepositService();

        setTitle("Банковская система");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 2));
        setLocationRelativeTo(null);
        applyLookAndFeel();

        loadBanks();
        loadBranches();
        loadDepositors();
        loadDeposits();


        add(createPanel("🏦 Банки", bankList, this::addBank, this::deleteBank, this::editBank));
        add(createPanel("🏢 Филиалы", branchList, this::addBranch, this::deleteBranch, this::editBranch));  // Added editBranch
        add(createPanel("👤 Вкладчики", depositorList, this::addDepositor, this::deleteDepositor, this::editDepositor));
        add(createPanel("💰 Депозиты", depositList, this::addDeposit, this::deleteDeposit, this::editDeposit));

        // Add menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu reportsMenu = new JMenu("Отчеты");
        JMenuItem totalDepositsItem = new JMenuItem("Общая сумма депозитов");
        totalDepositsItem.addActionListener(e -> showTotalDeposits());
        reportsMenu.add(totalDepositsItem);

        JMenuItem findDepositItem = new JMenuItem("Найти депозит по вкладчику");
        findDepositItem.addActionListener(e -> findDepositByDepositor());
        reportsMenu.add(findDepositItem);

        menuBar.add(reportsMenu);
        setJMenuBar(menuBar);

    }


    private void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }
    }

    private JPanel createPanel(String title, JList<String> list,
                               Runnable addAction, Runnable deleteAction, Runnable editAction) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(new Color(17, 0, 255));

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonPanel.setBackground(new Color(225, 0, 31));

        JButton addButton = new JButton("➕ Добавить");
        addButton.addActionListener(e -> addAction.run());
        buttonPanel.add(addButton);

        if (deleteAction != null) {
            JButton deleteButton = new JButton("❌ Удалить");
            deleteButton.addActionListener(e -> deleteAction.run());
            buttonPanel.add(deleteButton);
        }

        if (editAction != null) {
            JButton editButton = new JButton("✏️ Редактировать");
            editButton.addActionListener(e -> editAction.run());
            buttonPanel.add(editButton);
        }

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JList<String> createStyledList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 14));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }

    private void loadBanks() {
        bankListModel.clear();
        for (Bank bank : bankService.getAllBanks()) {
            bankListModel.addElement("🏦 " + bank.getId() + ": " + bank.getName());
        }
    }

    private void loadBranches() {
        branchListModel.clear();
        for (Branch branch : branchService.getAllBranches()) {
            branchListModel.addElement("🏢 " + branch.getId() + ": " + branch.getName() + " (Банк ID: " + branch.getBankId() + ")");
        }
    }

    private void loadDepositors() {
        depositorListModel.clear();
        for (Depositor depositor : depositorService.getAllDepositors()) {
            depositorListModel.addElement("👤 " + depositor.getId() + ": " + depositor.getFullName() + " (Контакт: " + depositor.getContact() + ")");
        }
    }

    private void loadDeposits() {
        depositListModel.clear();
        for (Deposit deposit : depositService.getAllDeposits()) {
            depositListModel.addElement("💰 " + deposit.getId() + ": Сумма " + deposit.getAmount() +
                    " (Филиал ID: " + deposit.getBranchId() + ", Вкладчик ID: " + deposit.getDepositorId() + ")");
        }
    }

    // Методы для добавления, удаления и редактирования

    private void addBank() {
        String name = JOptionPane.showInputDialog("Введите название банка:");
        if (name != null && !name.trim().isEmpty()) {
            bankService.addBank(name);
            loadBanks();
        }
    }

    private void deleteBank() {
        int bankId = getSelectedId(bankList, "🏦");
        if (bankId != -1) {
            bankService.deleteBank(bankId);
            loadBanks();
        }
    }

    private void editBank() {
        int bankId = getSelectedId(bankList, "🏦");
        if (bankId != -1) {
            Bank bank = bankService.findBankById(bankId); // Use findBankById
            if (bank != null) {
                String newName = JOptionPane.showInputDialog(this, "Введите новое название банка:", bank.getName());
                if (newName != null && !newName.trim().isEmpty()) {
                    bankService.updateBank(bankId, newName);
                    loadBanks();
                }
            }
        }
    }

    private void addBranch() {
        String name = JOptionPane.showInputDialog("Введите название филиала:");
        int bankId = getSelectedId(bankList, "🏦");
        if (bankId == -1) {
            JOptionPane.showMessageDialog(this, "Сначала выберите банк.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if no bank is selected
        }
        if (name != null && !name.trim().isEmpty()) {
            branchService.addBranch(name, bankId);
            loadBranches();
        }
    }
    private void editBranch() {
        int branchId = getSelectedId(branchList, "🏢");
        if (branchId != -1) {
            Branch branch = branchService.findBranchById(branchId); // Use findBranchById
            if (branch != null) {
                String newName = JOptionPane.showInputDialog(this, "Введите новое название филиала:", branch.getName());
                int bankId = getSelectedId(bankList, "🏦");
                if (bankId == -1) {
                    JOptionPane.showMessageDialog(this, "Сначала выберите банк.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method if no bank is selected
                }

                if (newName != null && !newName.trim().isEmpty() ) {
                    branchService.updateBranch(branchId, newName,bankId);
                    loadBranches();
                }
            }
        }
    }

    private void deleteBranch() {
        int branchId = getSelectedId(branchList, "🏢");
        if (branchId != -1) {
            branchService.deleteBranch(branchId);
            loadBranches();
        }
    }

    private void addDepositor() {
        String fullName = JOptionPane.showInputDialog("Введите ФИО вкладчика:");
        String contact = JOptionPane.showInputDialog("Введите контактные данные:");
        if (fullName != null && contact != null && !fullName.trim().isEmpty() && !contact.trim().isEmpty()) {
            depositorService.addDepositor(fullName, contact);
            loadDepositors();
        } else {
            JOptionPane.showMessageDialog(this, "Пожалуйста, заполните все поля.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDepositor() {
        int depositorId = getSelectedId(depositorList, "👤");
        if (depositorId != -1) {
            depositorService.deleteDepositor(depositorId);
            loadDepositors();
        }
    }

    private void editDepositor() {
        int depositorId = getSelectedId(depositorList, "👤");
        if (depositorId != -1) {
            Depositor depositor = depositorService.getDepositorById(depositorId);
            if (depositor != null) {
                String newFullName = JOptionPane.showInputDialog(this, "Введите новое ФИО:", depositor.getFullName());
                String newContact = JOptionPane.showInputDialog(this, "Введите новые контактные данные:", depositor.getContact());

                if (newFullName != null && !newFullName.trim().isEmpty() && newContact != null && !newContact.trim().isEmpty()) {
                    depositorService.updateDepositor(depositorId, newFullName, newContact);
                    loadDepositors();
                } else {
                    JOptionPane.showMessageDialog(this, "Пожалуйста, заполните все поля.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void addDeposit() {
        String amountStr = JOptionPane.showInputDialog("Введите сумму депозита:");
        int branchId = getSelectedId(branchList, "🏢");
        int depositorId = getSelectedId(depositorList, "👤");


        if (branchId == -1 || depositorId == -1) {
            JOptionPane.showMessageDialog(this, "Выберите филиал и вкладчика.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                depositService.addDeposit(amount, branchId, depositorId);
                loadDeposits();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Некорректная сумма!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Введите сумму депозита.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void deleteDeposit() {
        int depositId = getSelectedId(depositList, "💰");
        if (depositId != -1) {
            depositService.deleteDeposit(depositId);
            loadDeposits();
        }
    }

    private void editDeposit() {
        int depositId = getSelectedId(depositList, "💰");
        if (depositId != -1) {
            Deposit deposit = depositService.getDepositById(depositId);
            if (deposit != null) {
                String newAmountStr = JOptionPane.showInputDialog(this, "Введите новую сумму депозита:", deposit.getAmount());
                if (newAmountStr != null && !newAmountStr.trim().isEmpty()) {
                    try {
                        double newAmount = Double.parseDouble(newAmountStr);
                        depositService.updateDepositAmount(depositId, newAmount);
                        loadDeposits(); // Reload the deposit list to show updated data
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Некорректная сумма.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Сумма не может быть пустой.", "Ошибка", JOptionPane.ERROR_MESSAGE);

                }
            }

        }
    }


    private int getSelectedId(JList<String> list, String emoji) {
        String selected = list.getSelectedValue();
        if (selected != null) {
            try {
                return Integer.parseInt(selected.split(":")[0].replace(emoji, "").trim());
            } catch (NumberFormatException ignored) {
            }
        }
        return -1;
    }


    private void showTotalDeposits() {
        double totalAmount = depositService.getTotalDepositsAmount();
        JOptionPane.showMessageDialog(this, "Общая сумма депозитов: " + totalAmount, "Отчет", JOptionPane.INFORMATION_MESSAGE);
    }

    private void findDepositByDepositor() {
        String depositorIdStr = JOptionPane.showInputDialog("Введите ID вкладчика:");
        if (depositorIdStr != null && !depositorIdStr.trim().isEmpty()) {
            try {
                int depositorId = Integer.parseInt(depositorIdStr);
                List<Deposit> deposits = depositService.findDepositByDepositorId(depositorId);
                if (deposits.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Депозиты для вкладчика с ID " + depositorId + " не найдены.", "Результат поиска", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Депозиты вкладчика с ID ").append(depositorId).append(":\n");
                    for (Deposit deposit : deposits) {
                        sb.append("💰 Депозит ID: ").append(deposit.getId()).append(", Сумма: ").append(deposit.getAmount()).append("\n");
                    }
                    JOptionPane.showMessageDialog(this, sb.toString(), "Результат поиска", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Некорректный ID вкладчика.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Введите ID вкладчика.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingSystemGUI().setVisible(true));
    }
}