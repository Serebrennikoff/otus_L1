package ru.otus.l91.entities.user;

import javax.persistence.*;

/**
 * Represents User entity.
 */

@Entity
@Table(name = "USERS")
@Access(AccessType.FIELD)
public class User {

    @Id @Transient
    private long id;
    @Column(name = "USER_NAME")
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getAge() {return age;}
    public void setAge(int age) {this.age = age;}
}
