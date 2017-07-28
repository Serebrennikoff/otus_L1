package ru.otus.db_service;

import org.eclipse.jetty.websocket.api.Session;
import org.hibernate.stat.Statistics;
import ru.otus.frontend.MsgGetCacheStatsAnswer;
import ru.otus.message_system.Address;
import ru.otus.message_system.MessageSystem;

public class MsgGetCacheStats extends MsgToDB {
    private final MessageSystem messageSystem;
    private final Session session;

    public MsgGetCacheStats(MessageSystem messageSystem,
                            Address from,
                            Address to,
                            Session session) {
        super(from, to);
        this.messageSystem = messageSystem;
        this.session = session;
    }

    @Override
    public void exec(DBService dbService) {
        Statistics stats = dbService.getStatistics();
        messageSystem.sendMessage(new MsgGetCacheStatsAnswer(getTo(), getFrom(), stats, session));
    }
}
