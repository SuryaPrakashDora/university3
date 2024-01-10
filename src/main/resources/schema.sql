CREATE TABLE professor(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    department VARCHAR(255)
);

CREATE TABLE course(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    credits INT,
    professorId INT,
    FOREIGN KEY(professorId) REFERENCES professor(id)
);

CREATE TABLE student(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE course_student(
    studentId INT,
    courseId INT,
    PRIMARY KEY(studentId, courseId),
    FOREIGN KEY(studentId) REFERENCES student(id),
    FOREIGN KEY(courseId) REFERENCES course(id)
);