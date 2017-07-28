package ru.otus.frontend;

import ru.otus.message_system.Address;
import ru.otus.message_system.Addressee;
import ru.otus.message_system.Message;

public abstract class MsgToFrontend extends Message {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBWorkSimulator) {
            exec((DBWorkSimulator) addressee);
        }
    }

    public abstract void exec(DBWorkSimulator dbWorkSimulator);
}
