package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Candidate {
    private int id;
    private String name;

    private byte[] photo;

    private String description;

    private LocalDateTime created;

    public Candidate() {
    }

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Candidate(int id, String name, byte[] photo, String description,
                     LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.photo = photo;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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
        if (!(o instanceof Candidate)) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return getId() == candidate.getId() && Objects.equals(getName(),
                candidate.getName()) && Arrays.equals(getPhoto(), candidate.getPhoto())
                && Objects.equals(getDescription(), candidate.getDescription());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getName(), getDescription());
        result = 31 * result + Arrays.hashCode(getPhoto());
        return result;
    }
}
