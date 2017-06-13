create database quiz_website;
use quiz_website;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT,
    nickname VARCHAR(20) NOT NULL,
    password VARCHAR(40) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_id)
);

CREATE TABLE friend_lists (
    friendList_id INT AUTO_INCREMENT,
    user1_id INT NOT NULL,
    user2_id INT NOT NULL,
    friendship_active BOOLEAN NOT NULL,
    CONSTRAINT friend_lists_pk PRIMARY KEY (friendList_id),
    CONSTRAINT friend_lists_fk1 FOREIGN KEY (user1_id)
        REFERENCES users (user_id),
    CONSTRAINT friend_lists_fk2 FOREIGN KEY (user2_id)
        REFERENCES users (user_id)
);

CREATE TABLE quizes (
    quiz_id INT AUTO_INCREMENT,
    quiz_file VARCHAR(30) NOT NULL,
    num_questions INT NOT NULL,
    max_points INT NOT NULL,
    CONSTRAINT quizes_pk PRIMARY KEY (quiz_id)
);

CREATE TABLE quiz_stats (
    stat_id INT AUTO_INCREMENT,
    quiz_id INT NOT NULL,
    user_id INT NOT NULL,
    taken_on DATE NOT NULL,
    used_time TIME NOT NULL,
    num_correct_answers INT NOT NULL,
    num_recieved_points INT NOT NULL,
    CONSTRAINT quiz_stats_pk PRIMARY KEY (stat_id),
    CONSTRAINT quiz_stats_fk1 FOREIGN KEY (quiz_id)
        REFERENCES quizes (quiz_id),
    CONSTRAINT quiz_stats_fk2 FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);