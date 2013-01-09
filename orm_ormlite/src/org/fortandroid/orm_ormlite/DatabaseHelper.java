package org.fortandroid.orm_ormlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayOutputStream;
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

    private static String TAG = "DatabaseHelper";

    private static final String DB_NAME = "contacts";
    private static String DB_PATH;
    private static final String SCRIPT_NAME = "create_contacts.sql";

    private final Context context;
    private SQLiteDatabase database;

    /**
     * Constructor.
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = String.format("/data/data/%s/databases/", context.getPackageName());
        this.database = getWritableDatabase();  // Open the database as "writeable"
    }

    public int populateDatabaseFromScript() throws IOException {
        byte[] bytes = readAsset();
        String sql = new String(bytes, "UTF-8");
        String[] lines = sql.split(";(\\s)*[\n\r]");
        int count;
        count = executeSqlStatementsInTx(database, lines);
        return count;
    }

    /**
     * Opens the database for subsequent reading using the getReadableDatabase() method.
     * @return true of the database was successfully opened, false if not.
     * @throws android.database.SQLException
     */
    public boolean open() throws SQLException
    {
        database = SQLiteDatabase.openDatabase(fullPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return database != null;
    }

    private String fullPath() {
        return DB_PATH + DB_NAME;
    }

    private int executeSqlStatementsInTx(SQLiteDatabase db, String[] statements) {
        db.beginTransaction();
        try {
            int count = executeSqlStatements(db, statements);
            db.setTransactionSuccessful();
            return count;
        } finally {
            db.endTransaction();
        }
    }

    private int executeSqlStatements(SQLiteDatabase db, String[] statements) {
        int count = 0;
        for (String line : statements) {
            line = line.trim();
            if (line.length() > 0) {
                db.execSQL(line);
                count++;
            }
        }
        return count;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Not implemented
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Not implemented
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    private int copyAllBytes(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        return byteCount;
    }

    private byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copyAllBytes(in, out);
        return out.toByteArray();
    }

    private byte[] readAsset() throws IOException {
        InputStream in = context.getResources().getAssets().open(SCRIPT_NAME);
        try {
            return readAllBytes(in);
        } finally {
            in.close();
        }
    }
}
