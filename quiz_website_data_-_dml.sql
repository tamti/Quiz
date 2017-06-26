insert into question_types
	(question_type_name)
values
	('basicResponse'),
    ('fillInTheBlank'),
    ('multipleChoice'),
    ('pictureResponse'),
    ('multiAnswer'),
    ('multiChoiceWithMultiAnswers'),
    ('matching'),
    ('graded'),
    ('timed');
	
insert into photos
	(photo_id, photo_file, is_default);
values
	(1, "no_profile_picture.jpg", true);