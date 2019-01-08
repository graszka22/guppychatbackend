package com.guppychat.backend.datasource.schemas;

import annotationprocessor.Schema;

@Schema(tableName = "friends")
public class FriendsSchema {
    int userId;
    int friendId;
}
