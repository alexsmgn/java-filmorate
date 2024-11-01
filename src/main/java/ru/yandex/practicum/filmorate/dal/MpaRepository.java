package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.rowMappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Repository
@Slf4j
public class MpaRepository implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Mpa getMpaById(Integer id) {
        String query = "SELECT * FROM mpa WHERE id = ?";
        Mpa mpa = jdbcTemplate.query(query, new MpaRowMapper(), id).stream().findAny()
                .orElseThrow(() -> new NotFoundException("Mpa не найден"));

        return mpa;
    }

    @Override
    public List<Mpa> getMpa() {
        String query = "SELECT * FROM mpa";

        List<Mpa> mpa = jdbcTemplate.query(query, new MpaRowMapper());
        return mpa;
    }
}