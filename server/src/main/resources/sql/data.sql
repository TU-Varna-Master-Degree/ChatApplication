INSERT INTO chatapp.chat_files (file_name, file_path, file_size, file_type) VALUES
    ('mysummer', 'C:\Users\Ivaylo_nikolaev\Desktop\ChatApplication\server\src\main\resources\files\1028814c-041c-4eea-8e03-b7e984cd3ae5.jpg', 62740, 'jpg'),
    ('aspirin', 'C:\Users\Ivaylo_nikolaev\Desktop\ChatApplication\server\src\main\resources\files\19e3d807-5d14-40f0-9a68-3c97f90364d1.jpg', 58348, 'jpg'),
    ('therock', 'C:\Users\Ivaylo_nikolaev\Desktop\ChatApplication\server\src\main\resources\files\2f6a7da3-3535-481c-b057-b8060bfe2967.jpg', 57982, 'jpg'),
    ('numb', 'C:\Users\Ivaylo_nikolaev\Desktop\ChatApplication\server\src\main\resources\files\9c8db882-4e04-473b-aa00-6414e9a1c34a.mp4', 12373628, 'mp4');

INSERT INTO chatapp.users (email, password, username) VALUES
    ('anton65@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Anton'),
    ('georgi_ivanov@abv.bg', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Georgi'),
    ('boris_todorow@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Boris'),
    ('dimitar_go@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'DimitarGo'),
    ('martin4@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Martin'),
    ('nikol_dimova@yahoo.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Nikol'),
    ('elena_ivanova@abv.bg', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Elena'),
    ('hristo_georgiev@yahoo.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Hristo'),
    ('ivaylo_nikolaev@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Ivaylo'),
    ('bojidar_ivanov@yahoo.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Bojidar'),
    ('gergana11@mail.bg', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Gergana'),
    ('dimitar_at@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'DimitarAt'),
    ('gencho_genev@abv.bg', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Gencho'),
    ('teodora96@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Teodora'),
    ('krasen_ivanov@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Krasen'),
    ('nikoleta_georgieva@abv.bg', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Nikoleta'),
    ('nikolaj_todorov@yahoo.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Nikolaj'),
    ('nikolina@abv.bg', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Nikolina'),
    ('nikola_petrov@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Nikola'),
    ('nikolinka@gmail.com', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Nikolinka'),
    ('kiril_dimitrov@abv.bg', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'Kiril');

INSERT INTO chatapp.friendship_relations (friendship_state, updated_state_date, sender_id, receiver_id) VALUES
    ('PENDING', '2022-01-07 12:50:38.62', 13, 4),
    ('PENDING', '2022-01-07 12:45:38.62', 1, 4),
    ('ACCEPTED', '2022-01-07 14:04:28.477', 4, 7),
    ('ACCEPTED', '2022-01-07 14:05:09.418', 4, 8),
    ('ACCEPTED', '2022-01-07 14:05:47.384', 4, 2),
    ('ACCEPTED', '2022-01-07 14:06:31.238', 4, 9),
    ('ACCEPTED', '2022-01-07 14:07:27.721', 4, 3),
    ('ACCEPTED', '2022-01-07 14:10:55.27', 4, 5),
    ('ACCEPTED', '2022-01-07 14:11:51.538', 4, 11),
    ('ACCEPTED', '2022-01-07 14:12:54.89', 14, 4),
    ('REJECTED', '2022-01-07 14:13:22.034', 21, 4),
    ('PENDING', '2022-01-07 12:46:38.62', 10, 4),
    ('ACCEPTED', '2022-01-07 17:06:12.678', 13, 15),
    ('ACCEPTED', '2022-01-08 11:07:52.463', 12, 4),
    ('ACCEPTED', '2022-01-08 11:10:10', 9, 12);

INSERT INTO chatapp.groups (creation_date, parent_id) VALUES
    ('2022-01-07 14:04:28.523', NULL),
    ('2022-01-07 14:05:09.42', NULL),
    ('2022-01-07 14:05:47.386', NULL),
    ('2022-01-07 14:06:31.239', NULL),
    ('2022-01-07 14:07:27.755', NULL),
    ('2022-01-07 14:10:55.3', NULL),
    ('2022-01-07 14:11:51.541', NULL),
    ('2022-01-07 14:12:54.892', NULL),
    ('2022-01-07 16:26:13.995', 5),
    ('2022-01-07 16:35:10.962', 8),
    ('2022-01-07 16:51:15.35', 6),
    ('2022-01-07 17:06:12.734', NULL),
    ('2022-01-08 11:07:52.537', NULL),
    ('2022-01-08 11:10:10.008', NULL);

INSERT INTO chatapp.notifications (content, message_type, send_date, file_id, sender_id, group_id) VALUES
    ('Hello!', 'TEXT', '2022-01-07 14:23:39.88', NULL, 4, 6),
    ('Hello Todor', 'TEXT', '2022-01-07 14:24:39.88', NULL, 5, 6),
    ('How are you?', 'TEXT', '2022-01-07 14:25:39.88', NULL, 5, 6),
    ('I''m fine, thank you!', 'TEXT', '2022-01-07 14:26:39.88', NULL, 4, 6),
    ('Can you send me picture from your summer?', 'TEXT', '2022-01-07 14:27:39.88', NULL, 4, 6),
    ('Yes, of course.', 'TEXT', '2022-01-07 14:28:39.88', NULL, 5, 6),
    (NULL, 'IMAGE', '2022-01-07 14:39:24.989', 1, 5, 6),
    ('Amazing!', 'TEXT', '2022-01-07 14:45:39.88', NULL, 4, 6),
    ('Hey', 'TEXT', '2022-01-07 14:45:54.606', NULL, 4, 8),
    ('Hey', 'TEXT', '2022-01-07 14:46:54.606', NULL, 14, 8),
    ('I need your help.', 'TEXT', '2022-01-07 14:47:54.606', NULL, 4, 8),
    ('I''m having a terrible headache.', 'TEXT', '2022-01-07 14:48:54.606', NULL, 4, 8),
    ('How can i help you?', 'TEXT', '2022-01-07 14:49:54.606', NULL, 14, 8),
    ('Can you bring me an aspirin?', 'TEXT', '2022-01-07 14:50:54.606', NULL, 4, 8),
    ('How does it look like?', 'TEXT', '2022-01-07 14:51:54.606', NULL, 14, 8),
    (NULL, 'IMAGE', '2022-01-07 15:05:14.759', 2, 4, 8),
    ('Okey', 'TEXT', '2022-01-07 15:06:14.759', NULL, 14, 8),
    ('Hello Boris!', 'TEXT', '2022-01-07 15:08:38.306', NULL, 4, 5),
    ('Hi', 'TEXT', '2022-01-07 15:10:38.306', NULL, 3, 5),
    ('Do you like watching movies?', 'TEXT', '2022-01-07 15:11:38.306', NULL, 4, 5),
    ('Yes, of course', 'TEXT', '2022-01-07 15:12:38.306', NULL, 3, 5),
    ('Who is your favorite actor?', 'TEXT', '2022-01-07 15:13:38.306', NULL, 4, 5),
    (NULL, 'IMAGE', '2022-01-07 16:19:54.681', 3, 3, 5),
    ('I also like him!', 'TEXT', '2022-01-07 16:20:54.681', NULL, 4, 5),
    ('Hi, Georgi!', 'TEXT', '2022-01-07 16:21:54.681', NULL, 4, 9),
    ('Hi guys.', 'TEXT', '2022-01-07 16:22:54.681', NULL, 2, 9),
    ('I''m busy right now. I''ll be online again tomorrow.', 'TEXT', '2022-01-07 16:23:54.681', NULL, 2, 9),
    ('Ok, no problem.', 'TEXT', '2022-01-07 16:24:54.681', NULL, 3, 9),
    ('Hey Gergana, how are you?', 'TEXT', '2022-01-07 16:36:17.021', NULL, 4, 10),
    ('I''am fine.', 'TEXT', '2022-01-07 16:37:17.021', NULL, 11, 10),
    ('Gergana, can you attend to your meet at 6PM?', 'TEXT', '2022-01-07 16:38:17.021', NULL, 14, 10),
    ('Yes, I''ll be there ten minutes earlier.', 'TEXT', '2022-01-07 16:39:17.021', NULL, 11, 10),
    ('Very nice!', 'TEXT', '2022-01-07 16:40:17.021', NULL, 4, 10),
    ('Are you ready for the party tonight?', 'TEXT', '2022-01-07 16:52:51.279', NULL, 4, 11),
    ('YES!', 'TEXT', '2022-01-07 16:53:51.279', NULL, 8, 11),
    ('Let''s go!', 'TEXT', '2022-01-07 16:55:51.279', NULL, 7, 11),
    ('I''m ready!', 'TEXT', '2022-01-07 16:54:51.279', NULL, 5, 11),
    ('Thanks!', 'TEXT', '2022-01-07 16:53:26.584', NULL, 4, 8),
    ('Hi friend!', 'TEXT', '2022-01-07 17:06:49.309', NULL, 15, 12),
    ('How is your day going?', 'TEXT', '2022-01-07 17:07:18.112', NULL, 15, 12),
    ('My day is going great so far.', 'TEXT', '2022-01-07 17:09:19.924', NULL, 13, 12),
    ('Nice', 'TEXT', '2022-01-07 17:10:19.924', NULL, 15, 12),
    ('Hello', 'TEXT', '2022-01-08 11:10:24.901', NULL, 12, 14),
    ('Hi', 'TEXT', '2022-01-08 11:10:51.899', NULL, 9, 14),
    ('Which is your favourite song ?', 'TEXT', '2022-01-08 11:15:16.691', NULL, 4, 6),
    (NULL, 'FILE', '2022-01-08 11:27:13.396', 4, 5, 6),
    ('Nice choice!', 'TEXT', '2022-01-08 11:31:41.486', NULL, 4, 6);

INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES
    ('2022-01-07 14:04:28.523', 7, 1),
    ('2022-01-07 14:04:28.523', 4, 1),
    ('2022-01-07 14:05:09.42', 8, 2),
    ('2022-01-07 14:05:09.42', 4, 2),
    ('2022-01-07 14:05:47.386', 2, 3),
    ('2022-01-07 14:05:47.386', 4, 3),
    ('2022-01-07 14:06:31.239', 9, 4),
    ('2022-01-07 14:06:31.239', 4, 4),
    ('2022-01-07 14:07:27.755', 3, 5),
    ('2022-01-07 14:07:27.755', 4, 5),
    ('2022-01-07 14:10:55.3', 5, 6),
    ('2022-01-07 14:10:55.3', 4, 6),
    ('2022-01-07 14:11:51.541', 11, 7),
    ('2022-01-07 14:11:51.541', 4, 7),
    ('2022-01-07 14:12:54.892', 4, 8),
    ('2022-01-07 14:12:54.892', 14, 8),
    ('2022-01-07 14:07:27.755', 3, 9),
    ('2022-01-07 14:07:27.755', 4, 9),
    ('2022-01-07 16:21:00.995', 2, 9),
    ('2022-01-07 14:12:54.892', 4, 10),
    ('2022-01-07 14:12:54.892', 14, 10),
    ('2022-01-07 16:35:10.962', 11, 10),
    ('2022-01-07 14:10:55.3', 4, 11),
    ('2022-01-07 14:10:55.3', 5, 11),
    ('2022-01-07 16:51:15.35', 7, 11),
    ('2022-01-07 16:51:15.35', 8, 11),
    ('2022-01-07 17:06:12.734', 15, 12),
    ('2022-01-07 17:06:12.734', 13, 12),
    ('2022-01-08 11:07:52.537', 4, 13),
    ('2022-01-08 11:07:52.537', 12, 13),
    ('2022-01-08 11:10:10.008', 12, 14),
    ('2022-01-08 11:10:10.008', 9, 14);