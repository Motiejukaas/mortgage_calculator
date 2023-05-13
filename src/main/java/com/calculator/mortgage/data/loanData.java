package com.calculator.mortgage.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class loanData {
    private ObservableList<loanEntry> monthly_payments = FXCollections.observableArrayList();

    public ObservableList<loanEntry> getMonthly_payments() {
        return monthly_payments;
    }

    public void addMonthlyPayment(loanEntry monthly_payment) {
        monthly_payments.add(monthly_payment);
    }
}
