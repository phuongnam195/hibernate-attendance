DROP DATABASE IF EXISTS attendance_management;
CREATE DATABASE attendance_management;
USE attendance_management;

CREATE TABLE SUBJECT(
	ID CHAR(4) NOT NULL,
    Name VARCHAR(25) NOT NULL,
    StartDate DATE NOT NULL,
    FinishDate DATE,
    Weekday INT(1),
    StartTime TIME NOT NULL,
    FinishTime TIME NOT NULL,
    Classroom VARCHAR(20) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE STUDENT(
    ID CHAR(8) NOT NULL,
    Name VARCHAR(25) NOT NULL,
    HashedPW BLOB,
    PRIMARY KEY (ID)
);

CREATE TABLE LEARNING(
    ID INT AUTO_INCREMENT NOT NULL,
    SubjectID CHAR(4) NOT NULL,
    StudentID CHAR(8) NOT NULL,
    Attendance INT(5) NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (SubjectID) REFERENCES SUBJECT(ID),
    FOREIGN KEY (StudentID) REFERENCES STUDENT(ID),
    UNIQUE(SubjectID, StudentID)
);

CREATE TABLE ADMIN(
	Username CHAR(10) NOT NULL,
    HashedPW BLOB NOT NULL,
    PRIMARY KEY (Username)
);

INSERT INTO SUBJECT(ID, Name, StartDate, FinishDate, Weekday, StartTime, FinishTime, Classroom)
VALUES('MH01', 'Toán nâng cao', '2021-11-24', '2022-03-02', 4, '12:30:00', '16:00:00', 'F.104');
INSERT INTO SUBJECT(ID, Name, StartDate, FinishDate, Weekday, StartTime, FinishTime, Classroom)
VALUES('MH02', 'Vật lý đại cương', '2021-12-06', '2022-03-14', 2, '07:30:00', '10:00:00', 'F.203');

INSERT INTO STUDENT(ID, Name) VALUES('19120591', 'Đặng Phương Nam');
INSERT INTO STUDENT(ID, Name) VALUES('19120500', 'Lưu Hữu Dụng');

INSERT INTO LEARNING(SubjectID, StudentID, Attendance) VALUES('MH01', '19120591', 22); -- 22d = 10110b
INSERT INTO LEARNING(SubjectID, StudentID, Attendance) VALUES('MH02', '19120591', 13); -- 13d = 01101b
INSERT INTO LEARNING(SubjectID, StudentID, Attendance) VALUES('MH01', '19120500', 30); -- 30d = 11110b

INSERT INTO ADMIN(Username, HashedPW) VALUES('admin', x'7099EFB08513C50696C93BCDCD71418D');