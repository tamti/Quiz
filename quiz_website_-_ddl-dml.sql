DROP DATABASE IF EXISTS quiz_website;

CREATE DATABASE quiz_website
DEFAULT CHARACTER SET utf8;

USE quiz_website;

-- ---------------------------- DDL ----------------------------

-- ---------------------------- TABLES -------------------------


-- List of names of all photo files avaliable on the website
CREATE TABLE photos (
    photo_id INT AUTO_INCREMENT,
    photo_file VARCHAR(150) NOT NULL,
    CONSTRAINT photos_pk PRIMARY KEY (photo_id)
);

/*
 * This table stores string values of nicknames and passwords 
 * (all necessary info for verifying user's password hash-values plus "salt")
 * of all users of the website. Column "is_active" indicates wether 
 * the account has been deactivated. Value of the column "is_admin" 
 * is set to true if the user is an admin. Column photo_id is a reference 
 * to the user's "profile picture"
 */
CREATE TABLE users (
    user_id INT AUTO_INCREMENT,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(80) NOT NULL,
	email varchar(50) NOT NULL,
	photo_id int,
	is_active BOOLEAN NOT NULL,
    is_admin BOOLEAN NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_id),
    CONSTRAINT users_uk1 UNIQUE KEY (username),
    CONSTRAINT users_uk2 UNIQUE KEY (email),
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
    time_sent DATETIME NOT NULL,
    txt TEXT NOT NULL,
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
    announcer_id INT NOT NULL,
    time_announced DATETIME NOT NULL,
    txt TEXT NOT NULL,
    CONSTRAINT announcements_pk PRIMARY KEY (announcement_id),
    CONSTRAINT announcements_fk FOREIGN KEY (announcer_id)
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
    quiz_id INT AUTO_INCREMENT,
    quiz_name VARCHAR(100),
    quiz_description TEXT,
    quiz_author_id INT,
    date_created DATE,
    show_correct_answer_immediately BOOLEAN NOT NULL,
	show_questions_on_one_page BOOlEAN NOT NULL,
	max_allowed_time_in_minutes INT NOT NULL,
    max_points INT NOT NULL,
    CONSTRAINT quizzes_pk PRIMARY KEY (quiz_id),
    CONSTRAINT quizzes_uk UNIQUE KEY (quiz_name),
    CONSTRAINT quizzes_fk FOREIGN KEY (quiz_author_id)
        REFERENCES users (user_id)
);

-- String values of all quiz categories
CREATE TABLE all_quiz_categories (
	quiz_category_id INT AUTO_INCREMENT,
	category_name VARCHAR(100) NOT NULL,
	CONSTRAINT all_quiz_categories_pk PRIMARY KEY (quiz_category_id),
    CONSTRAINT all_quiz_categories_uk UNIQUE KEY (category_name)
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

CREATE TABLE challenges (
	challenge_id INT AUTO_INCREMENT,
	message_id INT NOT NULL,
	quiz_id INT NOT NULL,
	challange_seen BOOLEAN NOT NULL,
	challange_accepted BOOLEAN NOT NULL,
	CONSTRAINT challenges_pk PRIMARY KEY (challenge_id),
    CONSTRAINT challenges_fk1 FOREIGN KEY (message_id)
        REFERENCES messages (message_id),
    CONSTRAINT challenges_fk2 FOREIGN KEY (quiz_id)
        REFERENCES quizzes (quiz_id)
);

-- String values of all questions types
CREATE TABLE question_types (
    question_type_id INT AUTO_INCREMENT,
    question_type_name VARCHAR(50) NOT NULL,
    CONSTRAINT question_types_pk PRIMARY KEY (question_type_id),
    CONSTRAINT question_types_uk UNIQUE KEY (question_type_name)
);

/*
 * This table Stores string values of all questions. Also idicates
 * their type and max number of points for the question
 *
 * Questions types are stored as foreign keys referencing their
 * respective table
 */
CREATE TABLE questions (
    question_id INT AUTO_INCREMENT,
    question_txt TEXT NOT NULL,
    question_type_id INT NOT NULL,
    photo_id INT,
    max_points DECIMAL NOT NULL,
    CONSTRAINT questions_pk PRIMARY KEY (question_id),
    CONSTRAINT qyestions_fk FOREIGN KEY (question_type_id)
        REFERENCES question_types (question_type_id)
);

-- String values of all kinds of answers
CREATE TABLE answers (
    answer_id INT AUTO_INCREMENT,
    answer_txt TEXT,
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
    user_answer_txt TEXT NOT NULL,
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
    used_time_in_seconds INT NOT NULL,
    num_correct_answers INT NOT NULL,
    num_recieved_points DECIMAL NOT NULL,
	some_answers_need_checking BOOLEAN NOT null,
    CONSTRAINT quiz_stats_pk PRIMARY KEY (stat_id),
    CONSTRAINT quiz_stats_fk1 FOREIGN KEY (quiz_id)
        REFERENCES quizzes (quiz_id),
    CONSTRAINT quiz_stats_fk2 FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);



-- ---------------------------- TRIGGERS ----------------------------

/*
 * If data from the table "questions" is deleted, this trigger will delete 
 * other related data from tables "photos" and "answers_to_questions"

delimiter $$
CREATE TRIGGER delete_photos_and_answers_to_deleted_question

AFTER DELETE ON questions 

FOR EACH ROW

BEGIN

	DELETE FROM photos 
	WHERE old.photo_id = photos.photo_id;

	DELETE FROM answers_to_questions 
	WHERE old.question_id = answers_to_questions.question_id;

END;
$$
 */
/*
 * If data from the table "answers_to_questions" is deleted, 
 * this trigger will delete related data from table "answers"

delimiter $$
CREATE TRIGGER delete_answers_after_deleting_answers_to_questions

AFTER DELETE ON answers 

FOR EACH ROW

BEGIN

	DELETE FROM answers 
	WHERE old.answer_id = answers.answer_id;

END;
$$
 */
/*
 * If data from table "users" is deleted, this trigger will 
 * delete all related data from table "friend_lists"
 
delimiter $$
CREATE TRIGGER delete_photo_and_friend_lists_of_deleted_user

AFTER DELETE ON users 

FOR EACH ROW

BEGIN

	DELETE FROM friend_lists 
	WHERE old.user_id = friend_lists.user1_id
		OR old.user_id = friend_lists.user2_id;
			
	DELETE FROM photos
	WHERE old.photo_id > 1
		AND old.photo_id = photos.photo_id;

END;
$$
*/
/*
 * If value of the column "photo_id" from table "users" is updated
 * (and is not set to default picture value: 1), this trigger will
 * delete related data from table "photos"

delimiter $$
CREATE TRIGGER delete_old_user_photo_when_updating

AFTER UPDATE ON users 

FOR EACH ROW

BEGIN

	DELETE FROM photos
	WHERE old.photo_id <> new.photo_id AND old.photo_id > 1
		AND old.photo_id = photos.photo_id;

END;
$$ */



-- -------------------------- DML ----------------------------

insert into question_types
	(question_type_id, question_type_name)
values
	(1, 'basicResponse'),
    (2, 'fillInTheBlank'),
    (3, 'multipleChoice'),
    (4, 'pictureResponse'),
    (5, 'multiAnswer'),
    (6, 'multiChoiceWithMultiAnswers'),
    (7, 'matching'),
    (8, 'graded'),
    (9, 'timed');
	
insert into photos
	(photo_id, photo_file)
values
	(1, "no_profile_picture.jpg");
