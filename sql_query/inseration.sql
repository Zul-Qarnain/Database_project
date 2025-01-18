-- 1. Admin Table
BEGIN
    INSERT INTO Admin (AdminID, aEmail) VALUES (1, 'admin1@example.com');
    INSERT INTO Admin (AdminID, aEmail) VALUES (2, 'admin2@example.com');
    INSERT INTO Admin (AdminID, aEmail) VALUES (3, 'admin3@example.com');
    INSERT INTO Admin (AdminID, aEmail) VALUES (4, 'admin4@example.com');
    INSERT INTO Admin (AdminID, aEmail) VALUES (5, 'admin5@example.com');
    COMMIT;
END;
/

-- 2. Admin Credentials Table
BEGIN
    INSERT INTO AdminCredentials (AdminID, aPassword, aUsername) VALUES (1, 'password1', 'admin1');
    INSERT INTO AdminCredentials (AdminID, aPassword, aUsername) VALUES (2, 'password2', 'admin2');
    INSERT INTO AdminCredentials (AdminID, aPassword, aUsername) VALUES (3, 'password3', 'admin3');
    INSERT INTO AdminCredentials (AdminID, aPassword, aUsername) VALUES (4, 'password4', 'admin4');
    INSERT INTO AdminCredentials (AdminID, aPassword, aUsername) VALUES (5, 'password5', 'admin5');
    COMMIT;
END;
/

-- 3. Books Table
BEGIN
    INSERT INTO Books (ISBN, Author, Title, Quantity, Price, AdminID) VALUES (101, 'J.K. Rowling', 'Harry Potter and the Philosopher''s Stone', 10, 29.99, 1);
    INSERT INTO Books (ISBN, Author, Title, Quantity, Price, AdminID) VALUES (102, 'J.R.R. Tolkien', 'The Hobbit', 15, 19.99, 2);
    INSERT INTO Books (ISBN, Author, Title, Quantity, Price, AdminID) VALUES (103, 'George Orwell', '1984', 20, 14.99, 3);
    INSERT INTO Books (ISBN, Author, Title, Quantity, Price, AdminID) VALUES (104, 'Mark Twain', 'The Adventures of Huckleberry Finn', 5, 9.99, 4);
    INSERT INTO Books (ISBN, Author, Title, Quantity, Price, AdminID) VALUES (105, 'F. Scott Fitzgerald', 'The Great Gatsby', 8, 12.99, 5);
    COMMIT;
END;
/

-- 4. Books Backup Table
BEGIN
    INSERT INTO BooksBackup (ISBN, Title, Author, Quantity, Price) VALUES (101, 'Harry Potter and the Philosopher''s Stone', 'J.K. Rowling', 10, 29.99);
    INSERT INTO BooksBackup (ISBN, Title, Author, Quantity, Price) VALUES (102, 'The Hobbit', 'J.R.R. Tolkien', 15, 19.99);
    INSERT INTO BooksBackup (ISBN, Title, Author, Quantity, Price) VALUES (103, '1984', 'George Orwell', 20, 14.99);
    INSERT INTO BooksBackup (ISBN, Title, Author, Quantity, Price) VALUES (104, 'The Adventures of Huckleberry Finn', 'Mark Twain', 5, 9.99);
    INSERT INTO BooksBackup (ISBN, Title, Author, Quantity, Price) VALUES (105, 'The Great Gatsby', 'F. Scott Fitzgerald', 8, 12.99);
    COMMIT;
END;
/

-- 5. Book Category Table
BEGIN
    INSERT INTO BookCategory (CategoryID, CategoryName, ISBN) VALUES (1, 'Fantasy', 101);
    INSERT INTO BookCategory (CategoryID, CategoryName, ISBN) VALUES (2, 'Adventure', 102);
    INSERT INTO BookCategory (CategoryID, CategoryName, ISBN) VALUES (3, 'Dystopian', 103);
    INSERT INTO BookCategory (CategoryID, CategoryName, ISBN) VALUES (4, 'Classic', 104);
    INSERT INTO BookCategory (CategoryID, CategoryName, ISBN) VALUES (5, 'Literature', 105);
    COMMIT;
END;
/

-- 6. Category Admin Table
BEGIN
    INSERT INTO CategoryAdmin (CategoryID, CategoryName, AdminID) VALUES (1, 'Fantasy', 1);
    INSERT INTO CategoryAdmin (CategoryID, CategoryName, AdminID) VALUES (2, 'Adventure', 2);
    INSERT INTO CategoryAdmin (CategoryID, CategoryName, AdminID) VALUES (3, 'Dystopian', 3);
    INSERT INTO CategoryAdmin (CategoryID, CategoryName, AdminID) VALUES (4, 'Classic', 4);
    INSERT INTO CategoryAdmin (CategoryID, CategoryName, AdminID) VALUES (5, 'Literature', 5);
    COMMIT;
END;
/

-- 7. Orders Table
BEGIN
    INSERT INTO Orders (OrderID, OrderDate) VALUES (1, TO_DATE('2025-01-01', 'YYYY-MM-DD'));
    INSERT INTO Orders (OrderID, OrderDate) VALUES (2, TO_DATE('2025-01-02', 'YYYY-MM-DD'));
    INSERT INTO Orders (OrderID, OrderDate) VALUES (3, TO_DATE('2025-01-03', 'YYYY-MM-DD'));
    INSERT INTO Orders (OrderID, OrderDate) VALUES (4, TO_DATE('2025-01-04', 'YYYY-MM-DD'));
    INSERT INTO Orders (OrderID, OrderDate) VALUES (5, TO_DATE('2025-01-05', 'YYYY-MM-DD'));
    COMMIT;
END;
/

-- 8. Orders with Admin Table
BEGIN
    INSERT INTO OrdersWithAdmin (OrderID, HouseNo, Zip_Code, City, AdminID) VALUES (1, '123', '10001', 'New York', 1);
    INSERT INTO OrdersWithAdmin (OrderID, HouseNo, Zip_Code, City, AdminID) VALUES (2, '124', '20002', 'Los Angeles', 2);
    INSERT INTO OrdersWithAdmin (OrderID, HouseNo, Zip_Code, City, AdminID) VALUES (3, '125', '30003', 'Chicago', 3);
    INSERT INTO OrdersWithAdmin (OrderID, HouseNo, Zip_Code, City, AdminID) VALUES (4, '126', '40004', 'Houston', 4);
    INSERT INTO OrdersWithAdmin (OrderID, HouseNo, Zip_Code, City, AdminID) VALUES (5, '127', '50005', 'Phoenix', 5);
    COMMIT;
END;
/

-- 9. Orders with Books Table
BEGIN
    INSERT INTO OrdersWithBooks (OrderID, HouseNo, Zip_Code, City, ISBN) VALUES (1, '123', '10001', 'New York', 101);
    INSERT INTO OrdersWithBooks (OrderID, HouseNo, Zip_Code, City, ISBN) VALUES (2, '124', '20002', 'Los Angeles', 102);
    INSERT INTO OrdersWithBooks (OrderID, HouseNo, Zip_Code, City, ISBN) VALUES (3, '125', '30003', 'Chicago', 103);
    INSERT INTO OrdersWithBooks (OrderID, HouseNo, Zip_Code, City, ISBN) VALUES (4, '126', '40004', 'Houston', 104);
    INSERT INTO OrdersWithBooks (OrderID, HouseNo, Zip_Code, City, ISBN) VALUES (5, '127', '50005', 'Phoenix', 105);
    COMMIT;
END;
/

-- 10. Customer Table
BEGIN
    INSERT INTO Customer (CustomerID, cEmail) VALUES (1, 'customer1@example.com');
    INSERT INTO Customer (CustomerID, cEmail) VALUES (2, 'customer2@example.com');
    INSERT INTO Customer (CustomerID, cEmail) VALUES (3, 'customer3@example.com');
    INSERT INTO Customer (CustomerID, cEmail) VALUES (4, 'customer4@example.com');
    INSERT INTO Customer (CustomerID, cEmail) VALUES (5, 'customer5@example.com');
    COMMIT;
END;
/

-- 11. Customer Credentials Table
BEGIN
    INSERT INTO CustomerCredentials (CustomerID, cUsername, cPassword) VALUES (1, 'customer1', 'password1');
    INSERT INTO CustomerCredentials (CustomerID, cUsername, cPassword) VALUES (2, 'customer2', 'password2');
    INSERT INTO CustomerCredentials (CustomerID, cUsername, cPassword) VALUES (3, 'customer3', 'password3');
    INSERT INTO CustomerCredentials (CustomerID, cUsername, cPassword) VALUES (4, 'customer4', 'password4');
    INSERT INTO CustomerCredentials (CustomerID, cUsername, cPassword) VALUES (5, 'customer5', 'password5');
    COMMIT;
END;
/

-- 12. Cart Table
BEGIN
    INSERT INTO Cart (CartID, Amount, CartItem, CustomerID) VALUES (1, 59.97, 'Harry Potter, The Hobbit, 1984', 1);
    INSERT INTO Cart (CartID, Amount, CartItem, CustomerID) VALUES (2, 39.97, 'The Hobbit, 1984, The Adventures of Huckleberry Finn', 2);
    INSERT INTO Cart (CartID, Amount, CartItem, CustomerID) VALUES (3, 29.97, '1984, The Great Gatsby', 3);
    INSERT INTO Cart (CartID, Amount, CartItem, CustomerID) VALUES (4, 69.97, 'Harry Potter, The Adventures of Huckleberry Finn, The Great Gatsby', 4);
    INSERT INTO Cart (CartID, Amount, CartItem, CustomerID) VALUES (5, 79.97, 'Harry Potter, The Hobbit, 1984, The Great Gatsby', 5);
    COMMIT;
END;
/

-- 13. Orders with Customers Table
BEGIN
    INSERT INTO OrdersWithCustomers (OrderID, HouseNo, Zip_Code, City, CustomerID) VALUES (1, '123', '10001', 'New York', 1);
    INSERT INTO OrdersWithCustomers (OrderID, HouseNo, Zip_Code, City, CustomerID) VALUES (2, '124', '20002', 'Los Angeles', 2);
    INSERT INTO OrdersWithCustomers (OrderID, HouseNo, Zip_Code, City, CustomerID) VALUES (3, '125', '30003', 'Chicago', 3);
    INSERT INTO OrdersWithCustomers (OrderID, HouseNo, Zip_Code, City, CustomerID) VALUES (4, '126', '40004', 'Houston', 4);
    INSERT INTO OrdersWithCustomers (OrderID, HouseNo, Zip_Code, City, CustomerID) VALUES (5, '127', '50005', 'Phoenix', 5);
    COMMIT;
END;
/

-- 14. Orders with Cart Table
BEGIN
    INSERT INTO OrdersWithCart (OrderID, HouseNo, Zip_Code, City, CartID) VALUES (1, '123', '10001', 'New York', 1);
    INSERT INTO OrdersWithCart (OrderID, HouseNo, Zip_Code, City, CartID) VALUES (2, '124', '20002', 'Los Angeles', 2);
    INSERT INTO OrdersWithCart (OrderID, HouseNo, Zip_Code, City, CartID) VALUES (3, '125', '30003', 'Chicago', 3);
    INSERT INTO OrdersWithCart (OrderID, HouseNo, Zip_Code, City, CartID) VALUES (4, '126', '40004', 'Houston', 4);
    INSERT INTO OrdersWithCart (OrderID, HouseNo, Zip_Code, City, CartID) VALUES (5, '127', '50005', 'Phoenix', 5);
    COMMIT;
END;
/

-- 15. Cart Items Table
BEGIN
    INSERT INTO CartItems (CartID, Amount, CartItem) VALUES (1, 59.97, 'Harry Potter, The Hobbit, 1984');
    INSERT INTO CartItems (CartID, Amount, CartItem) VALUES (2, 39.97, 'The Hobbit, 1984, The Adventures of Huckleberry Finn');
    INSERT INTO CartItems (CartID, Amount, CartItem) VALUES (3, 29.97, '1984, The Great Gatsby');
    INSERT INTO CartItems (CartID, Amount, CartItem) VALUES (4, 69.97, 'Harry Potter, The Adventures of Huckleberry Finn, The Great Gatsby');
    INSERT INTO CartItems (CartID, Amount, CartItem) VALUES (5, 79.97, 'Harry Potter, The Hobbit, 1984, The Great Gatsby');
    COMMIT;
END;
/

-- 16. Payment Table
BEGIN
    INSERT INTO Payment (TXNID, PaymentType) VALUES (1, 'Credit Card');
    INSERT INTO Payment (TXNID, PaymentType) VALUES (2, 'Debit Card');
    INSERT INTO Payment (TXNID, PaymentType) VALUES (3, 'PayPal');
    INSERT INTO Payment (TXNID, PaymentType) VALUES (4, 'Cash');
    INSERT INTO Payment (TXNID, PaymentType) VALUES (5, 'Bank Transfer');
    COMMIT;
END;
/

-- 17. Payment with Orders Table
BEGIN
    INSERT INTO PaymentWithOrders (TXNID, PaymentDate, PhoneNum, OrderID) VALUES (1, TO_DATE('2025-01-01', 'YYYY-MM-DD'), '1234567890', 1);
    INSERT INTO PaymentWithOrders (TXNID, PaymentDate, PhoneNum, OrderID) VALUES (2, TO_DATE('2025-01-02', 'YYYY-MM-DD'), '2345678901', 2);
    INSERT INTO PaymentWithOrders (TXNID, PaymentDate, PhoneNum, OrderID) VALUES (3, TO_DATE('2025-01-03', 'YYYY-MM-DD'), '3456789012', 3);
    INSERT INTO PaymentWithOrders (TXNID, PaymentDate, PhoneNum, OrderID) VALUES (4, TO_DATE('2025-01-04', 'YYYY-MM-DD'), '4567890123', 4);
    INSERT INTO PaymentWithOrders (TXNID, PaymentDate, PhoneNum, OrderID) VALUES (5, TO_DATE('2025-01-05', 'YYYY-MM-DD'), '5678901234', 5);
    COMMIT;
END;
/

-- 18. Orders Address Table
BEGIN
    INSERT INTO OrdersAddress (OrderID, HouseNo, Zip_Code, City) VALUES (1, '123', '10001', 'New York');
    INSERT INTO OrdersAddress (OrderID, HouseNo, Zip_Code, City) VALUES (2, '124', '20002', 'Los Angeles');
    INSERT INTO OrdersAddress (OrderID, HouseNo, Zip_Code, City) VALUES (3, '125', '30003', 'Chicago');
    INSERT INTO OrdersAddress (OrderID, HouseNo, Zip_Code, City) VALUES (4, '126', '40004', 'Houston');
    INSERT INTO OrdersAddress (OrderID, HouseNo, Zip_Code, City) VALUES (5, '127', '50005', 'Phoenix');
    COMMIT;
END;
/

-- 19. Payment with Customers Table
BEGIN
    INSERT INTO PaymentWithCustomers (TXNID, PaymentDate, PhoneNum, CustomerID) VALUES (1, TO_DATE('2025-01-01', 'YYYY-MM-DD'), '1234567890', 1);
    INSERT INTO PaymentWithCustomers (TXNID, PaymentDate, PhoneNum, CustomerID) VALUES (2, TO_DATE('2025-01-02', 'YYYY-MM-DD'), '2345678901', 2);
    INSERT INTO PaymentWithCustomers (TXNID, PaymentDate, PhoneNum, CustomerID) VALUES (3, TO_DATE('2025-01-03', 'YYYY-MM-DD'), '3456789012', 3);
    INSERT INTO PaymentWithCustomers (TXNID, PaymentDate, PhoneNum, CustomerID) VALUES (4, TO_DATE('2025-01-04', 'YYYY-MM-DD'), '4567890123', 4);
    INSERT INTO PaymentWithCustomers (TXNID, PaymentDate, PhoneNum, CustomerID) VALUES (5, TO_DATE('2025-01-05', 'YYYY-MM-DD'), '5678901234', 5);
    COMMIT;
END;
/

-- 20. Loan Table
BEGIN
    INSERT INTO Loan (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, CustomerID) VALUES (1, 100.00, TO_DATE('2025-01-01', 'YYYY-MM-DD'), TO_DATE('2025-01-15', 'YYYY-MM-DD'), INTERVAL '14' DAY, 1);
    INSERT INTO Loan (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, CustomerID) VALUES (2, 200.00, TO_DATE('2025-01-02', 'YYYY-MM-DD'), TO_DATE('2025-01-16', 'YYYY-MM-DD'), INTERVAL '14' DAY, 2);
    INSERT INTO Loan (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, CustomerID) VALUES (3, 150.00, TO_DATE('2025-01-03', 'YYYY-MM-DD'), TO_DATE('2025-01-17', 'YYYY-MM-DD'), INTERVAL '14' DAY, 3);
    INSERT INTO Loan (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, CustomerID) VALUES (4, 250.00, TO_DATE('2025-01-04', 'YYYY-MM-DD'), TO_DATE('2025-01-18', 'YYYY-MM-DD'), INTERVAL '14' DAY, 4);
    INSERT INTO Loan (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, CustomerID) VALUES (5, 300.00, TO_DATE('2025-01-05', 'YYYY-MM-DD'), TO_DATE('2025-01-19', 'YYYY-MM-DD'), INTERVAL '14' DAY, 5);
    COMMIT;
END;
/

-- 21. Loan with Books Table
BEGIN
    INSERT INTO LoanWithBooks (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, ISBN) VALUES (1, 100.00, TO_DATE('2025-01-01', 'YYYY-MM-DD'), TO_DATE('2025-01-15', 'YYYY-MM-DD'), INTERVAL '14' DAY, 101);
    INSERT INTO LoanWithBooks (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, ISBN) VALUES (2, 200.00, TO_DATE('2025-01-02', 'YYYY-MM-DD'), TO_DATE('2025-01-16', 'YYYY-MM-DD'), INTERVAL '14' DAY, 102);
    INSERT INTO LoanWithBooks (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, ISBN) VALUES (3, 150.00, TO_DATE('2025-01-03', 'YYYY-MM-DD'), TO_DATE('2025-01-17', 'YYYY-MM-DD'), INTERVAL '14' DAY, 103);
    INSERT INTO LoanWithBooks (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, ISBN) VALUES (4, 250.00, TO_DATE('2025-01-04', 'YYYY-MM-DD'), TO_DATE('2025-01-18', 'YYYY-MM-DD'), INTERVAL '14' DAY, 104);
    INSERT INTO LoanWithBooks (LoanID, LoanAmount, BorrowDate, ReturnDate, BorrowTime, ISBN) VALUES (5, 300.00, TO_DATE('2025-01-05', 'YYYY-MM-DD'), TO_DATE('2025-01-19', 'YYYY-MM-DD'), INTERVAL '14' DAY, 105);
    COMMIT;
END;
/

-- 22. Cart Items with Books Table
BEGIN
    INSERT INTO CartItemsWithBooks (CartID, Amount, CartItem, ISBN) VALUES (1, 59.97, 'Harry Potter, The Hobbit, 1984', 101);
    INSERT INTO CartItemsWithBooks (CartID, Amount, CartItem, ISBN) VALUES (2, 39.97, 'The Hobbit, 1984, The Adventures of Huckleberry Finn', 102);
    INSERT INTO CartItemsWithBooks (CartID, Amount, CartItem, ISBN) VALUES (3, 29.97, '1984, The Great Gatsby', 103);
    INSERT INTO CartItemsWithBooks (CartID, Amount, CartItem, ISBN) VALUES (4, 69.97, 'Harry Potter, The Adventures of Huckleberry Finn, The Great Gatsby', 104);
    INSERT INTO CartItemsWithBooks (CartID, Amount, CartItem, ISBN) VALUES (5, 79.97, 'Harry Potter, The Hobbit, 1984, The Great Gatsby', 105);
    COMMIT;
END;
/
