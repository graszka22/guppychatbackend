package com.guppychat.backend.datasource.PSQL.finders;

import com.guppychat.backend.datasource.finders.FriendsFinder;
import com.guppychat.backend.datasource.finders.FriendsFinderFactory;

public class FriendsFinderPSQLFactory implements FriendsFinderFactory {
    @Override
    public FriendsFinder create() {
        return new FriendsFinderPSQL();
    }
}
