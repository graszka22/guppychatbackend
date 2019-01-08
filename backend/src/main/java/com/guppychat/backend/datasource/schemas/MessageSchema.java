package com.guppychat.backend.datasource.schemas;

import annotationprocessor.Schema;

import java.sql.Timestamp;

@Schema(tableName = "message")
public class MessageSchema {
    int senderId;
    int receiverId;
    String text;
    Timestamp dateSent;
}
