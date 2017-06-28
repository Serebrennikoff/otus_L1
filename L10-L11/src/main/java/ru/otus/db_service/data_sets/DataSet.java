package ru.otus.db_service.data_sets;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Parent class for data sets.
 */
@MappedSuperclass
public class DataSet {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private long id;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
}
