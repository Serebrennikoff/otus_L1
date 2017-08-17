package ru.otus.message_system;

import ru.otus.message_system.channels.SocketClientChannel;
import ru.otus.message_system.message_server.MessageServer;
import ru.otus.message_system.messages.Msg;
import ru.otus.message_system.messages.front_to_db.MsgGetUserByIdAndReturnCacheStats;
import ru.otus.message_system.messages.front_to_db.MsgPopulateDB;
import ru.otus.message_system.messages.front_to_db.payload_objects.UserDSPayload;
import ru.otus.message_system.messages.front_to_db.payload_objects.UserIdPayload;
import ru.otus.message_system.messages.identification.MsgIdentifyService;

import java.net.Socket;


public class Client {
    private final static String HOST = "localhost";
    private final static int PORT = 8081;

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(HOST, PORT);
        System.out.println("Socket created. Local port: " + socket.getLocalPort());

        SocketClientChannel client = new SocketClientChannel(socket);
        client.init();
        client.take();
        client.send(new MsgIdentifyService(MessageServer.ServiceType.FRONTEND));

        UserDSPayload payload = new UserDSPayload.PayloadBuilder()
                .setName("Peter")
                .setAddress("Baker str.", 149304)
                .addPhone(321, "930-292")
                .addPhone(324, "828-922")
                .build();

        MsgPopulateDB msg = new MsgPopulateDB(socket.getLocalPort(), payload);
        client.send(msg);

        payload = new UserDSPayload.PayloadBuilder()
                .setName("Mark")
                .setAddress("Himark str.", 125604)
                .addPhone(324, "228-222")
                .build();

        msg = new MsgPopulateDB(socket.getLocalPort(), payload);
        client.send(msg);

        for (int i = 0; i < 2; i++) {
            client.send(new MsgGetUserByIdAndReturnCacheStats(socket.getLocalPort(), new UserIdPayload(1)));
            Msg msg1 = client.take();
            System.out.println(msg1.getPayload());

            Thread.sleep(3000);
        }

    }

}
