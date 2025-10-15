# SecureBankingApp_H240557E
Description
Secure Console Banking Application is a Java-based banking system that demonstrates core banking operations with robust security measures. It provides a text-based interface where users can register, login, create bank accounts, perform transactions (deposits/withdrawals), and view their account history. All data is securely persisted to encrypted files without requiring a database.

reg number H240557E
name Oprah A Madaka

Features Input Sanitization - Removes XSS-dangerous characters (< > " ') from all user inputs.
Data Encryption - AES-256 encryption for all stored data in files.
Access Control - Users can only access their own accounts via principle of least privilege.
Input Validation - Regex patterns validate usernames, emails, amounts, and account numbers.
Secure Error Handling - Generic user messages with detailed internal logging only.
