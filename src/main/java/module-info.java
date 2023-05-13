module com.calculator.mortgage.mortgage_calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    opens com.calculator.mortgage.mortgage_calculator to javafx.fxml, javafx.graphics, javafx.base;
    opens com.calculator.mortgage.data to  javafx.fxml, javafx.graphics, javafx.base;
    exports com.calculator.mortgage.mortgage_calculator;
    exports com.calculator.mortgage.data;
}