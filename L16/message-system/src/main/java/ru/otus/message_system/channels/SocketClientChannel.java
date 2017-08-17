package ru.otus.message_system.channels;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.otus.message_system.messages.Msg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientChannel implements MsgChannel {
    private static final Logger logger = Logger.getLogger(SocketClientChannel.class.getName());
    private static final int THREADS_NUM = 2;

    private final BlockingQueue<Msg> outcomings = new LinkedBlockingQueue<>();
    private final BlockingQueue<Msg> incomings = new LinkedBlockingQueue<>();

    private final ExecutorService executor;

    private final Socket client;

    public SocketClientChannel(Socket client) {
        this.client = client;
        this.executor = Executors.newFixedThreadPool(THREADS_NUM);
    }

    public void init() {
        executor.execute(this::sendMsg);
        executor.execute(this::receiveMsg);
    }

    private void receiveMsg() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Msg msg = getMsgFromJSON(inputLine);
                incomings.add(msg);
            }
        } catch (IOException | ParseException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, e.toString() + " " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private void sendMsg() {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            while (client.isConnected()) {
                Msg msg = outcomings.take();
                String json = new Gson().toJson(msg);
                out.println(json);
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.toString() + " " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private Msg getMsgFromJSON(String json) throws ParseException, ClassNotFoundException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        String className = (String) jsonObject.get(Msg.CLASS_NAME_FIELD);
        Class<?> msgType = Class.forName(className);
        return (Msg) new Gson().fromJson(json, msgType);
    }

    @Override
    public void send(Msg msg) {
        outcomings.add(msg);
    }

    @Override
    public Msg pool() {
        return incomings.poll();
    }

    @Override
    public Msg take() throws InterruptedException {return incomings.take();}

    @Override
    public Msg retrieveOutcomingMsg() {return outcomings.poll();}

    @Override
    public boolean wasClosed() {return client.isClosed();}

    @Override
    public int getLocalPort() {return client.getLocalPort();}

    @Override
    public void close() throws IOException {executor.shutdown();}
}
