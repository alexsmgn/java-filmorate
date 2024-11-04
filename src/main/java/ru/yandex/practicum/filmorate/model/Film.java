package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.MinDate;

import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(of = {"id"})
@Slf4j
public class Film {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @MinDate
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Mpa mpa;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private Set<Likes> likes = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public boolean isLiked(Long id) {
        if (likes.size() == 0) {
            return false;
        }

        for (Likes like : likes) {
            if (like.getUserId() == id) {
                return true;
            }
        }
        return false;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void addLike(Likes like) {
        if (like.getUserId() != 0) {
            likes.add(like);
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", getId());
        values.put("film_name", getName());
        values.put("description", getDescription());
        values.put("release_date", getReleaseDate());
        values.put("duration", getDuration());
        values.put("mpa", getMpa().getId());
        values.put("genres", getGenres());

        return values;
    }
}
