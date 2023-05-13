package com.calculator.mortgage.mortgage_calculator;

import com.calculator.mortgage.data.*;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class Controller implements Initializable {
    @FXML
    private Label loan_invalid_input, delay_invalid_input;

    @FXML
    private TableView<loanEntry> loan_table;

    @FXML
    private TableColumn<loanEntry, Integer> month;

    @FXML
    private TableColumn<loanEntry, Double> principal, interest, total, balance;

    @FXML
    private TextField amount, rate, term_years, delay_rate, term_months, delay_duration, delay_from, filter_from, filter_to;

    @FXML
    private Button loan_calculate, delay_calculate, export, filter_apply, filter_reset;

    @FXML
    private RadioButton annuity, linear;

    @FXML
    private CheckBox delay;

    @FXML
    private LineChart<Integer, Double> payment_chart;

    loanCalculator calculator = new loanCalculator();

    @FXML
    public void loanCalculate(ActionEvent actionEvent) {
        loan_invalid_input.setVisible(false);
        loan_table.getItems().clear();

        try {
            int amount_int = Integer.parseInt(amount.getText());
            double rate_int = Double.parseDouble(rate.getText());
            int term_y_int = Integer.parseInt(term_years.getText());
            int term_m_int = Integer.parseInt(term_months.getText());

            loanType loan_type = loanType.LINEAR;
            if (annuity.isSelected()) {
                loan_type = loanType.ANNUITY;
            }

            if (((term_y_int <= 0) && (term_m_int <= 0)) || amount_int <= 0 || rate_int < 0) {
                throw new NumberFormatException();
            }

            calculator.loanCalculate(amount_int, rate_int / 100, term_y_int * 12 + term_m_int, loan_type);
        } catch (NumberFormatException e) {
            loan_invalid_input.setVisible(true);
        }
        loan_table.setItems(calculator.getMonthlyPayments());

        showChart();
    }

    @FXML
    public void delayCalculate(ActionEvent actionEvent) {
        loan_invalid_input.setVisible(false);
        loan_table.getItems().clear();

        try {
            int amount_int = Integer.parseInt(amount.getText());
            double rate_int = Double.parseDouble(rate.getText());
            int term_y_int = Integer.parseInt(term_years.getText());
            int term_m_int = Integer.parseInt(term_months.getText());

            loanType loan_type = loanType.LINEAR;
            if (annuity.isSelected()) {
                loan_type = loanType.ANNUITY;
            }

            int delay_from_int = Integer.parseInt(delay_from.getText());
            int delay_duration_int = Integer.parseInt(delay_duration.getText());
            double delay_rate_int = Double.parseDouble(delay_rate.getText());

            if (((term_y_int <= 0) && (term_m_int <= 0)) || amount_int <= 0 || rate_int < 0 || delay_from_int < 1 || delay_from_int > (term_y_int * 12 + term_m_int) || delay_duration_int <= 0 || delay_rate_int < 0) {
                throw new NumberFormatException();
            }

            calculator.loanCalculate(amount_int, rate_int / 100, term_y_int * 12 + term_m_int, loan_type, delay_from_int, delay_duration_int, delay_rate_int);
        } catch (NumberFormatException e) {
            delay_invalid_input.setVisible(true);
        }

        loan_table.setItems(calculator.getMonthlyPayments());

        showChart();
    }

    private void showChart() {
        payment_chart.getData().clear();
        XYChart.Series<Integer, Double> series = new XYChart.Series<>();
        for (loanEntry entry : calculator.getMonthlyPayments()) {
            series.getData().add(new XYChart.Data<Integer, Double>(entry.getCurrent_month(), entry.getTotal()));
        }
        series.setName("Monthly payment");
        payment_chart.getData().addAll(series);
    }

    @FXML
    public void enableDelay(ActionEvent actionEvent) {
        delay_from.setDisable(!delay_from.isDisable());
        delay_duration.setDisable(!delay_calculate.isDisable());
        delay_rate.setDisable(!delay_rate.isDisable());
        delay_calculate.setDisable(!delay_calculate.isDisable());

    }

    @FXML
    public void exportToFile(ActionEvent actionEvent) {
        FileChooser file_chooser = new FileChooser();
        file_chooser.setInitialFileName("mortgage_payments.csv");
        Stage stage = (Stage) amount.getScene().getWindow();
        File file = file_chooser.showSaveDialog(stage);

        if (file == null) {
            return;
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(stream);
            writer.println("Month,Principal,Interest,Total,Balance");

            for (loanEntry entry : calculator.getMonthlyPayments()) {
                writer.println(entry.getCurrent_month() + "," + entry.getPrincipal() + "," + entry.getInterest() + "," + entry.getTotal() + "," + entry.getBalance());
            }

            writer.flush();
            stream.close();
        } catch (IOException e) {
            return;
        }
    }

    @FXML
    public void applyFilter(ActionEvent actionEvent) {
        if (calculator.getMonthlyPayments() == null) {
            return;
        }
        int from, to;
        try {
            from = Integer.parseInt(filter_from.getText());
            to = Integer.parseInt(filter_to.getText());
        } catch (NumberFormatException e) {
            resetFilter(actionEvent);
            return;
        }

        FilteredList<loanEntry> filteredList = new FilteredList<>(calculator.getMonthlyPayments());

        loan_table.setItems(filteredList);

        filteredList.setPredicate(new Predicate<loanEntry>() {
            public boolean test(loanEntry t) {
                return (t.getCurrent_month() >= from) && (t.getCurrent_month() <= to);
            }
        });
    }

    @FXML
    public void resetFilter(ActionEvent actionEvent) {
        loan_table.setItems(calculator.getMonthlyPayments());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        month.setCellValueFactory(new PropertyValueFactory<loanEntry, Integer>("current_month"));
        principal.setCellValueFactory(new PropertyValueFactory<loanEntry, Double>("principal"));
        interest.setCellValueFactory(new PropertyValueFactory<loanEntry, Double>("interest"));
        total.setCellValueFactory(new PropertyValueFactory<loanEntry, Double>("total"));
        balance.setCellValueFactory(new PropertyValueFactory<loanEntry, Double>("balance"));
    }
}