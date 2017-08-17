package ru.otus.message_system.process_runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessRunnerImpl implements ProcessRunner {
    private final StringBuffer out = new StringBuffer();
    private Process process;

    @Override
    public void start(String command) throws IOException {
        process = runProcess(command);
    }

    @Override
    public void stop() {
        process.destroy();
    }

    @Override
    public String getOutput() {
        return out.toString();
    }

    private Process runProcess(String command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        Process p = pb.start();

        StreamListener errors = new StreamListener(p.getErrorStream(), "ERROR");
        StreamListener output = new StreamListener(p.getInputStream(), "OUTPUT");

        output.start();
        errors.start();
        return p;
    }

    private class StreamListener extends Thread {
        private final Logger logger = Logger.getLogger(StreamListener.class.getName());

        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    out.append(type).append('>').append(line).append('\n');
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
