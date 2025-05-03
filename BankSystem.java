import java.io.*;
import java.util.*;

class BankAccount {
    private String accountHolder;
    private int accountNumber;
    private double balance;
    private List<String> transactionHistory;

    public BankAccount(String accountHolder, int accountNumber, double balance, List<String> transactionHistory) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionHistory = transactionHistory != null ? transactionHistory : new ArrayList<>();
        addTransaction("Account loaded with balance: " + balance);
    }

    public BankAccount(String accountHolder, int accountNumber, double balance) {
        this(accountHolder, accountNumber, balance, new ArrayList<>());
        addTransaction("Account created with initial balance: " + balance);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("Deposited: " + amount);
        } else {
            System.out.println("Amount to deposit should be greater than 0.");
        }
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            addTransaction("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    public void checkBalance() {
        System.out.println("Account Holder: " + accountHolder);
        System.out.println("Current balance: " + balance);
    }

    public void viewTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("Transaction History:");
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    private void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String toFileString() {
        return accountHolder + "|" + accountNumber + "|" + balance + "|" + String.join(",", transactionHistory);
    }

    public static BankAccount fromFileString(String line) {
        String[] parts = line.split("\\|");
        String name = parts[0];
        int number = Integer.parseInt(parts[1]);
        double balance = Double.parseDouble(parts[2]);
        List<String> transactions = new ArrayList<>();
        if (parts.length > 3 && !parts[3].isEmpty()) {
            transactions = new ArrayList<>(Arrays.asList(parts[3].split(",")));
        }
        return new BankAccount(name, number, balance, transactions);
    }
}

public class BankManagementSystem {
    private static final String FILE_NAME = "account.txt";
    private static Map<Integer, BankAccount> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccountsFromFile();

        while (true) {
            System.out.println("\nBank Management System");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: createAccount(); break;
                case 2: depositMoney(); break;
                case 3: withdrawMoney(); break;
                case 4: checkBalance(); break;
                case 5: viewTransactionHistory(); break;
                case 6:
                    saveAccountsToFile();
                    System.out.println("Thank you for using the Bank Management System.");
                    return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter account holder's name: ");
        scanner.nextLine(); // consume newline
        String name = scanner.nextLine();
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();

        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account number already exists. Please try a new account number.");
            return;
        }

        System.out.print("Enter initial deposit: ");
        double initialDeposit = scanner.nextDouble();

        BankAccount newAccount = new BankAccount(name, accountNumber, initialDeposit);
        accounts.put(accountNumber, newAccount);
        System.out.println("Account created successfully.");
    }

    private static void depositMoney() {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        BankAccount account = accounts.get(accountNumber);

        if (account != null) {
            System.out.print("Enter amount to deposit: ");
            double depositAmount = scanner.nextDouble();
            account.deposit(depositAmount);
            account.checkBalance(); // Show balance after deposit
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void withdrawMoney() {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        BankAccount account = accounts.get(accountNumber);

        if (account != null) {
            System.out.print("Enter amount to withdraw: ");
            double withdrawAmount = scanner.nextDouble();
            account.withdraw(withdrawAmount);
            account.checkBalance(); // Show balance after withdrawal
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void checkBalance() {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        BankAccount account = accounts.get(accountNumber);

        if (account != null) {
            account.checkBalance();
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewTransactionHistory() {
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();
        BankAccount account = accounts.get(accountNumber);

        if (account != null) {
            account.viewTransactionHistory();
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void saveAccountsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (BankAccount account : accounts.values()) {
                writer.write(account.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    private static void loadAccountsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                BankAccount account = BankAccount.fromFileString(line);
                accounts.put(account.getAccountNumber(), account);
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }
}
