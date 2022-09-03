package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostDbStoreTest {
    @Test
    public void whenCreatePost() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", true, 0, "Java descr", LocalDateTime.now());
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertEquals(postInDb, post);
    }

    @Test
    public void whenUpdatePost() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        LocalDateTime created = LocalDateTime.now();
        Post firstPost = new Post(1, "Java Job", true, 0, "Java descr", created);
        int id = store.add(firstPost).getId();
        Post secondPost = new Post(id, "C++ Job", true, 0, "Java descr", created);
        store.update(secondPost);
        Post postInDb = store.findById(id);
        assertEquals(postInDb, secondPost);
    }

    @Test
    public void whenFindAllPosts() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        store.clearTable();
        List<Post> addedList = List.of(new Post(0, "Java Job",
                        true, 0, "Java descr", LocalDateTime.now()),
                new Post(1, "C++ Job",
                        true, 1, "C++ descr", LocalDateTime.now()),
                new Post(2, "Ruby Job",
                        true, 2, "Ruby descr", LocalDateTime.now()));
        addedList.forEach(store::add);
        List<Post> findedList = store.findAll();
        boolean result = true;
        for (int i = 0; i < 3; i++) {
            if (!addedList.get(i).equals(findedList.get(i))) {
                result = false;
                break;
            }
        }
        assertTrue(result);
    }
}
