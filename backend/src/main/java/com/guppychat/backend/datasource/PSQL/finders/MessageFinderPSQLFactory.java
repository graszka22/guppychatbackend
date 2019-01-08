package com.guppychat.backend.datasource.PSQL.finders;

import com.guppychat.backend.datasource.finders.MessageFinder;
import com.guppychat.backend.datasource.finders.MessageFinderFactory;

public class MessageFinderPSQLFactory implements MessageFinderFactory {
    @Override
    public MessageFinder create() {
        return new MessageFinderPSQL();
    }
}
