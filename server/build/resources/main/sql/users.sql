

--
-- TOC entry 3055 (class 0 OID 24998)
-- Dependencies: 204
-- Data for Name: friendship_relations; Type: TABLE DATA; Schema: chatapp; Owner: postgres
--

INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 1, 2);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 1, 3);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('PENDING', 1, 5);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('REJECTED', 1, 9);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 1, 6);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('PENDING', 2, 3);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('PENDING', 2, 5);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('PENDING', 2, 4);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 2, 10);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 2, 7);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 3, 5);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 3, 6);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 3, 9);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 4, 1);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 4, 2);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 7, 8);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 6, 5);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 9, 10);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('ACCEPTED', 6, 10);
INSERT INTO chatapp.friendship_relations (friendship_state, sender_id, receiver_id) VALUES ('PENDING', 10, 1);


--
-- TOC entry 3056 (class 0 OID 25003)
-- Dependencies: 205
-- Data for Name: group_notifications; Type: TABLE DATA; Schema: chatapp; Owner: postgres
--

INSERT INTO chatapp.group_notifications (notification_id, sender_id, group_id) VALUES (10, 2, 1);
INSERT INTO chatapp.group_notifications (notification_id, sender_id, group_id) VALUES (11, 3, 1);
INSERT INTO chatapp.group_notifications (notification_id, sender_id, group_id) VALUES (12, 1, 1);


--
-- TOC entry 3058 (class 0 OID 25010)
-- Dependencies: 207
-- Data for Name: groups; Type: TABLE DATA; Schema: chatapp; Owner: postgres
--

INSERT INTO chatapp.groups (id, creation_date, name, creator_id) VALUES (2, '20201-11-10 00:00:00', 'Friends4Lyfe', 5);
INSERT INTO chatapp.groups (id, creation_date, name, creator_id) VALUES (3, '2021-11-20 00:00:00', 'PepsiEnjoyers', 8);
INSERT INTO chatapp.groups (id, creation_date, name, creator_id) VALUES (1, '2021-10-29 00:00:00', 'IllegalActivity', 1);


--
-- TOC entry 3060 (class 0 OID 25018)
-- Dependencies: 209
-- Data for Name: notifications; Type: TABLE DATA; Schema: chatapp; Owner: postgres
--

INSERT INTO chatapp.notifications (id, content, message_type, received, send_date, file_id) VALUES (5, 'Hello', 'TEXT', true, '2021-11-29 00:00:00', NULL);
INSERT INTO chatapp.notifications (id, content, message_type, received, send_date, file_id) VALUES (6, 'Hey', 'TEXT', true, '2021-11-29 00:00:00', NULL);
INSERT INTO chatapp.notifications (id, content, message_type, received, send_date, file_id) VALUES (7, 'How are you?', 'TEXT', true, '2021-11-29 00:00:00', NULL);
INSERT INTO chatapp.notifications (id, content, message_type, received, send_date, file_id) VALUES (8, 'Fine, and you?', 'TEXT', true, '2021-11-29 00:00:00', NULL);
INSERT INTO chatapp.notifications (id, content, message_type, received, send_date, file_id) VALUES (9, '...', 'TEXT', true, '2021-11-29 00:00:00', NULL);
INSERT INTO chatapp.notifications (id, content, message_type, received, send_date, file_id) VALUES (10, 'Hi people', 'TEXT', true, '2021-12-01 00:00:00', NULL);
INSERT INTO chatapp.notifications (id, content, message_type, received, send_date, file_id) VALUES (11, 'Hi, Azure', 'TEXT', false, '2021-12-01 00:00:00', NULL);
INSERT INTO chatapp.notifications (id, content, message_type, received, send_date, file_id) VALUES (12, 'hi', 'TEXT', true, '2021-12-01 00:00:00', NULL);


--
-- TOC entry 3061 (class 0 OID 25027)
-- Dependencies: 210
-- Data for Name: user_groups; Type: TABLE DATA; Schema: chatapp; Owner: postgres
--

INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 1, 1);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 2, 1);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 3, 1);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 4, 1);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 5, 2);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 8, 3);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 6, 2);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 7, 2);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 3, 3);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 4, 3);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 5, 3);
INSERT INTO chatapp.user_groups (join_date, user_id, group_id) VALUES ('2021-10-29 00:00:00', 9, 3);


--
-- TOC entry 3062 (class 0 OID 25032)
-- Dependencies: 211
-- Data for Name: user_notifications; Type: TABLE DATA; Schema: chatapp; Owner: postgres
--

INSERT INTO chatapp.user_notifications (notification_id, sender_id, receiver_id) VALUES (5, 1, 2);
INSERT INTO chatapp.user_notifications (notification_id, sender_id, receiver_id) VALUES (6, 2, 1);
INSERT INTO chatapp.user_notifications (notification_id, sender_id, receiver_id) VALUES (7, 1, 2);
INSERT INTO chatapp.user_notifications (notification_id, sender_id, receiver_id) VALUES (8, 2, 1);
INSERT INTO chatapp.user_notifications (notification_id, sender_id, receiver_id) VALUES (9, 1, 2);


--
-- TOC entry 3064 (class 0 OID 25039)
-- Dependencies: 213
-- Data for Name: users; Type: TABLE DATA; Schema: chatapp; Owner: postgres
--

INSERT INTO chatapp.users (id, email, password, username) VALUES (1, 'ppesho22@abv.bg', '18dkASg1', 'Pesho22');
INSERT INTO chatapp.users (id, email, password, username) VALUES (2, 'azuris@pharo.eg', '8ufFaaS', 'Azure');
INSERT INTO chatapp.users (id, email, password, username) VALUES (3, 'g.george@gmail.com', '132hggFF', 'GeneralG');
INSERT INTO chatapp.users (id, email, password, username) VALUES (4, 'ludwig.peterson21@mail.de', '777hdaG0', 'Lute12');
INSERT INTO chatapp.users (id, email, password, username) VALUES (5, 'samuel.h224@gmail.com', '1qazi88F', 'TheJanitor');
INSERT INTO chatapp.users (id, email, password, username) VALUES (6, 'd.georgiev@hotmail.com', 'ER712fg', 'Voice909');
INSERT INTO chatapp.users (id, email, password, username) VALUES (7, 'daniel_frank99@gmail.com', '12BBsdgF11', 'Dan');
INSERT INTO chatapp.users (id, email, password, username) VALUES (8, 'philiplane17@proton-mail.com', '8u8FkkJn', 'PepsiEnjoyer');
INSERT INTO chatapp.users (id, email, password, username) VALUES (9, 'civ4denmark@gmail.com', '1aghFFa66f2', 'Crusader10');
INSERT INTO chatapp.users (id, email, password, username) VALUES (10, 'liliana223@abv.bg', 'TGhjM2', 'Lili223');


--
-- TOC entry 3074 (class 0 OID 0)
-- Dependencies: 202
-- Name: chat_files_id_seq; Type: SEQUENCE SET; Schema: chatapp; Owner: postgres
--
