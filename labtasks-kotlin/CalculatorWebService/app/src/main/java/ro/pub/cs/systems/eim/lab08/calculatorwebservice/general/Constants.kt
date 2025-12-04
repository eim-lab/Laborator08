package ro.pub.cs.systems.eim.lab08.calculatorwebservice.general

object Constants {

    const val DEBUG = true

    const val TAG = "[CalculatorWebService]"

    const val GET_WEB_SERVICE_ADDRESS = "https://wi-fi.cs.pub.ro/expr/expr_get.php"
    const val POST_WEB_SERVICE_ADDRESS = "https://wi-fi.cs.pub.ro/expr/expr_post.php"

    const val ERROR_MESSAGE_EMPTY = "Operator fields cannot be empty!"

    const val GET_OPERATION = 0
    const val POST_OPERATION = 1

    const val OPERATION_ATTRIBUTE = "operation"
    const val OPERATOR1_ATTRIBUTE = "t1"
    const val OPERATOR2_ATTRIBUTE = "t2"
}

