package com.calculator.mortgage.data;

public class loanEntry {
    private int current_month;
    private double principal;
    private double interest;
    private double balance;
    private double total;

    // Getters
    public int getCurrent_month() {
        return current_month;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getInterest() {
        return interest;
    }

    public double getBalance() {
        return balance;
    }

    public double getTotal() {
        return total;
    }

    // Setters
    public void setCurrent_month(int current_month) {
        this.current_month = current_month;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}