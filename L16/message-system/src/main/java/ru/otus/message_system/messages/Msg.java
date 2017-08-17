package ru.otus.message_system.messages;

import com.google.gson.Gson;

public abstract class Msg {
    public static final String CLASS_NAME_FIELD = "className";

    private final String className;
    private final String payload;

    protected Msg(Class<?> klass, String payload) {
        this.className = klass.getName();
        this.payload = payload;
    }

    public String getPayload() {return payload;}
    public String getClassName() {return className;}

    public static <T> String stringifyPayloadObject(T obj) {
        if(obj == null) throw new RuntimeException("Payload object should not be null.");
        return new Gson().toJson(obj);
    }
}
