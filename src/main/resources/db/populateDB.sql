DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id)
VALUES (now() - INTERVAL '1 HOUR','Админ ланч', 510, 100001),
       ('27-02-2022 01:02:59', 'A_user dinner1', 1200, 100000),
       ('27-02-2022 02:02:59', 'B_user dinner2', 1300, 100000),
       ('27-02-2022 03:02:59', 'C_user dinner3', 1400, 100000),
       (now(),'Админ ужин', 1500, 100001);

