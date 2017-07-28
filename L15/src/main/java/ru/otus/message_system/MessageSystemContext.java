package ru.otus.message_system;

public class MessageSystemContext {
    private final MessageSystem messageSystem;

    private Address frontAddress;
    private Address dbAddress;

    public MessageSystemContext(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getFrontAddress() {
        return frontAddress;
    }

    public Address getDbAddress() {
        return dbAddress;
    }

    public void setFrontAddress(Address frontAddress) {
        this.frontAddress = frontAddress;
    }

    public void setDbAddress(Address dbAddress) {
        this.dbAddress = dbAddress;
    }
}
