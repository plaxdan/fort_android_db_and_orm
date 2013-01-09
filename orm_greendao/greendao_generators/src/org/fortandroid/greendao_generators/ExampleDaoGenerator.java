package org.fortandroid.greendao_generators;

import de.greenrobot.daogenerator.*;

/**
 * Created with IntelliJ IDEA.
 * User: dwalker
 * Date: 1/10/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3, "org.fortandroid.greendao_android.generated_models"); // Package for models / DAOs to be generated

        addContactAndAddress(schema);

        new DaoGenerator().generateAll(schema, "../greendao_android/src/");
    }

    private static void addContactAndAddress(Schema schema) {
        Entity contact = schema.addEntity("Contact");
        contact.addIdProperty();
        contact.addStringProperty("name").notNull();

        Entity address = schema.addEntity("Address");
        address.addIdProperty();
        address.addStringProperty("addr1");
        address.addStringProperty("addr2");
        address.addStringProperty("city");
        Property state = address.addStringProperty("state").getProperty();  // We'll need this to define default sort order later..
        address.addStringProperty("zip");
        Property contactId = address.addLongProperty("contactId")           // Add a mandatory FK field
                .notNull()
                .getProperty();

        // Map both sides of the relationship
        address.addToOne(contact, contactId);
        ToMany contactToAddresses = contact.addToMany(address, contactId);
        contactToAddresses.setName("addresses");
        contactToAddresses.orderDesc(state); // Set the default sort order on the relationship
    }
}
