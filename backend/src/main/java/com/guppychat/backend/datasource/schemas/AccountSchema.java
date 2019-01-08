package com.guppychat.backend.datasource.schemas;

import annotationprocessor.Schema;

@Schema(tableName = "account")
public class AccountSchema {
    String username;
    String password;
    String email;
    String sessionToken;
}
