import java.io.*;
import java.util.*;

public class BankSystem {
    private static final String FILE_NAME = "accounts.txt";
    private static List<BankAccount> accounts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccounts();

        while (true) {
            System.out.println("\n--- BANK MANAGEMENT SYSTEM ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> checkBalance();
                case 5 -> {
                    saveAccounts();
                    System.out.println("Thank you for using the system!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // private static void createAccount() {
    //     System.out.print("Enter Account Number: ");
    //     String accNo = scanner.nextLine();
    //     System.out.print("Enter Account Holder Name: ");
    //     String name = scanner.nextLine();

    //     BankAccount account = new BankAccount(accNo, name);
    //     accounts.add(account);
    //     System.out.println("Account created successfully!");
    // }

    private static void createAccount() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();
    
        // Check if account number already exists
        if (findAccount(accNo) != null) {
            System.out.println("Account with this number already exists!");
            return;
        }
    
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
    
        BankAccount account = new BankAccount(accNo, name);
        accounts.add(account);
        System.out.println("Account created successfully!");
    }
    

    private static BankAccount findAccount(String accNo) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accNo)) return acc;
        }
        return null;
    }

    private static void deposit() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();
        BankAccount account = findAccount(accNo);

        if (account != null) {
            System.out.print("Enter amount to deposit: ");
            double amount = scanner.nextDouble();
            account.deposit(amount);
            System.out.println("Deposit successful!");
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void withdraw() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();
        BankAccount account = findAccount(accNo);

        if (account != null) {
            System.out.print("Enter amount to withdraw: ");
            double amount = scanner.nextDouble();
            if (account.withdraw(amount)) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Insufficient balance!");
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void checkBalance() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();
        BankAccount account = findAccount(accNo);

        if (account != null) {
            System.out.println("Account Holder: " + account.getHolderName());
            // System.out.println("Balance: ₹" + account.getBalance());
            System.out.println("Balance: Rs. " + account.getBalance());
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void loadAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                accounts.add(BankAccount.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("No existing accounts found. Starting fresh.");
        }
    }

    private static void saveAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (BankAccount acc : accounts) {
                writer.write(acc.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts!");
        }
    }
}