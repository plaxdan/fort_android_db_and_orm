package org.fortandroid.db_from_script;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

public class MyActivity extends Activity {

    private static String TAG = "MyActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        testDatabase();
    }

    private void testDatabase() {
        Cursor contacts = null;
        Cursor addresses = null;
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.populateDatabaseFromScript();
            if (dbHelper.open()) {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
                SQLiteDatabase database = dbHelper.getReadableDatabase();
                contacts = database.rawQuery("select * from contacts", null);
                if (contacts.getCount() > 0) {
                    linearLayout.addView(textView("CONTACTS..."));
                    while (contacts.moveToNext()) {
                        int id      = contacts.getInt(0);
                        String name = contacts.getString(1);
                        Log.i(TAG, String.format("ID[%s], Name[%s]", id, name));
                        linearLayout.addView(textView(String.format("%s: %s", id, name)));
                    }
                }
                addresses = database.rawQuery("select * from addresses", null);
                if (addresses.getCount() > 0) {
                    linearLayout.addView(textView("\nADDRESSES..."));
                    while (addresses.moveToNext()) {
                        int id          = addresses.getInt(0);
                        int contact_id  = addresses.getInt(1);
                        String addr1    = addresses.getString(2);
                        String addr2    = addresses.getString(3);
                        String city     = addresses.getString(4);
                        String state    = addresses.getString(5);
                        String zip      = addresses.getString(6);
                        linearLayout.addView(textView(String.format("ID: %s:\nContact ID:%s\n%s,\n%s,\n%s,\n%s,\n%s", id, contact_id, addr1, addr2, city, state, zip)));
                    }
                }
            }
        } catch (IOException ioe) {
            throw new Error(ioe.getMessage(), ioe);
        } finally {
            if (contacts != null) {
                contacts.close();
            }
            if (addresses != null) {
                addresses.close();
            }
            dbHelper.close();
        }
    }

    private TextView textView(String text) {
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText(text);
        return tv;
    }
}
