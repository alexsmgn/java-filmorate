DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS films CASCADE;

DROP TABLE IF EXISTS likes CASCADE;

DROP TABLE IF EXISTS friends CASCADE;

DROP TABLE IF EXISTS film_genres CASCADE;

DROP TABLE IF EXISTS genre CASCADE;

DROP TABLE IF EXISTS mpa CASCADE;

CREATE TABLE mpa (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE genre (
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE films (
    film_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name VARCHAR(50) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa INTEGER REFERENCES mpa(id)
);

CREATE TABLE likes (
    user_id BIGINT REFERENCES users(user_id),
    film_id BIGINT REFERENCES films(film_id),
    PRIMARY KEY(user_id, film_id)
);

CREATE TABLE friends (
    user_id BIGINT REFERENCES users(user_id),
    friend_id BIGINT REFERENCES users(user_id),
    status BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(user_id, friend_id)
);


CREATE TABLE film_genres (
    film_id BIGINT REFERENCES films(film_id),
    id INTEGER REFERENCES genre(id)
);


