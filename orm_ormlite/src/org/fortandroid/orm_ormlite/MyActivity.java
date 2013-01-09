package org.fortandroid.orm_ormlite;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MyActivity extends Activity {

    private static String TAG = "MyActivity";
    private DatabaseHelper dbHelper;
    private Dao<Contact, Integer> contactDao;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initDb();
        initDaos();
        try {
            displayContactsWithTheirAddresses(contactDao.queryForAll());
        } catch (SQLException sqle) {
            throw new Error(sqle.getMessage(), sqle);
        }
        initListeners();
    }

    private void initListeners() {
        Button searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText)findViewById(R.id.searchText);
                Editable editable = searchText.getText();
                Log.i(TAG, "Editable length: " + editable.length());
                List<Contact> searchResults = null;
                try {
                    if (editable.length() > 0) {
                        searchResults = contactDao.queryForEq("name", editable.toString());
                    } else {
                        // No search criteria entered, display all
                        searchResults = contactDao.queryForAll();
                    }
                } catch(SQLException sqle) {
                    throw new Error(sqle.getMessage(), sqle);
                }
                displayContactsWithTheirAddresses(searchResults);
            }
        });
    }

    private void initDb() {
        this.dbHelper = new DatabaseHelper(this);
        try {
            if (!dbHelper.open()) {
                this.dbHelper.populateDatabaseFromScript();
            }
        } catch (IOException e) {
            throw new Error(e.getMessage(), e);
        }
    }

    private void initDaos() {
        try {
            ConnectionSource connSrc = new AndroidConnectionSource(dbHelper);
            contactDao = DaoManager.createDao(connSrc, Contact.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayContactsWithTheirAddresses(List<Contact> contacts) {
        // TODO: clear children of layout containing search results
        LinearLayout searchResultsLayout = (LinearLayout)findViewById(R.id.searchResults);
        searchResultsLayout.removeAllViews();   // Clear the currently displayed contacts
        if (contacts != null && contacts.size() > 0) {

            // Display the contacts
            searchResultsLayout.addView(textView("CONTACTS..."));
            for (Contact contact : contacts) {
                searchResultsLayout.addView(textView(String.format("%s: %s", contact.getId(), contact.getName())));

                // For each contact, display the addresses
                Collection<Address> addresses = contact.getAddresses(); // Get the Addresses from the Contact
                if (addresses != null && addresses.size() > 0) {
                    searchResultsLayout.addView(textView("\tADDRESSES..."));
                    for (Address address : addresses) {
                        searchResultsLayout.addView(textView(String.format(
                                "\tID: %s:\n\tAddr1: %s\n\tAddr2: %s,\n\tCity: %s,\n\tState: %s,\n\tZip: %s",
                                address.getId(),
                                address.getAddr1(),
                                address.getAddr2(),
                                address.getCity(),
                                address.getState(),
                                address.getZip())));
                    }
                }
            }

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
