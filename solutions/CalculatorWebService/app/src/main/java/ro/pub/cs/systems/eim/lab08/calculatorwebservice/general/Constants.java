package ro.pub.cs.systems.eim.lab08.calculatorwebservice.general;

public class Constants {

    final public static boolean DEBUG = true;

    final public static String TAG = "[CalculatorWebService]";

    final public static String GET_WEB_SERVICE_ADDRESS = "https://wi-fi.cs.pub.ro/expr/expr_get.php";
    final public static String POST_WEB_SERVICE_ADDRESS = "https://wi-fi.cs.pub.ro/expr/expr_post.php";

    final public static String ERROR_MESSAGE_EMPTY = "Operator fields cannot be empty!";

    final public static int GET_OPERATION = 0;
    final public static int POST_OPERATION = 1;

    final public static String OPERATION_ATTRIBUTE = "operation";
    final public static String OPERATOR1_ATTRIBUTE = "t1";
    final public static String OPERATOR2_ATTRIBUTE = "t2";

    // Database constants
    final public static String DATABASE_NAME = "calculator_operations.db";
    final public static int DATABASE_VERSION = 1;
    final public static String TABLE_NAME = "operations";
    final public static String COLUMN_ID = "id";
    final public static String COLUMN_OPERATOR1 = "operator1";
    final public static String COLUMN_OPERATOR2 = "operator2";
    final public static String COLUMN_OPERATION = "operation";
    final public static String COLUMN_METHOD = "method";
    final public static String COLUMN_RESULT = "result";
    final public static String COLUMN_TIMESTAMP = "timestamp";

}
