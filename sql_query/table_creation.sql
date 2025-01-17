-- 1. Admin Table
CREATE TABLE Admin (
    AdminID NUMBER PRIMARY KEY,
    aEmail VARCHAR2(100)
);

-- 2. Admin Credentials Table
CREATE TABLE AdminCredentials (
    AdminID NUMBER PRIMARY KEY,
    aPassword VARCHAR2(50),
    aUsername VARCHAR2(50),
    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

-- 3. Books Table
CREATE TABLE Books (
    ISBN NUMBER PRIMARY KEY,
    Author VARCHAR2(100),
    Title VARCHAR2(200),
    Quantity NUMBER,
    Price NUMBER(10, 2),
    AdminID NUMBER,
    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

-- 4. Books Backup Table
CREATE TABLE BooksBackup (
    ISBN NUMBER PRIMARY KEY,
    Title VARCHAR2(200),
    Author VARCHAR2(100),
    Quantity NUMBER,
    Price NUMBER(10, 2)
);

-- 5. Book Category Table
CREATE TABLE BookCategory (
    CategoryID NUMBER PRIMARY KEY,
    CategoryName VARCHAR2(100),
    ISBN NUMBER,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN)
);

-- 6. Category Admin Table
CREATE TABLE CategoryAdmin (
    CategoryID NUMBER PRIMARY KEY,
    CategoryName VARCHAR2(100),
    AdminID NUMBER,
    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

-- 7. Orders Table
CREATE TABLE Orders (
    OrderID NUMBER PRIMARY KEY,
    OrderDate DATE
);

-- 8. Orders with Admin Table
CREATE TABLE OrdersWithAdmin (
    OrderID NUMBER PRIMARY KEY,
    HouseNo VARCHAR2(50),
    Zip_Code VARCHAR2(10),
    City VARCHAR2(50),
    AdminID NUMBER,
    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

-- 9. Orders with Books Table
CREATE TABLE OrdersWithBooks (
    OrderID NUMBER PRIMARY KEY,
    HouseNo VARCHAR2(50),
    Zip_Code VARCHAR2(10),
    City VARCHAR2(50),
    ISBN NUMBER,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN)
);

-- 10. Customer Table
CREATE TABLE Customer (
    CustomerID NUMBER PRIMARY KEY,
    cEmail VARCHAR2(100)
);

-- 11. Customer Credentials Table
CREATE TABLE CustomerCredentials (
    CustomerID NUMBER PRIMARY KEY,
    cUsername VARCHAR2(50),
    cPassword VARCHAR2(50),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- 12. Cart Table
CREATE TABLE Cart (
    CartID NUMBER PRIMARY KEY,
    Amount NUMBER(10, 2),
    CartItem VARCHAR2(200),
    CustomerID NUMBER,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- 13. Orders with Customers Table
CREATE TABLE OrdersWithCustomers (
    OrderID NUMBER PRIMARY KEY,
    HouseNo VARCHAR2(50),
    Zip_Code VARCHAR2(10),
    City VARCHAR2(50),
    CustomerID NUMBER,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- 14. Orders with Cart Table
CREATE TABLE OrdersWithCart (
    OrderID NUMBER PRIMARY KEY,
    HouseNo VARCHAR2(50),
    Zip_Code VARCHAR2(10),
    City VARCHAR2(50),
    CartID NUMBER,
    FOREIGN KEY (CartID) REFERENCES Cart(CartID)
);

-- 15. Cart Items Table
CREATE TABLE CartItems (
    CartID NUMBER PRIMARY KEY,
    Amount NUMBER(10, 2),
    CartItem VARCHAR2(200)
);

-- 16. Transactions Table
CREATE TABLE Transactions (
    TXNID NUMBER PRIMARY KEY,
    PaymentType VARCHAR2(50)
);

-- 17. Transactions with Orders Table
CREATE TABLE TransactionsWithOrders (
    TXNID NUMBER PRIMARY KEY,
    PaymentDate DATE,
    PhoneNum VARCHAR2(15),
    OrderID NUMBER,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID)
);

-- 18. Orders Address Table
CREATE TABLE OrdersAddress (
    OrderID NUMBER PRIMARY KEY,
    HouseNo VARCHAR2(50),
    Zip_Code VARCHAR2(10),
    City VARCHAR2(50)
);

-- 19. Transactions with Customers Table
CREATE TABLE TransactionsWithCustomers (
    TXNID NUMBER PRIMARY KEY,
    PaymentDate DATE,
    PhoneNum VARCHAR2(15),
    CustomerID NUMBER,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- 20. Loan Table
CREATE TABLE Loan (
    LoanID NUMBER PRIMARY KEY,
    LoanAmount NUMBER(10, 2),
    BorrowDate DATE,
    ReturnDate DATE,
    BorrowTime INTERVAL DAY TO SECOND,
    CustomerID NUMBER,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- 21. Loan with Books Table
CREATE TABLE LoanWithBooks (
    LoanID NUMBER PRIMARY KEY,
    LoanAmount NUMBER(10, 2),
    BorrowDate DATE,
    ReturnDate DATE,
    BorrowTime INTERVAL DAY TO SECOND,
    ISBN NUMBER,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN)
);

-- 22. Cart Items with Books Table
CREATE TABLE CartItemsWithBooks (
    CartID NUMBER PRIMARY KEY,
    Amount NUMBER(10, 2),
    CartItem VARCHAR2(200),
    ISBN NUMBER,
    FOREIGN KEY (ISBN) REFERENCES Books(ISBN)
);
