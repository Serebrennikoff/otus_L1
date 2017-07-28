package ru.otus.frontend;

import org.eclipse.jetty.websocket.api.Session;
import org.hibernate.stat.Statistics;
import ru.otus.message_system.Address;

public class MsgGetCacheStatsAnswer extends MsgToFrontend {
    private final Statistics stats;
    private final Session session;


    public MsgGetCacheStatsAnswer(Address from,
                                  Address to,
                                  Statistics stats,
                                  Session session) {
        super(from, to);
        this.stats = stats;
        this.session = session;
    }

    @Override
    public void exec(DBWorkSimulator dbWorkSimulator) {
        dbWorkSimulator.publishCacheStats(session, stats);
    }
}
