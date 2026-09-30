# CodeAlpha Stock Trading Platform

## Description
A Java console-based stock trading simulation built for a CodeAlpha internship project. The application allows users to simulate buying and selling stocks, view their portfolio, track transaction history, and calculate unrealized profit and loss. This project relies entirely on core Java features without any external databases, libraries, or real-time APIs.

## Features
- **User Account:** Start with a ₹100,000 balance to trade with.
- **Stock Market:** A predefined list of fictional stocks to trade.
- **View & Search:** Display available stocks and search by stock symbol.
- **Buy/Sell Stocks:** Purchase shares and sell existing shares, with robust input validation.
- **Portfolio Management:** Track owned stocks, quantity, average purchase price, and current valuation.
- **Transaction History:** Records every BUY and SELL action.
- **Profit/Loss Tracking:** See a basic summary of unrealized profit or loss based on your active portfolio.
- **Data Persistence:** Automatically saves your balance, portfolio, and transaction data to simple text files upon exit.

## Technologies Used
- Java (Core Java)
- File I/O (for simple data persistence)

## OOP Concepts Used
- **Classes and Objects:** Separated the application into well-defined components (`Stock`, `PortfolioItem`, `Transaction`, `StockTradingPlatform`).
- **Encapsulation:** Used `private` fields with `public` getters/setters to protect data integrity.
- **Collections:** Utilized `ArrayList` to manage dynamic lists of stocks, portfolio items, and transactions.

## How to Run
1. Make sure you have the Java Development Kit (JDK) installed.
2. Open a terminal or command prompt and navigate to the project's root directory.
3. Compile the Java files:
   ```bash
   javac src/*.java
   ```
4. Run the main class:
   ```bash
   java -cp src Main
   ```

## Project Structure
- `src/Main.java`: The entry point of the program.
- `src/Stock.java`: Model representing a stock in the market.
- `src/PortfolioItem.java`: Model for a stock owned by the user in their portfolio.
- `src/Transaction.java`: Model representing a single buy/sell transaction.
- `src/StockTradingPlatform.java`: The core application logic managing state, file I/O, and the console menu.

## Example Usage
- Start the application and view the available stocks (Option 1).
- Buy a stock by specifying the symbol and the quantity (Option 3).
- View your updated portfolio to see your holdings and available cash balance (Option 5).
- At any point, view your transaction history or see if your portfolio is in profit or loss.
- Exit the program (Option 8) to automatically save your data. Next time you start the app, your balance and portfolio will be restored!

## Testing Summary
- Verified viewing of available stocks.
- Tested successful stock purchasing and selling operations.
- Validated error handling:
  - Insufficient balance when attempting large purchases.
  - Selling more shares than currently owned.
  - Entering invalid negative or zero quantities.
  - Providing invalid alphabetic characters for quantity fields (avoids crashing).
  - Inputting invalid stock symbols.
- Successfully verified calculation of total portfolio value and profit/loss.
- Confirmed transaction history logging.
- Verified successful file saving and loading across sessions.
