package com.calculator.mortgage.mortgage_calculator;

import com.calculator.mortgage.data.*;
import javafx.collections.ObservableList;
import java.lang.Math;

public class loanCalculator {
    loanData monthly_payments = new loanData();

    public void loanCalculate(int amount, double rate, int term, loanType type) {
        double total_annuity = amount / ((Math.pow((1 + rate / 12), term) - 1) / (rate / 12 * Math.pow((1 + rate / 12), term)));
        double total_paid = 0;
        for (int month = 1; month <= term; ++month) {
            double balance = 0, interest = 0, principal = 0, total = 0;
            if (type == loanType.ANNUITY) {
                total = total_annuity;
                balance = amount - total_paid;
                interest = balance * rate / 12;
                principal = total - interest;
                total_paid += principal;
            } else if (type == loanType.LINEAR) {
                principal = (double)amount / term;
                interest = (amount - (month - 1) * ((double)amount / term)) * (rate / 12);
                balance = amount - total_paid;
                total = principal + interest;
                total_paid += principal;
            }

            sendMonthlyPayment(month, balance, interest, principal, total);
        }
    }

    public void loanCalculate(int amount, double rate, int term, loanType type, int delay_from, int delay_duration, double delay_rate) {
        double total_annuity = amount / ((Math.pow((1 + rate / 12), term) - 1) / (rate / 12 * Math.pow((1 + rate / 12), term)));
        double total_paid = 0;
        for (int month = 1; month <= (term + delay_duration); ++month) {
            double balance = 0, interest = 0, principal = 0, total = 0;
            if (month >= delay_from && month < (delay_from + delay_duration)) {
                balance = amount - total_paid;
                interest = balance * delay_rate / 12;
                total = interest;
            } else {
                if (type == loanType.ANNUITY) {
                    total = total_annuity;
                    balance = amount - total_paid;
                    interest = balance * rate / 12;
                    principal = total - interest;
                    total_paid += principal;
                } else if (type == loanType.LINEAR) {
                    principal = (double) amount / term;
                    if (month >= (delay_from + delay_duration)) {
                        interest = (amount - (month - delay_duration - 1) * ((double) amount / term)) * (rate / 12);

                    } else {
                        interest = (amount - (month - 1) * ((double) amount / term)) * (rate / 12);
                    }
                    balance = amount - total_paid;
                    total = principal + interest;
                    total_paid += principal;
                }
            }

            sendMonthlyPayment(month, balance, interest, principal, total);
        }
    }

    private void sendMonthlyPayment(int month, double balance, double interest, double principal, double total) {
        loanEntry loan_entry = new loanEntry();

        loan_entry.setCurrent_month(month);
        loan_entry.setPrincipal(Math.round(principal * 100.0) / 100.0);
        loan_entry.setInterest(Math.round(interest * 100.0) / 100.0);
        loan_entry.setTotal(Math.round(total * 100.0) / 100.0);
        loan_entry.setBalance(Math.round(balance * 100.0) / 100.0);

        monthly_payments.addMonthlyPayment(loan_entry);
    }

    public ObservableList<loanEntry> getMonthlyPayments() {
        return monthly_payments.getMonthly_payments();
    }
}
