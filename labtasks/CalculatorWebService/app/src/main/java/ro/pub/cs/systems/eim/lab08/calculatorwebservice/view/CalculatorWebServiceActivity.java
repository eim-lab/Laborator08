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

public class CalculatorWebServiceActivity extends AppCompatActivity {

    private EditText operator1EditText, operator2EditText;
    private TextView resultTextView;
    private Spinner operationSpinner, methodSpinner;

    private final DisplayResultButtonClickListener displayResultButtonClickListener = new DisplayResultButtonClickListener();
    private class DisplayResultButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String operator1 = operator1EditText.getText().toString();
            String operator2 = operator2EditText.getText().toString();
            String operation = operationSpinner.getSelectedItem().toString();
            String method = String.valueOf(methodSpinner.getSelectedItemPosition());

            CalculatorWebServiceAsyncTask calculatorWebServiceAsyncTask = new CalculatorWebServiceAsyncTask(resultTextView);
            calculatorWebServiceAsyncTask.execute(operator1, operator2, operation, method);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_web_service);

        operator1EditText = (EditText)findViewById(R.id.operator1_edit_text);
        operator2EditText = (EditText)findViewById(R.id.operator2_edit_text);
        resultTextView = (TextView)findViewById(R.id.result_text_view);
        operationSpinner = (Spinner)findViewById(R.id.operation_spinner);
        methodSpinner = (Spinner)findViewById(R.id.method_spinner);
        Button displayResultButton = (Button) findViewById(R.id.display_result_button);
        displayResultButton.setOnClickListener(displayResultButtonClickListener);
    }
}
