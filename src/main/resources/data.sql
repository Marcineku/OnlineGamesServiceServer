--NEWS
SELECT 1 FROM NEWS;
create sequence news_sequence;

INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (
  news_sequence.nextval,
  'Type1',
  'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse ornare, massaeu euismod vestibulum, tortor risus blandit tellus, sit amet pharetra purus nulla a arcu. In ullamcorper purus tempor libero aliquet dictum. Proin hendrerit ullamcorper di',
  'sed rhoncus nisi. Etiam sit amet quam ac turpis auctor volutpat. Maecenas in condimentum augue. Donec egestas felis a massa consequat porttitor vitae nec lacus. Nulla imperdiet nibh ultricies quam lobortis malesuada. Cras mattis ligula augue, ',
  sysdate(),
  'EN');
INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (
  news_sequence.nextval,
  'Type2',
  'Mauris varius ullamcorper placerat. Vestibulum egestas est purus, pharetra egestas mi euismod nec. Nam vel eros leo. Nulla ut auctor lorem, vel tempus magna. Praesent quis efficitur metus. Vivamus sit amet massa sed risus tempus blandit eu in justo',
  'Nunc posuere, massa eget molestie tristique, quam est volutpat nulla, nec tempus enim nunc vitae mauris. Proin eros odio, tempor a lacinia vitae, auctor sit amet nisl. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turp',
  sysdate(),
  'PL');


INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (news_sequence.nextval,'Type3','Title3','Text3', sysdate(), 'EN');
INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (news_sequence.nextval,'Type4','Title4','Text4', sysdate(), 'PL');
INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (news_sequence.nextval,'Type4','Title4','Text4', sysdate(), 'PL');
INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (news_sequence.nextval,'Type4','Title4','Text4', sysdate(), 'PL');
INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (news_sequence.nextval,'Type4','Title4','Text4', sysdate(), 'PL');
INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (news_sequence.nextval,'Type4','Title4','Text4', sysdate(), 'PL');
INSERT INTO NEWS (NEWS_ID, TYPE, TITLE, TEXT, PUBLICATION_DATE, LANGUAGE)
  VALUES (news_sequence.nextval,'Type4','Title4','Text4', sysdate(), 'PL');

--ROLES
INSERT INTO ROLE(ROLE_ID,ROLE_NAME)
VALUES(1,'ROLE_ADMIN');
INSERT INTO ROLE(ROLE_ID,ROLE_NAME)
VALUES(2,'ROLE_USER');

--USERS
INSERT INTO USER(USER_ID,EMAIL,IS_EMAIL_VERIFIED,LAST_LOGON_DATE,PASSWORD,REGISTRATION_DATE,USERNAME)
VALUES(1,	'mateusz@mateusz.pl',	'T',	sysdate(),	'$2a$10$/cOZMG2lwBCY1p.Fod3BROVNgB.qOW5UtDmaAwdMMUCcsn28Zm/8e',	sysdate(),	'Mateusz');
INSERT INTO USER(USER_ID,EMAIL,IS_EMAIL_VERIFIED,LAST_LOGON_DATE,PASSWORD,REGISTRATION_DATE,USERNAME)
VALUES(2,	'Marcin@Marcin.pl',	'T',	sysdate(),	'$2a$10$Xz0NO6YbnhLETzMulf0XKOvecBubTGq31SiDchGV3k7jqnh.wEGgO',	sysdate(),	'Marcin');

--USER_ROLES
INSERT INTO USER_ROLES(USER_ID,ROLE_ID)
VALUES(1,1);
INSERT INTO USER_ROLES(USER_ID,ROLE_ID)
VALUES(1,2);
INSERT INTO USER_ROLES(USER_ID,ROLE_ID)
VALUES(2,2);


 INSERT INTO TIC_TAC_TOE_GAME (GAME_ID,CREATED,FIRST_PLAYER_PIECE_CODE,GAME_STATUS,GAME_TYPE,FIRST_PLAYER_ID,SECOND_PLAYER_ID)
 VALUES (1,	sysdate(),	'X',	'FIRST_PLAYER_WON',	'MULTIPLAYER',	1,	2);
 INSERT INTO TIC_TAC_TOE_GAME (GAME_ID,CREATED,FIRST_PLAYER_PIECE_CODE,GAME_STATUS,GAME_TYPE,FIRST_PLAYER_ID,SECOND_PLAYER_ID)
 VALUES (2,	sysdate(),	'O',	'DRAW',	'MULTIPLAYER',	1,	2);

 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (1,	sysdate(),	0,	1, 1,	1);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (2,	sysdate(),	4,	2, 1,	2);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (3,	sysdate(),	3,	3, 1,	1);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (4,	sysdate(),	1,	4, 1,	2);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (5,	sysdate(),	5,	5, 1,	1);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (6,	sysdate(),	2,	6,	1,	2);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (7,	sysdate(),	6,	7,	1,	1);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (8,	sysdate(),	0,	1,	2,	1);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (9,	sysdate(),	3,	2,	2,	2);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (10,	sysdate(),	6,	3,	2,	1);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (11,	sysdate(),	4,	4,	2,	2);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (12,	sysdate(),	1,	5,	2,	1);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (13,	sysdate(),	8,	6,	2,	2);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (14,	sysdate(),	7,	7,	2,	1);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (15,	sysdate(),	2,	8,	2,	2);
 INSERT INTO TIC_TAC_TOE_MOVE (MOVE_ID,CREATED,FIELD,MOVE_NO,GAME_ID,USER_ID)
 VALUES (16,	sysdate(),	5,	9,	2,	1);