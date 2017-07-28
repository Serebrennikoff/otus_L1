package ru.otus.message_system;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public final class MessageSystem {
    private final Map<Address, ArrayBlockingQueue<Message>> messagesMap = new HashMap<>();
    private final Map<Address, Addressee> addressMap = new HashMap<>();

    public void addAddressee(Addressee addressee) {
        addressMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new ArrayBlockingQueue<>(10));
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).add(message);
    }

    public void start() {
        for (Map.Entry<Address, Addressee> entry : addressMap.entrySet()) {
            new Thread(() -> {
               while (true) {
                   ArrayBlockingQueue<Message> queue = messagesMap.get(entry.getKey());
                   try {
                       Message message = queue.take();
                       message.exec(entry.getValue());
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }).start();
        }
    }
}
