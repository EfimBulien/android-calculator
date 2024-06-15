package com.example.cbo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.ArithmeticException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private EditText editTextResult;
    private boolean lastNumeric;
    private boolean stateError;
    private boolean lastDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextResult = findViewById(R.id.editTextResult);

        // Clear button action
        findViewById(R.id.buttonClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextResult.getText().clear();
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });

        // Number buttons
        int[] numberButtons = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9
        };

        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberClicked(v);
                }
            });
        }

        // Operation buttons
        int[] operationButtons = {
                R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMultiply,
                R.id.buttonDivide, R.id.buttonSqrt, R.id.buttonSquare, R.id.buttonPercent
        };

        for (int id : operationButtons) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    operationClicked(v);
                }
            });
        }

        // Dot button
        findViewById(R.id.buttonDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dotClicked();
            }
        });

        // Equal button
        findViewById(R.id.buttonEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equalClicked();
            }
        });
    }

    private void numberClicked(View view) {
        if (stateError) {
            editTextResult.getText().clear();
            stateError = false;
        }
        Button button = (Button) view;
        editTextResult.append(button.getText());
        lastNumeric = true;
    }

    private void operationClicked(View view) {
        if (lastNumeric && !stateError) {
            Button button = (Button) view;
            editTextResult.append(button.getText());
            lastNumeric = false;
            lastDot = false;
        }
    }

    private void dotClicked() {
        if (lastNumeric && !stateError && !lastDot) {
            editTextResult.append(".");
            lastNumeric = false;
            lastDot = true;
        }
    }

    private void equalClicked() {
        if (lastNumeric && !stateError) {
            String txt = editTextResult.getText().toString();
            try {
                double result = evaluate(txt);
                editTextResult.setText(BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP).toString());
                lastDot = false;
            } catch (ArithmeticException ex) {
                editTextResult.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }

    private double evaluate(String expression) {
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            return Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
        } else if (expression.contains("-")) {
            String[] parts = expression.split("-");
            return Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
        } else if (expression.contains("*")) {
            String[] parts = expression.split("\\*");
            return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
        } else if (expression.contains("/")) {
            String[] parts = expression.split("/");
            if (parts[1].equals("0")) throw new ArithmeticException("Division by zero");
            return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
        } else if (expression.contains("√")) {
            String[] parts = expression.split("√");
            return Math.sqrt(Double.parseDouble(parts[1]));
        } else if (expression.contains("x²")) {
            String[] parts = expression.split("x²");
            return Math.pow(Double.parseDouble(parts[0]), 2);
        } else if (expression.contains("%")) {
            String[] parts = expression.split("%");
            return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]) / 100;
        } else {
            return Double.parseDouble(expression);
        }
    }
}
