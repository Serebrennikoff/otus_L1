package ru.otus.db_service;

import ru.otus.message_system.Address;
import ru.otus.message_system.Addressee;
import ru.otus.message_system.Message;

public abstract class MsgToDB extends Message {
    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof  DBService) {
            exec((DBService) addressee);
        }
    }

    public abstract  void exec(DBService dbService);
}
