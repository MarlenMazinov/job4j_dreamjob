package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostStore {

    private static final PostStore INST = new PostStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job",
                "This post contains iformation about Junior Java vacancy",
                new Date(2022, Calendar.MAY, 11, 10, 25)));
        posts.put(2, new Post(2, "Middle Java Job",
                "This post contains iformation about Middle Java vacancy",
                new Date(2022, Calendar.JUNE, 5, 12, 33)));
        posts.put(3, new Post(3, "Senior Java Job",
                "This post contains iformation about Senior Java vacancy",
                new Date(2022, Calendar.JUNE, 2, 15, 0)));
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}