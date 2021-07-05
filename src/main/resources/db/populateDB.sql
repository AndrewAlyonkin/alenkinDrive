DELETE
FROM files;

DELETE
FROM events;

DELETE
FROM users;

ALTER SEQUENCE global_seq RESTART WITH 10000;

INSERT INTO users (name)
VALUES ('Chuck Norris');
INSERT INTO users (name)
VALUES ('Bruce Lee');

INSERT INTO files (file_uri, size, user_id)
VALUES ('Chuck://test/testDir/testFile.pdf', 9000, 10000);

INSERT INTO files (file_uri, size, user_id)
VALUES ('Chuck://directory/dir/file.txt', 15000, 10000);

INSERT INTO files (file_uri, size, user_id)
VALUES ('Lee://BruceDir/testFile.exe', 25000, 10001);

INSERT INTO files (file_uri, size, user_id)
VALUES ('Lee://BruceDir/virusFile.jpeg', 35000, 10001);

INSERT INTO events (download_time, file_id, user_id)
VALUES ('2021-06-20 19:10:25-07', 10002, 10000);

INSERT INTO events (download_time, file_id, user_id)
VALUES ('2021-06-20 19:10:25-07', 10005, 10001);

