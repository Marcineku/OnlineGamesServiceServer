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
