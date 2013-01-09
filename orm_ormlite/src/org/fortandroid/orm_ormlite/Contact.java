package org.fortandroid.orm_ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: dwalker
 * Date: 1/7/13
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "contacts")
public class Contact {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String name;

    @ForeignCollectionField(eager = false, foreignFieldName = "contact")
    private Collection<Address> addresses;

    /**
     * ORMLite requires a no-arg constructor with at-least package visibility
     */
    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<Address> addresses) {
        this.addresses = addresses;
    }
}
