package org.fortandroid.orm_ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created with IntelliJ IDEA.
 * User: dwalker
 * Date: 1/8/13
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "addresses")
public class Address {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String addr1;

    @DatabaseField
    private String addr2;

    @DatabaseField
    private String city;

    @DatabaseField
    private String state;

    @DatabaseField
    private String zip;

    @DatabaseField(canBeNull = false, foreign = true)
    private Contact contact;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
