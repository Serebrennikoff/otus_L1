package ru.otus.db_service.data_sets;

import javax.persistence.*;
import java.util.*;

/**
 * Represents user entity.
 */
@Entity
@Table(name = "USERS")
public class UserDataSet extends DataSet {
    private String name;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhoneDataSet> phone = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AddressDataSet address;

    public UserDataSet() {}

    public UserDataSet(String name, AddressDataSet address, PhoneDataSet... phones) {
        this.name = name;
        this.address = address;
        Collections.addAll(this.phone, phones);
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public Set<PhoneDataSet> getPhone() {return phone;}

    public AddressDataSet getAddress() {return address;}
    public void setAddress(AddressDataSet address) {this.address = address;}

    @Override
    public String toString() {
        StringBuilder phoneStr = new StringBuilder();
        for (PhoneDataSet phoneDS : phone) {
            phoneStr.append(phoneDS.toString()).append(", ");
        }
        phoneStr.delete(phoneStr.length()-2, phoneStr.length());
        return "UserDataSet {\n" +
                "name : " + name + ",\n" +
                "phone : " + phoneStr.toString() + ",\n" +
                "address : " + address.toString() +
                "\n}";
    }
}
