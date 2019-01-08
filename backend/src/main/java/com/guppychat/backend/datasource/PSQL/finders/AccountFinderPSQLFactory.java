package com.guppychat.backend.datasource.PSQL.finders;

import com.guppychat.backend.datasource.finders.AccountFinder;
import com.guppychat.backend.datasource.finders.AccountFinderFactory;

public class AccountFinderPSQLFactory implements AccountFinderFactory {

    @Override
    public AccountFinder create() {
        return new AccountFinderPSQL();
    }
}
