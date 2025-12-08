package ro.pub.cs.systems.eim.lab08.calculatorwebservice.view

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.R
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.data.OperationsDatabaseHelper
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.network.CalculatorWebServiceAsyncTask
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.network.QuotesAsyncTask
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalculatorWebServiceActivity : AppCompatActivity() {

    private lateinit var operator1EditText: EditText
    private lateinit var operator2EditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var operationSpinner: Spinner
    private lateinit var methodSpinner: Spinner
    private lateinit var quoteTextView: TextView
    private var quoteIndex = 0

    private val displayResultButtonClickListener = object : View.OnClickListener {
        override fun onClick(view: View) {
            val operator1 = operator1EditText.text.toString()
            val operator2 = operator2EditText.text.toString()
            val operation = operationSpinner.selectedItem.toString()
            // The method logic remains the same
            val method = methodSpinner.selectedItemPosition.toString()

            val calculatorWebServiceAsyncTask = CalculatorWebServiceAsyncTask(resultTextView, this@CalculatorWebServiceActivity)
            calculatorWebServiceAsyncTask.execute(operator1, operator2, operation, method)
        }
    }

    private val fetchQuoteButtonClickListener = object : View.OnClickListener {
        override fun onClick(view: View) {
            val quotesAsyncTask = QuotesAsyncTask(quoteTextView)
            quotesAsyncTask.execute(quoteIndex)
            // Increment index for next time, wrap around after 100 quotes
            quoteIndex = (quoteIndex + 1) % 100
        }
    }

    private val showHistoryButtonClickListener = object : View.OnClickListener {
        override fun onClick(view: View) {
            val dbHelper = OperationsDatabaseHelper(this@CalculatorWebServiceActivity)
            val operations = dbHelper.getAllOperations()
            
            Log.d(Constants.TAG, "========== OPERATIONS HISTORY ==========")
            if (operations.isEmpty()) {
                Log.d(Constants.TAG, "No operations found in database.")
            } else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                operations.forEachIndexed { index, operation ->
                    val timestamp = dateFormat.format(Date(operation.timestamp))
                    Log.d(Constants.TAG, "Operation #${index + 1}:")
                    Log.d(Constants.TAG, "  Operator 1: ${operation.operator1}")
                    Log.d(Constants.TAG, "  Operator 2: ${operation.operator2}")
                    Log.d(Constants.TAG, "  Operation: ${operation.operation}")
                    Log.d(Constants.TAG, "  Method: ${operation.method}")
                    Log.d(Constants.TAG, "  Result: ${operation.result}")
                    Log.d(Constants.TAG, "  Timestamp: $timestamp")
                    Log.d(Constants.TAG, "  ---")
                }
            }
            Log.d(Constants.TAG, "========================================")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display for API 36
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContentView(R.layout.activity_calculator_web_service)

        // Handle window insets to prevent overlap with system bars
        // Apply insets to the root LinearLayout
        val rootLayout = findViewById<View>(R.id.root_layout)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        operator1EditText = findViewById(R.id.operator1_edit_text)
        operator2EditText = findViewById(R.id.operator2_edit_text)
        resultTextView = findViewById(R.id.result_text_view)
        operationSpinner = findViewById(R.id.operation_spinner)
        methodSpinner = findViewById(R.id.method_spinner)
        val displayResultButton: Button = findViewById(R.id.display_result_button)
        displayResultButton.setOnClickListener(displayResultButtonClickListener)
        
        quoteTextView = findViewById(R.id.quote_text_view)
        val fetchQuoteButton: Button = findViewById(R.id.fetch_quote_button)
        fetchQuoteButton.setOnClickListener(fetchQuoteButtonClickListener)
        
        val showHistoryButton: Button = findViewById(R.id.show_history_button)
        showHistoryButton.setOnClickListener(showHistoryButtonClickListener)
    }
}

