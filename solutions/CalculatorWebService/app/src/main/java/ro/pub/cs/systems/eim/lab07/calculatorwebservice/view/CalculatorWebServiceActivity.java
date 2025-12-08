package ro.pub.cs.systems.eim.lab08.calculatorwebservice.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ro.pub.cs.systems.eim.lab08.calculatorwebservice.R;
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.data.OperationRecord;
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.data.OperationsDatabaseHelper;
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants;
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

            CalculatorWebServiceAsyncTask calculatorWebServiceAsyncTask = new CalculatorWebServiceAsyncTask(resultTextView, CalculatorWebServiceActivity.this);
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

    private final ShowHistoryButtonClickListener showHistoryButtonClickListener = new ShowHistoryButtonClickListener();
    private class ShowHistoryButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            OperationsDatabaseHelper dbHelper = new OperationsDatabaseHelper(CalculatorWebServiceActivity.this);
            List<OperationRecord> operations = dbHelper.getAllOperations();

            Log.d(Constants.TAG, "========== OPERATIONS HISTORY ==========");
            if (operations.isEmpty()) {
                Log.d(Constants.TAG, "No operations found in database.");
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                for (int i = 0; i < operations.size(); i++) {
                    OperationRecord operation = operations.get(i);
                    String timestamp = dateFormat.format(new Date(operation.getTimestamp()));
                    Log.d(Constants.TAG, "Operation #" + (i + 1) + ":");
                    Log.d(Constants.TAG, "  Operator 1: " + operation.getOperator1());
                    Log.d(Constants.TAG, "  Operator 2: " + operation.getOperator2());
                    Log.d(Constants.TAG, "  Operation: " + operation.getOperation());
                    Log.d(Constants.TAG, "  Method: " + operation.getMethod());
                    Log.d(Constants.TAG, "  Result: " + operation.getResult());
                    Log.d(Constants.TAG, "  Timestamp: " + timestamp);
                    Log.d(Constants.TAG, "  ---");
                }
            }
            Log.d(Constants.TAG, "========================================");
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

        Button showHistoryButton = findViewById(R.id.show_history_button);
        showHistoryButton.setOnClickListener(showHistoryButtonClickListener);
    }
}
