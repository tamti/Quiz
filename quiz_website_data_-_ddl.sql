DROP DATABASE IF EXISTS quiz_website;

CREATE DATABASE quiz_website
DEFAULT CHARACTER SET utf8;

USE quiz_website;


-- ---------------------------- TABLES ----------------------------


-- List of names of all photo files avaliable on the website
CREATE TABLE photos (
    photo_id INT NOT NULL,
    photo_file VARCHAR(150) NOT NULL,
    CONSTRAINT photos_pk PRIMARY KEY (photo_id)
);

/*
 * This table stores string values of nicknames and passwords (hash-values)
 * of all users of the website. String values of the column "salt"
 * (which are optional) are used for ensuring the avoidance of repeting
 * the hash values of same passwords of different users. Column "is_active"
 * indicates wether the account has been deactivated. Value of the column "is_admin" 
 * is set to true if the user is an admin. Column photo_id is a reference 
 * to the user's "profile picture"
 */
CREATE TABLE users (
    user_id INT NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(60) NOT NULL,
    salt varchar(20),
	email varchar(50) NOT NULL,
	photo_id int NOT NULL,
	is_active BOOLEAN NOT NULL,
    is_admin BOOLEAN NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_id),
    CONSTRAINT users_fk FOREIGN KEY (photo_id) references photos (photo_id)
);

/*
 * This table Stores pairs of "friends".
 * A new entry is made when one user sends a "friend request"
 * to the other user. Value of the column "awaiting_response" 
 * is set to True until the second user accepts or denies the request.
 * If the request is denied or one of friends removes another
 * from ones "friend list" value of the column "friendship_active" is set to False
 *
 * Users are stored as foreign keys referencing their
 * respective table
 */
CREATE TABLE friend_lists (
    friendList_id INT AUTO_INCREMENT,
    user1_id INT NOT NULL,
    user2_id INT NOT NULL,
    awaiting_response BOOLEAN NOT NULL,
	friendship_active BOOLEAN NOT NULL,
    CONSTRAINT friend_lists_pk PRIMARY KEY (friendList_id),
    CONSTRAINT friend_lists_fk1 FOREIGN KEY (user1_id)
        REFERENCES users (user_id),
    CONSTRAINT friend_lists_fk2 FOREIGN KEY (user2_id)
        REFERENCES users (user_id)
);

/*
 * This table stores string values of the messages sent 
 * by users to eachother, type of the message (It can be either 
 * a "friend request", a "challange" request or just a "note") 
 * and time when the message was sent. As well as IDs of those users.
 *
 * Users are stored as foreign keys referencing their
 * respective table
 */
CREATE TABLE messages (
    message_id INT AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_type ENUM('friend_request', 'challange', 'note'),
    time_sent DATETIME NOT NULL,
    text VARCHAR(1000) NOT NULL,
    CONSTRAINT messages_pk PRIMARY KEY (message_id),
    CONSTRAINT messages_fk1 FOREIGN KEY (sender_id)
        REFERENCES users (user_id),
    CONSTRAINT messages_fk2 FOREIGN KEY (receiver_id)
        REFERENCES users (user_id)
);

/*
 * Stores String values of all announcements,
 * their authors and times when the announcements were made
 * 
 * Authors are stored as foreign keys referencing their
 * respective table ("users")
 */
CREATE TABLE announcements (
    announcement_id INT AUTO_INCREMENT,
    author_id INT NOT NULL,
    time_announced DATETIME NOT NULL,
    text VARCHAR(1000) NOT NULL,
    CONSTRAINT announcements_pk PRIMARY KEY (announcement_id),
    CONSTRAINT announcements_fk FOREIGN KEY (author_id)
        REFERENCES users (user_id)
);

-- Stores String values of all achievements
CREATE TABLE achievements (
    achievement_id INT AUTO_INCREMENT,
    achievement_name VARCHAR(100) NOT NULL,
    CONSTRAINT achievements_pk PRIMARY KEY (achievement_id)
);

/*
 * Stores achievements of users
 *
 * Users and achievements are stored as foreign keys referencing their
 * respective tables
 */
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

/*
 * This table Stores essential information about the quiz:
 * Name of the quiz, id of the author, time of creation,
 * boolean indicating if the correct answers to the 
 * questions of this quiz should be showed immediately or not
 * (true indicates that they should), max allowed time to complete
 * the quiz, max number of points for the quiz.
 *
 * Authors are stored as foreign keys referencing their
 * respective table ("users")
 */
CREATE TABLE quizzes (
    quiz_id INT NOT NULL,
    quiz_name VARCHAR(100),
    quiz_description VARCHAR(1500),
    quiz_author_id INT,
    date_created DATE,
    show_correct_answer_immediately BOOLEAN NOT NULL,
	show_questions_on_one_page BOOlEAN NOT NULL,
	max_allowed_time TIME,
    max_points INT NOT NULL,
    CONSTRAINT quizes_pk PRIMARY KEY (quiz_id),
    CONSTRAINT quizes_fk FOREIGN KEY (quiz_author_id)
        REFERENCES users (user_id)
);

-- String values of all quiz categories
CREATE TABLE all_quiz_categories (
	quiz_category_id INT AUTO_INCREMENT,
	category_name VARCHAR(100) NOT NULL,
	CONSTRAINT all_quiz_categories_pk PRIMARY KEY (quiz_category_id)
);

/*
 * Stores which quizzes belong to which categories
 *
 * Quizzes and quiz categories are stored as reference to their
 * respective tables
 */
CREATE TABLE categories_of_quizzes (
    category_of_quiz_id INT AUTO_INCREMENT,
    quiz_id INT NOT NULL,
    quiz_category_id INT NOT NULL,
    CONSTRAINT categories_of_quizes_pk PRIMARY KEY (category_of_quiz_id),
    CONSTRAINT categories_of_quizes_fk1 FOREIGN KEY (quiz_id)
        REFERENCES quizzes (quiz_id),
    CONSTRAINT categories_of_quizes_fk2 FOREIGN KEY (quiz_category_id)
        REFERENCES all_quiz_categories (quiz_category_id)
);

-- String values of all questions types
CREATE TABLE question_types (
	question_type_id INT NOT NULL,
	question_type_name varchar(50) not null,
	constraint question_types primary key (question_type_id)
);

/*
 * This table Stores string values of all questions. Also idicates
 * their type and max number of points for the question
 *
 * Questions types are stored as foreign keys referencing their
 * respective table
 */
CREATE TABLE questions (
    question_id INT NOT NULL,
    question_txt VARCHAR(2000) NOT NULL,
    question_type_id INT NOT NULL,
    photo_id INT,
    max_points DECIMAL NOT NULL,
    CONSTRAINT questions_pk PRIMARY KEY (question_id),
    CONSTRAINT qyestions_fk FOREIGN KEY (question_type_id)
        REFERENCES question_types (question_type_id)
);

-- String values of all kinds of answers
CREATE TABLE answers (
    answer_id INT NOT NULL,
    answer_txt VARCHAR(600),
    is_correct BOOLEAN NOT NULL,
	answer_no INT,
    CONSTRAINT answers_pk PRIMARY KEY (answer_id)
);

/*
 * This table Stores which answers belong to which questions.
 * Also indicates which answers are correct. And has_photo
 * an optional column ("answer_no") for the kind of answers
 * which have an order.
 *
 * Questions and answers are stored as foreign keys referencing their
 * respective tables
 */
CREATE TABLE answers_to_questions (
    question_answers_id INT AUTO_INCREMENT,
    question_id INT NOT NULL,
    answer_id INT NOT NULL,
    CONSTRAINT answers_to_questions_pk PRIMARY KEY (question_answers_id),
    CONSTRAINT answers_to_questions_fk1 FOREIGN KEY (question_id)
        REFERENCES questions (question_id),
    CONSTRAINT answers_to_questions_fk2 FOREIGN KEY (answer_id)
        REFERENCES answers (answer_id)
);

/* Stores which questions belong to which quizzes
 *
 * Quizzes and questions are stored as foreign keys referencing their
 * respective tables
 */
CREATE TABLE quiz_questions (
    quiz_question_id INT AUTO_INCREMENT,
    quiz_id INT NOT NULL,
    question_id INT NOT NULL,
    CONSTRAINT quiz_questions_pk PRIMARY KEY (quiz_question_id),
    CONSTRAINT quiz_questions_fk1 FOREIGN KEY (quiz_id)
        REFERENCES quizzes (quiz_id),
    CONSTRAINT quiz_questions_fk2 FOREIGN KEY (question_id)
        REFERENCES questions (question_id)
);

/*
 * This table stores string value of answers entered 
 * by users for the questions that can not be checked
 * by the computer. So the quiz author will be 
 * able to see and grade those answers when possible.
 *
 * Questions and users are stored as foreign keys referencing their
 * respective tables
 */
CREATE TABLE user_answers (
    user_answer_id INT AUTO_INCREMENT,
    question_id INT NOT NULL,
    user_id INT NOT NULL,
    user_answer_txt VARCHAR(1000) NOT NULL,
    CONSTRAINT user_answers_pk PRIMARY KEY (user_answer_id),
    CONSTRAINT user_answers_fk1 FOREIGN KEY (question_id)
        REFERENCES questions (question_id),
    CONSTRAINT user_answers_fk2 FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);

/*
 * Table for saving all quiz related stats:
 * which quiz was done by whom, when and what results 
 * he had.
 * Can be used for any kind of quiz "result boards",
 * as well as for the user "history page" or just on
 * user's "profile page" as a "recently made quiz" post.
 *
 * Quizzes and users are stored as foreign keys referencing their
 * respective tables
 */
CREATE TABLE quiz_stats (
    stat_id INT AUTO_INCREMENT,
    quiz_id INT NOT NULL,
    user_id INT NOT NULL,
    taken_on DATETIME NOT NULL,
    used_time TIME NOT NULL,
    num_correct_answers INT NOT NULL,
    num_recieved_points decimal NOT NULL,
	some_answers_need_checking boolean not null,
    CONSTRAINT quiz_stats_pk PRIMARY KEY (stat_id),
    CONSTRAINT quiz_stats_fk1 FOREIGN KEY (quiz_id)
        REFERENCES quizzes (quiz_id),
    CONSTRAINT quiz_stats_fk2 FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);



-- ---------------------------- TRIGGERS ----------------------------

delimiter $$
CREATE TRIGGER delete_photos_and_answers_to_deleted_question
AFTER DELETE
   ON questions FOR EACH ROW

BEGIN



END;
$$

delimiter $$
CREATE TRIGGER delete_user_from_friend_lists_if_deactivated
AFTER DELETE
   ON users FOR EACH ROW

BEGIN



END;
$$

delimiter $$
CREATE TRIGGER delete_old_user_photo_when_updating
BEFORE UPDATE
   ON users FOR EACH ROW

BEGIN

   -- remove old photo before changing photo_id to the id of the new photo

END;
$$