package ru.otus.db_service.data_sets;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represents phone entity.
 */
@Entity
@Table(name = "phones")
public class PhoneDataSet extends DataSet {
    private int code;
    private String number;

    public PhoneDataSet() {}

    public PhoneDataSet(int code, String number) {
        this.code = code;
        this.number = number;
    }

    public int getCode() {return code;}
    public void setCode(int code) {this.code = code;}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}

    @Override
    public String toString() {
        return "(" + code + ")" + number;
    }
}
