package org.fortandroid.db_as_asset;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: dwalker
 * Date: 1/6/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "contacts";
    private static String DB_PATH;

    private final Context context;
    private SQLiteDatabase database;

    /**
     * Constructor.
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        DB_PATH = String.format("/data/data/%s/databases/", context.getPackageName());
        this.context = context;
    }

    /**
     * Creates an Android database as a copy of the databases stored in the assets directory.
     * @throws IOException
     */
    public void createDatabaseIfNotExists() throws IOException {
        if (!databaseExists()) {
            try {
                copyDatabase();
            } catch (IOException ioe) {
                throw new Error(ioe.getMessage(), ioe);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Not implemented
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Not implemented
    }

    /**
     * Opens the database for subsequent reading using the getReadableDatabase() method.
     * @return true of the database was successfully opened, false if not.
     * @throws SQLException
     */
    public boolean open() throws SQLException
    {
        database = SQLiteDatabase.openDatabase(fullPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return database != null;
    }

    @Override
    public synchronized void close()
    {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    private String fullPath() {
        return DB_PATH + DB_NAME;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring a byte stream.
     * */
    private void copyDatabase() throws IOException {
        this.getReadableDatabase(); // Creates an empty db file that we'll write to during the copy.
        this.close();

        InputStream myInput     = context.getAssets().open(DB_NAME);    // Open your local db as the input stream
        String outFileName      = fullPath();                           // Path to the just created empty db
        OutputStream myOutput   = new FileOutputStream(outFileName);    // Open the empty db as the output stream

        // Copy the file
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
        } finally {
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
    }

    /**
     * Checks if the database already exists in Android's /data/data directory
     * @return true if the database already exists, false if not.
     */
    private boolean databaseExists() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database doesn't exist yet.
        } finally {
            if (checkDB != null) {
                checkDB.close();
            }
        }
        return checkDB != null;
    }
}
