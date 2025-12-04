package ro.pub.cs.systems.eim.lab08.calculatorwebservice.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab08.calculatorwebservice.R;
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.network.CalculatorWebServiceAsyncTask;
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.network.QuotesAsyncTask;

public class CalculatorWebServiceActivity extends AppCompatActivity {

    private EditText operator1EditText, operator2EditText;
    private TextView resultTextView;
    private Spinner operationSpinner, methodSpinner;
    private TextView quoteTextView;
    private int quoteIndex = 0;

    private final DisplayResultButtonClickListener displayResultButtonClickListener = new DisplayResultButtonClickListener();
    private class DisplayResultButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String operator1 = operator1EditText.getText().toString();
            String operator2 = operator2EditText.getText().toString();
            String operation = operationSpinner.getSelectedItem().toString();
            // The method logic remains the same
            String method = String.valueOf(methodSpinner.getSelectedItemPosition());

            CalculatorWebServiceAsyncTask calculatorWebServiceAsyncTask = new CalculatorWebServiceAsyncTask(resultTextView);
            calculatorWebServiceAsyncTask.execute(operator1, operator2, operation, method);
        }
    }

    private final FetchQuoteButtonClickListener fetchQuoteButtonClickListener = new FetchQuoteButtonClickListener();
    private class FetchQuoteButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            QuotesAsyncTask quotesAsyncTask = new QuotesAsyncTask(quoteTextView);
            quotesAsyncTask.execute(quoteIndex);
            // Increment index for next time, wrap around after 100 quotes
            quoteIndex = (quoteIndex + 1) % 100;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_web_service);

        operator1EditText = findViewById(R.id.operator1_edit_text);
        operator2EditText = findViewById(R.id.operator2_edit_text);
        resultTextView = findViewById(R.id.result_text_view);
        operationSpinner = findViewById(R.id.operation_spinner);
        methodSpinner = findViewById(R.id.method_spinner);
        Button displayResultButton = findViewById(R.id.display_result_button);
        displayResultButton.setOnClickListener(displayResultButtonClickListener);

        quoteTextView = findViewById(R.id.quote_text_view);
        Button fetchQuoteButton = findViewById(R.id.fetch_quote_button);
        fetchQuoteButton.setOnClickListener(fetchQuoteButtonClickListener);
    }
}
