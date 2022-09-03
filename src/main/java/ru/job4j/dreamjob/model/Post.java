package ru.job4j.dreamjob.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Post implements Serializable {
    private int id;
    private String name;

    private boolean visible;

    private int cityId;

    private String description;

    private LocalDateTime created;

    private Post() {

    }

    public Post(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Post(int id, String name, boolean visible, int cityId, String description,
                LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.visible = visible;
        this.cityId = cityId;
        this.description = description;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        Post post = (Post) o;
        return getId() == post.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
