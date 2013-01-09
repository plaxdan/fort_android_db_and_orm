package org.fortandroid.greendao_android;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.fortandroid.greendao_android.generated_models.*;
import org.fortandroid.greendao_android.generated_models.ContactDao.Properties;
import org.fortandroid.greendao_android.generated_models.DaoMaster.DevOpenHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyActivity extends Activity {

    private static String TAG = "MyActivity";

    private SQLiteDatabase db;

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ContactDao contactDao;
    private AddressDao addressDao;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initDaos();
        populateDb();
        displayContactsWithTheirAddresses(contactDao.loadAll());
        initListeners();
    }

    private void initDaos() {
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "contacts", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        contactDao = daoSession.getContactDao();
        addressDao = daoSession.getAddressDao();
    }

    private void populateDb() {
        // Make the contacts
        Contact theHive = new Contact(1L, "The Hive");
        Contact oskarBlues = new Contact(2L, "Oskar Blues");
        Contact pinballJones = new Contact(3L, "Pinball Jones");
        contactDao.insertInTx(theHive, oskarBlues, pinballJones);

        // Make the addresses (notice the last constructor param is a FK to the owning Contact)
        Address newTheHiveAddress = new Address( 1L, "117 E. Mountain", "Suite 222", "Fort Collins", "Colorado", "80524", theHive.getId());
        List<Address> newOskarBluesAddresses = Arrays.asList(
          new Address(2L, "1800 Pike Road", null, "Longmont", "Colorado", "80501", oskarBlues.getId()),
          new Address(3L, "303 Main Street", null, "Lyons", "Colorado", "80540", oskarBlues.getId())
        );
        Address newPinballJonesAddress = new Address(4L, "107 Linden Street",null,"Fort Collins","Colorado","80521", pinballJones.getId());

        // Get the current addresses
        List<Address> theHiveAddresses = theHive.getAddresses();            // getAddresses() is called *first* to ensure the collections are cached
        List<Address> oskarBluesAddresses = oskarBlues.getAddresses();      // Failing to do so would result in the addresses being added twice.
        List<Address> pinballJonesAddresses = pinballJones.getAddresses();  // See: http://greendao-orm.com/documentation/relations/ for details

        // Insert and add for the hive
        daoSession.insert(newTheHiveAddress);
        theHiveAddresses.add(newTheHiveAddress);

        // Insert and add for oskar blues
        addressDao.insertInTx(newOskarBluesAddresses);
        oskarBluesAddresses.addAll(newOskarBluesAddresses);

        // Insert and add for pinball jones
        daoSession.insert(newPinballJonesAddress);
        pinballJonesAddresses.add(newPinballJonesAddress);
    }

    private void initListeners() {
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) findViewById(R.id.searchText);
                Editable editable = searchText.getText();
                Log.i(TAG, "Editable length: " + editable.length());
                List<Contact> searchResults = null;
                if (editable.length() > 0) {
                    searchResults = contactDao.queryBuilder().where(Properties.Name.eq(editable.toString())).list();
                } else {
                    // No search criteria entered, display all
                    searchResults = contactDao.loadAll();
                }
                displayContactsWithTheirAddresses(searchResults);
            }
        });
    }

    private void displayContactsWithTheirAddresses(List<Contact> contacts) {
        LinearLayout searchResultsLayout = (LinearLayout) findViewById(R.id.searchResults);
        searchResultsLayout.removeAllViews(); // Clear the currently displayed contacts
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
