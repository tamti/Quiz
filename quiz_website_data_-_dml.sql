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