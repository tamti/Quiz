package databaseManagement;

public class DbContract {
	private DbContract() {
	}

	public static final String TABLE_USERS = "users";
	public static final String COL_USER_ID = "user_id";
	public static final String COL_FIRST_NAME = "first_name";
	public static final String COL_LAST_NAME = "last_name";
	public static final String COL_USERNAME = "username";
	public static final String COL_PASSWORD = "password";
	public static final String COL_SALT = "salt";
	public static final String COL_EMAIL = "email";
	public static final String COL_IS_ACTIVE = "is_active";
	public static final String COL_IS_ADMIN = "is_admin";

	public static final String TABLE_PHOTOS = "photos";
	public static final String COL_PHOTO_ID = "photo_id";
	public static final String COL_PHOTO_FILE = "photo_file";
	public static final String COL_IS_DEFAULT_PHOTO = "is_default";

	public static final String TABLE_FRIEND_LISTS = "friend_lists";
	public static final String COL_FRIEND1 = "user1_id";
	public static final String COL_FRIEND2 = "user2_id";
	public static final String COL_AWAITING_RESPONSE = "awaiting_response";
	public static final String COL_FRIENDSHIP_ACTIVE = "friendship_active";

	public static final String TABLE_QUIZZES = "quizzes";
	public static final String COL_QUIZ_ID = "quiz_id";
	public static final String COL_QUIZ_NAME = "quiz_name";
	public static final String COL_AUTHOR_ID = "quiz_author_id";
	public static final String COL_TIME_CREATED = "time_created";
	public static final String COL_SHOW_ANSWERS_IMMEDIATELY = "show_correct_answer_immediately";
	public static final String COL_QUESTIONS_ON_SAME_PAGE = "show_questions_on_one_page";
	public static final String COL_MAX_ALLOWED_TIME = "max_allowed_time";
	public static final String COL_MAX_POINTS = "max_points";

	public static final String TABLE_CATEGORIES = "all_quiz_categories";
	public static final String COL_CATEGORY_ID = "quiz_category_id";
	public static final String COL_CATEGORY_NAME = "category_name";

	public static final String TABLE_CATEGORIES_OF_QUIZZES = "categories_of_quizzes";

	public static final String TABLE_QUESTION_TYPES = "question_types";
	public static final String COL_QUESTION_TYPE_ID = "question_type_id";
	public static final String COL_QUESTION_TYPE = "question_type_name";

	public static final String TABLE_QUESTIONS = "questions";
	public static final String COL_QUESTION_ID = "question_id";
	public static final String COL_QUESTION = "question_txt";

	public static final String TABLE_QUESTION_PHOTOS = "question_photos";
	public static final String COL_QUESTION_PHOTO_ID = "question_photo_id";

	public static final String TABLE_ANSWERS = "answers";
	public static final String COL_ANSWER_ID = "answer_id";
	public static final String COL_ANSWER = "answer_txt";

	public static final String TABLE_ANSWERS_TO_QUESTIONS = "answers_to_questions";
	public static final String COL_QUESTION_ANSWER_ID = "question_answers_id";
	public static final String COL_ANSWER_IS_CORRECT = "is_correct";
	public static final String COL_ANSWER_NO = "answer_no";

	public static final String TABLE_QUIZ_QUESTIONS = "quiz_questions";
	public static final String COL_QUIZ_QUESTION_ID = "quiz_question_id";

	public static final String TABLE_USER_ANSWERS = "user_answers";
	public static final String COL_USER_ANSWER_ID = "user_answer_id";
	public static final String COL_USER_ANSWER = "user_answer_txt";

	public static final String TABLE_QUIZ_STATS = "quiz_stats";
	public static final String COL_STAT_ID = "stat_id";
	public static final String COL_TAKE_ON = "taken_on";
	public static final String COL_USED_TIME = "used_time";
	public static final String COL_NUM_CORRECT_ANSWERS = "num_correct_answers";
	public static final String COL_NUM_RECIEVED_POINTS = "num_recieved_points";
	public static final String COL_HAS_ANSWERS_TO_CHECK = "some_answers_need_checking";
}
