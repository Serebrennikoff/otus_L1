package ru.otus.db_service;

import ru.otus.db_service.data_sets.UserDataSet;
import ru.otus.message_system.Address;

public class MsgPersistUser extends MsgToDB {
    private final UserDataSet user;

    public MsgPersistUser(Address from, Address to, UserDataSet user) {
        super(from, to);
        this.user = user;
    }

    @Override
    public void exec(DBService dbService) {
        dbService.save(user);
    }
}
