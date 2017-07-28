package ru.otus.db_service;

import org.eclipse.jetty.websocket.api.Session;
import org.hibernate.stat.Statistics;
import ru.otus.frontend.MsgGetCacheStatsAnswer;
import ru.otus.message_system.Address;
import ru.otus.message_system.MessageSystem;

public class MsgGetUserByIdAndReturnCacheStats extends MsgToDB {
    private final MessageSystem messageSystem;
    private final long id;
    private final Session session;

    public MsgGetUserByIdAndReturnCacheStats(MessageSystem messageSystem,
                                             Address from,
                                             Address to,
                                             long id,
                                             Session session) {
        super(from, to);
        this.messageSystem = messageSystem;
        this.id = id;
        this.session = session;
    }

    @Override
    public void exec(DBService dbService) {
        dbService.findById(id);
        Statistics stats = dbService.getStatistics();
        messageSystem.sendMessage(new MsgGetCacheStatsAnswer(getTo(), getFrom(), stats, session));
    }
}
