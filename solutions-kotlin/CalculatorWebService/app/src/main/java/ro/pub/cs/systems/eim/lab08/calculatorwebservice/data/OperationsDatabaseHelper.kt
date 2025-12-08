package ro.pub.cs.systems.eim.lab08.calculatorwebservice.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants

class OperationsDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    Constants.DATABASE_NAME,
    null,
    Constants.DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE ${Constants.TABLE_NAME} (
                ${Constants.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Constants.COLUMN_OPERATOR1} TEXT NOT NULL,
                ${Constants.COLUMN_OPERATOR2} TEXT NOT NULL,
                ${Constants.COLUMN_OPERATION} TEXT NOT NULL,
                ${Constants.COLUMN_METHOD} TEXT NOT NULL,
                ${Constants.COLUMN_RESULT} TEXT NOT NULL,
                ${Constants.COLUMN_TIMESTAMP} INTEGER NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${Constants.TABLE_NAME}")
        onCreate(db)
    }

    fun insertOperation(
        operator1: String,
        operator2: String,
        operation: String,
        method: String,
        result: String
    ): Long {
        val db = writableDatabase
        val values = android.content.ContentValues().apply {
            put(Constants.COLUMN_OPERATOR1, operator1)
            put(Constants.COLUMN_OPERATOR2, operator2)
            put(Constants.COLUMN_OPERATION, operation)
            put(Constants.COLUMN_METHOD, method)
            put(Constants.COLUMN_RESULT, result)
            put(Constants.COLUMN_TIMESTAMP, System.currentTimeMillis())
        }
        return db.insert(Constants.TABLE_NAME, null, values)
    }

    fun getAllOperations(): List<OperationRecord> {
        val operations = mutableListOf<OperationRecord>()
        val db = readableDatabase
        val cursor = db.query(
            Constants.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "${Constants.COLUMN_TIMESTAMP} DESC"
        )

        cursor.use {
            val idIndex = it.getColumnIndex(Constants.COLUMN_ID)
            val operator1Index = it.getColumnIndex(Constants.COLUMN_OPERATOR1)
            val operator2Index = it.getColumnIndex(Constants.COLUMN_OPERATOR2)
            val operationIndex = it.getColumnIndex(Constants.COLUMN_OPERATION)
            val methodIndex = it.getColumnIndex(Constants.COLUMN_METHOD)
            val resultIndex = it.getColumnIndex(Constants.COLUMN_RESULT)
            val timestampIndex = it.getColumnIndex(Constants.COLUMN_TIMESTAMP)

            while (it.moveToNext()) {
                operations.add(
                    OperationRecord(
                        id = it.getLong(idIndex),
                        operator1 = it.getString(operator1Index),
                        operator2 = it.getString(operator2Index),
                        operation = it.getString(operationIndex),
                        method = it.getString(methodIndex),
                        result = it.getString(resultIndex),
                        timestamp = it.getLong(timestampIndex)
                    )
                )
            }
        }
        return operations
    }
}

data class OperationRecord(
    val id: Long,
    val operator1: String,
    val operator2: String,
    val operation: String,
    val method: String,
    val result: String,
    val timestamp: Long
)

