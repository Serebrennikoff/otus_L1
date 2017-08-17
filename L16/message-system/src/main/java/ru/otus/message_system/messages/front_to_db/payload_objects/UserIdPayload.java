package ru.otus.message_system.messages.front_to_db.payload_objects;

public class UserIdPayload {
    private final long id;

    public UserIdPayload(long id) {this.id = id;}

    public long getId() {return id;}
}
