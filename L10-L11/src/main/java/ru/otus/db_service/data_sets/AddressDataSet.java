package ru.otus.db_service.data_sets;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represents address entity.
 */
@Entity
@Table(name = "addresses")
public class AddressDataSet extends DataSet {
    private String street;
    private int index;

    public AddressDataSet() {}

    public AddressDataSet(String street, int index) {
        this.street = street;
        this.index = index;
    }

    public String getStreet() {return street;}
    public void setStreet(String street) {this.street = street;}

    public int getIndex() {return index;}
    public void setIndex(int index) {this.index = index;}

    @Override
    public String toString() {
        return street + ", " + index;
    }
}
