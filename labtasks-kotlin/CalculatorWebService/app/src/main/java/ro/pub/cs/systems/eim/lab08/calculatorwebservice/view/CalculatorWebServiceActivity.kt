package ro.pub.cs.systems.eim.lab08.calculatorwebservice.view

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.R
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.network.CalculatorWebServiceAsyncTask

class CalculatorWebServiceActivity : AppCompatActivity() {

    private lateinit var operator1EditText: EditText
    private lateinit var operator2EditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var operationSpinner: Spinner
    private lateinit var methodSpinner: Spinner

    private val displayResultButtonClickListener = object : View.OnClickListener {
        override fun onClick(view: View) {
            val operator1 = operator1EditText.text.toString()
            val operator2 = operator2EditText.text.toString()
            val operation = operationSpinner.selectedItem.toString()
            val method = methodSpinner.selectedItemPosition.toString()

            val calculatorWebServiceAsyncTask = CalculatorWebServiceAsyncTask(resultTextView)
            calculatorWebServiceAsyncTask.execute(operator1, operator2, operation, method)
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
    }
}

