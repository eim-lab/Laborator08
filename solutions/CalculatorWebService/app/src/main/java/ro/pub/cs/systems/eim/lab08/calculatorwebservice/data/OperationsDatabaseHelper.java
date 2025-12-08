package ro.pub.cs.systems.eim.lab08.calculatorwebservice.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants;

public class OperationsDatabaseHelper extends SQLiteOpenHelper {

    public OperationsDatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + Constants.TABLE_NAME + " (" +
                Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.COLUMN_OPERATOR1 + " TEXT NOT NULL, " +
                Constants.COLUMN_OPERATOR2 + " TEXT NOT NULL, " +
                Constants.COLUMN_OPERATION + " TEXT NOT NULL, " +
                Constants.COLUMN_METHOD + " TEXT NOT NULL, " +
                Constants.COLUMN_RESULT + " TEXT NOT NULL, " +
                Constants.COLUMN_TIMESTAMP + " INTEGER NOT NULL" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    public long insertOperation(String operator1, String operator2, String operation, String method, String result) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_OPERATOR1, operator1);
        values.put(Constants.COLUMN_OPERATOR2, operator2);
        values.put(Constants.COLUMN_OPERATION, operation);
        values.put(Constants.COLUMN_METHOD, method);
        values.put(Constants.COLUMN_RESULT, result);
        values.put(Constants.COLUMN_TIMESTAMP, System.currentTimeMillis());
        return db.insert(Constants.TABLE_NAME, null, values);
    }

    public List<OperationRecord> getAllOperations() {
        List<OperationRecord> operations = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                Constants.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Constants.COLUMN_TIMESTAMP + " DESC"
        );

        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(Constants.COLUMN_ID);
            int operator1Index = cursor.getColumnIndex(Constants.COLUMN_OPERATOR1);
            int operator2Index = cursor.getColumnIndex(Constants.COLUMN_OPERATOR2);
            int operationIndex = cursor.getColumnIndex(Constants.COLUMN_OPERATION);
            int methodIndex = cursor.getColumnIndex(Constants.COLUMN_METHOD);
            int resultIndex = cursor.getColumnIndex(Constants.COLUMN_RESULT);
            int timestampIndex = cursor.getColumnIndex(Constants.COLUMN_TIMESTAMP);

            while (cursor.moveToNext()) {
                operations.add(new OperationRecord(
                        cursor.getLong(idIndex),
                        cursor.getString(operator1Index),
                        cursor.getString(operator2Index),
                        cursor.getString(operationIndex),
                        cursor.getString(methodIndex),
                        cursor.getString(resultIndex),
                        cursor.getLong(timestampIndex)
                ));
            }
            cursor.close();
        }
        return operations;
    }
}

