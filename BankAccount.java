import java.io.Serializable;

public class BankAccount implements Serializable {
    private String accountNumber;
    private String holderName;
    private double balance;

    public BankAccount(String accountNumber, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0;
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return accountNumber + "," + holderName + "," + balance;
    }

    public static BankAccount fromString(String data) {
        String[] parts = data.split(",");
        BankAccount acc = new BankAccount(parts[0], parts[1]);
        acc.balance = Double.parseDouble(parts[2]);
        return acc;
    }
}