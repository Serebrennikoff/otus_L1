package ru.otus.message_system.messages.front_to_db.payload_objects;

import java.util.HashSet;
import java.util.Set;

public class UserDSPayload {
    private final long id;
    private final String name;
    private final String[] address;
    private final Set<String[]> phones;

    private UserDSPayload(long id, String name, String[] address, Set<String[]> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public static class PayloadBuilder {
        private long id = -1;
        private String name;
        private String[] address;
        private Set<String[]> phones;

        public PayloadBuilder() {
            address = new String[2];
            phones = new HashSet<>();
        }

        public PayloadBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public PayloadBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PayloadBuilder setAddress(String street, int index) {
            address[0] = street;
            address[1] = Integer.toString(index);
            return this;
        }

        public PayloadBuilder addPhone(int code, String number) {
            phones.add(new String[]{Integer.toString(code), number});
            return this;
        }

        public UserDSPayload build() {
            if (name == null || address[0] == null || phones.isEmpty())
                throw new RuntimeException("Data set payload was not properly built.");
            return new UserDSPayload(id, name, address, phones);
        }
    }

    public long getId() {return id;}

    public String getName() {return name;}

    public String[] getAddress() {return address;}

    public Set<String[]> getPhones() {return phones;}
}
