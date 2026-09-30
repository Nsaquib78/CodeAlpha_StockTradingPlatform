import java.io.*;
import java.util.*;

public class StockTradingPlatform {
    private double balance = 100000.0;
    private List<Stock> availableStocks;
    private List<PortfolioItem> portfolio;
    private List<Transaction> transactions;
    private Scanner scanner;
    private final String PORTFOLIO_FILE = "portfolio.txt";
    private final String TRANSACTIONS_FILE = "transactions.txt";
    private final String BALANCE_FILE = "balance.txt";

    public StockTradingPlatform() {
        availableStocks = new ArrayList<>();
        portfolio = new ArrayList<>();
        transactions = new ArrayList<>();
        scanner = new Scanner(System.in);
        initializeStocks();
        loadData();
    }

    private void initializeStocks() {
        availableStocks.add(new Stock("TECH", "TechNova", 500.0));
        availableStocks.add(new Stock("BANK", "SecureBank", 800.0));
        availableStocks.add(new Stock("AUTO", "AutoDrive", 650.0));
        availableStocks.add(new Stock("PHARMA", "MediCare", 450.0));
        availableStocks.add(new Stock("ENERGY", "GreenEnergy", 700.0));
    }

    public void start() {
        System.out.println("Welcome to the CodeAlpha Stock Trading Platform!");
        boolean running = true;
        while (running) {
            System.out.println("\n===== Stock Trading Platform =====");
            System.out.println("1. View Stocks");
            System.out.println("2. Search Stock");
            System.out.println("3. Buy Stock");
            System.out.println("4. Sell Stock");
            System.out.println("5. View Portfolio");
            System.out.println("6. Transaction History");
            System.out.println("7. Profit/Loss");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewStocks();
                    break;
                case 2:
                    searchStock();
                    break;
                case 3:
                    buyStock();
                    break;
                case 4:
                    sellStock();
                    break;
                case 5:
                    viewPortfolio();
                    break;
                case 6:
                    viewTransactionHistory();
                    break;
                case 7:
                    viewProfitLoss();
                    break;
                case 8:
                    running = false;
                    saveData();
                    System.out.println("Thank you for using CodeAlpha Stock Trading Platform. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select from 1 to 8.");
            }
        }
    }

    private void viewStocks() {
        System.out.println("\n--- Available Stocks ---");
        for (Stock stock : availableStocks) {
            System.out.println("Symbol: " + stock.getSymbol() + " | Company: " + stock.getCompanyName() + " | Price: INR " + stock.getCurrentPrice());
        }
    }

    private void searchStock() {
        System.out.print("Enter Stock Symbol to search: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock found = findStock(symbol);
        if (found != null) {
            System.out.println("Stock Found - Symbol: " + found.getSymbol() + " | Company: " + found.getCompanyName() + " | Price: INR " + found.getCurrentPrice());
        } else {
            System.out.println("Stock not found.");
        }
    }

    private Stock findStock(String symbol) {
        for (Stock stock : availableStocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                return stock;
            }
        }
        return null;
    }

    private void buyStock() {
        System.out.print("Enter Stock Symbol to buy: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = findStock(symbol);
        if (stock == null) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        System.out.print("Enter quantity to buy: ");
        int quantity = -1;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        double totalCost = quantity * stock.getCurrentPrice();
        if (balance < totalCost) {
            System.out.println("Insufficient balance. Total cost: INR " + totalCost + " | Available Balance: INR " + balance);
            return;
        }

        balance -= totalCost;
        
        PortfolioItem existingItem = getPortfolioItem(symbol);
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            double totalSpent = (existingItem.getQuantity() * existingItem.getAveragePurchasePrice()) + totalCost;
            existingItem.setQuantity(newQuantity);
            existingItem.setAveragePurchasePrice(totalSpent / newQuantity);
        } else {
            portfolio.add(new PortfolioItem(symbol, quantity, stock.getCurrentPrice()));
        }

        transactions.add(new Transaction("BUY", symbol, quantity, stock.getCurrentPrice()));
        System.out.println("Successfully bought " + quantity + " shares of " + symbol + " for INR " + totalCost);
    }

    private void sellStock() {
        System.out.print("Enter Stock Symbol to sell: ");
        String symbol = scanner.nextLine().toUpperCase();
        Stock stock = findStock(symbol);
        if (stock == null) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        PortfolioItem item = getPortfolioItem(symbol);
        if (item == null || item.getQuantity() == 0) {
            System.out.println("You do not own any shares of " + symbol + ".");
            return;
        }

        System.out.print("Enter quantity to sell (Max: " + item.getQuantity() + "): ");
        int quantity = -1;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        if (quantity > item.getQuantity()) {
            System.out.println("You cannot sell more shares than you own.");
            return;
        }

        double totalValue = quantity * stock.getCurrentPrice();
        balance += totalValue;

        item.setQuantity(item.getQuantity() - quantity);
        if (item.getQuantity() == 0) {
            portfolio.remove(item);
        }

        transactions.add(new Transaction("SELL", symbol, quantity, stock.getCurrentPrice()));
        System.out.println("Successfully sold " + quantity + " shares of " + symbol + " for INR " + totalValue);
    }

    private PortfolioItem getPortfolioItem(String symbol) {
        for (PortfolioItem item : portfolio) {
            if (item.getSymbol().equalsIgnoreCase(symbol)) {
                return item;
            }
        }
        return null;
    }

    private void viewPortfolio() {
        System.out.println("\n--- Your Portfolio ---");
        if (portfolio.isEmpty()) {
            System.out.println("Your portfolio is currently empty.");
        } else {
            double totalStockValue = 0;
            for (PortfolioItem item : portfolio) {
                Stock stock = findStock(item.getSymbol());
                if (stock != null) {
                    double currentPrice = stock.getCurrentPrice();
                    double totalValue = item.getQuantity() * currentPrice;
                    totalStockValue += totalValue;
                    System.out.println("Symbol: " + item.getSymbol() + " | Company: " + stock.getCompanyName() + 
                                       " | Qty: " + item.getQuantity() + " | Current Price: INR " + currentPrice + 
                                       " | Total Value: INR " + totalValue);
                }
            }
            System.out.println("Total Stock Value: INR " + totalStockValue);
        }
        System.out.println("Available Cash Balance: INR " + balance);
    }

    private void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t.getType() + " | Symbol: " + t.getSymbol() + " | Qty: " + t.getQuantity() + 
                                   " | Price: INR " + t.getPrice() + " | Total Amount: INR " + t.getTotalAmount());
            }
        }
    }

    private void viewProfitLoss() {
        System.out.println("\n--- Profit/Loss ---");
        double totalInvested = 0;
        double currentTotalValue = 0;

        for (PortfolioItem item : portfolio) {
            totalInvested += item.getQuantity() * item.getAveragePurchasePrice();
            Stock stock = findStock(item.getSymbol());
            if (stock != null) {
                currentTotalValue += item.getQuantity() * stock.getCurrentPrice();
            }
        }

        double profitLoss = currentTotalValue - totalInvested;
        System.out.println("Total Amount Invested in active portfolio: INR " + totalInvested);
        System.out.println("Current Portfolio Value: INR " + currentTotalValue);
        
        if (profitLoss > 0) {
            System.out.println("Unrealized Profit: INR " + profitLoss);
        } else if (profitLoss < 0) {
            System.out.println("Unrealized Loss: INR " + Math.abs(profitLoss));
        } else {
            System.out.println("No Profit / No Loss");
        }
    }

    private void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BALANCE_FILE))) {
            writer.println(balance);
        } catch (IOException e) {
            System.out.println("Error saving balance: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(PORTFOLIO_FILE))) {
            for (PortfolioItem item : portfolio) {
                writer.println(item.getSymbol() + "," + item.getQuantity() + "," + item.getAveragePurchasePrice());
            }
        } catch (IOException e) {
            System.out.println("Error saving portfolio: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction t : transactions) {
                writer.println(t.getType() + "," + t.getSymbol() + "," + t.getQuantity() + "," + t.getPrice());
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    private void loadData() {
        File balanceFile = new File(BALANCE_FILE);
        if (balanceFile.exists()) {
            try (Scanner fileScanner = new Scanner(balanceFile)) {
                if (fileScanner.hasNextDouble()) {
                    balance = fileScanner.nextDouble();
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error loading balance.");
            }
        }

        File portfolioFile = new File(PORTFOLIO_FILE);
        if (portfolioFile.exists()) {
            try (Scanner fileScanner = new Scanner(portfolioFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        portfolio.add(new PortfolioItem(parts[0], Integer.parseInt(parts[1]), Double.parseDouble(parts[2])));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error loading portfolio.");
            }
        }

        File transactionsFile = new File(TRANSACTIONS_FILE);
        if (transactionsFile.exists()) {
            try (Scanner fileScanner = new Scanner(transactionsFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        transactions.add(new Transaction(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3])));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error loading transactions.");
            }
        }
    }
}
