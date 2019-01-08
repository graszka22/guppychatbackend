CREATE DATABASE guppychat;
\c guppychat
CREATE TABLE account(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    email VARCHAR(256) UNIQUE NOT NULL,
    session_token CHAR(32)
);
CREATE TABLE message(
    id SERIAL PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    text TEXT NOT NULL,
    date_sent TIMESTAMP NOT NULL,
    CONSTRAINT message_sender_id_fkey FOREIGN KEY (sender_id)
        REFERENCES account (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT message_receiver_id_fkey FOREIGN KEY (receiver_id)
        REFERENCES account (id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE friends(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    CONSTRAINT friends_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES account(id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT friends_friend_id_fkey FOREIGN KEY (friend_id)
        REFERENCES account(id) MATCH SIMPLE
        ON UPDATE CASCADE ON DELETE CASCADE
);