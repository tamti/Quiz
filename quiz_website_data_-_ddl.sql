create database quiz_website;
use quiz_website;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT,
    nickname VARCHAR(20) NOT NULL,
    password VARCHAR(40) NOT NULL,
    is_admin BOOLEAN NOT NULL,
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
    quiz_author_id INT,
    date_created DATETIME NOT NULL,
    quiz_file VARCHAR(30) NOT NULL,
    num_questions INT NOT NULL,
    max_points INT NOT NULL,
    CONSTRAINT quizes_pk PRIMARY KEY (quiz_id),
    CONSTRAINT quizes_fk FOREIGN KEY (quiz_author_id)
        REFERENCES users (user_id)
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

CREATE TABLE messages (
    message_id INT AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_type ENUM('friend_request', 'challange', 'note'),
    time_sent DATETIME NOT NULL,
    text VARCHAR(100) NOT NULL,
    CONSTRAINT messages_pk PRIMARY KEY (message_id),
	constraint messages_fk1 foreign key (sender_id) references users (user_id),
	constraint messages_fk2 foreign key (receiver_id) references users (user_id)
);

CREATE TABLE announcements (
    announcement_id INT AUTO_INCREMENT,
    author_id INT NOT NULL,
    time_announced DATETIME NOT NULL,
    text VARCHAR(200) NOT NULL,
    CONSTRAINT announcements_pk PRIMARY KEY (announcement_id),
    CONSTRAINT announcements_fk FOREIGN KEY (author_id)
        REFERENCES users (user_id)
);

CREATE TABLE achievements (
    achievement_id INT AUTO_INCREMENT,
    achievement_name VARCHAR(40) NOT NULL,
    picture_file VARCHAR(30),
    CONSTRAINT achievements_pk PRIMARY KEY (achievement_id)
);

CREATE TABLE user_achievements (
    user_achievement_id INT AUTO_INCREMENT,
    achievement_id INT NOT NULL,
    user_id INT NOT NULL,
    time_received DATETIME NOT NULL,
    CONSTRAINT user_achievements_pk PRIMARY KEY (user_achievement_id),
    CONSTRAINT user_achievements_fk1 FOREIGN KEY (achievement_id)
        REFERENCES achievements (achievement_id),
    CONSTRAINT user_achievements_fk2 FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);