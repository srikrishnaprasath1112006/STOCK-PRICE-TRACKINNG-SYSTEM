import java.sql.*;
import java.util.Scanner;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Stock Symbol: " + symbol + ", Price: $" + price;
    }
}

class StockTracker {
    private Connection connection;

    public StockTracker() {
        try {
            
            String url = "jdbc:mysql://localhost:3306/stock_db"; 
            String username = "root"; 
            String password = "g$77ee#tha33"; 

            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Add a new stock to the database
    public void addStock(String symbol, double price) {
        try {
            // Check if stock already exists
            String checkQuery = "SELECT * FROM stocks WHERE symbol = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, symbol);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Stock already exists. Use the update option to modify the price.");
            } else {
                // Add new stock
                String insertQuery = "INSERT INTO stocks (symbol, price) VALUES (?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, symbol);
                insertStmt.setDouble(2, price);
                insertStmt.executeUpdate();
                System.out.println("Stock added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding stock: " + e.getMessage());
        }
    }

    // Update the price of an existing stock
    public void updateStockPrice(String symbol, double price) {
        try {
            String updateQuery = "UPDATE stocks SET price = ? WHERE symbol = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setDouble(1, price);
            updateStmt.setString(2, symbol);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Stock price updated successfully.");
            } else {
                System.out.println("Stock not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating stock price: " + e.getMessage());
        }
    }

    // View the details of a stock
    public void viewStock(String symbol) {
        try {
            String selectQuery = "SELECT * FROM stocks WHERE symbol = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setString(1, symbol);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String symbolFromDB = rs.getString("symbol");
                double priceFromDB = rs.getDouble("price");
                System.out.println("Stock Symbol: " + symbolFromDB + ", Price: $" + priceFromDB);
            } else {
                System.out.println("Stock not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving stock: " + e.getMessage());
        }
    }

    // List all stocks in the tracker
    public void listAllStocks() {
        try {
            String selectAllQuery = "SELECT * FROM stocks";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectAllQuery);

            if (rs.next()) {
                do {
                    String symbolFromDB = rs.getString("symbol");
                    double priceFromDB = rs.getDouble("price");
                    System.out.println("Stock Symbol: " + symbolFromDB + ", Price: $" + priceFromDB);
                } while (rs.next());
            } else {
                System.out.println("No stocks available.");
            }
        } catch (SQLException e) {
            System.out.println("Error listing all stocks: " + e.getMessage());
        }
    }
}

public class StockPriceTracker {
    private static Scanner scanner = new Scanner(System.in);
    private static StockTracker stockTracker = new StockTracker();

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n--- Stock Price Tracker ---");
            System.out.println("1. Add a new stock");
            System.out.println("2. Update stock price");
            System.out.println("3. View stock details");
            System.out.println("4. List all stocks");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    addStock();
                    break;
                case 2:
                    updateStockPrice();
                    break;
                case 3:
                    viewStockDetails();
                    break;
                case 4:
                    listAllStocks();
                    break;
                case 5:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private static void addStock() {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine();
        System.out.print("Enter stock price: ");
        double price = scanner.nextDouble();
        stockTracker.addStock(symbol, price);
    }

    private static void updateStockPrice() {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine();
        System.out.print("Enter new stock price: ");
        double price = scanner.nextDouble();
        stockTracker.updateStockPrice(symbol, price);
    }

    private static void viewStockDetails() {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine();
        stockTracker.viewStock(symbol);
    }

    private static void listAllStocks() {
        stockTracker.listAllStocks();
    }
}
