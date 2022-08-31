package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Post;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostDbStoreTest {
    @Test
 public void whenCreatePost() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        Post post = new Post(0, "Java Job");
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenUpdatePost() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        Post firstPost = new Post(1, "Java Job");
        int id = store.add(firstPost).getId();
        Post secondPost = new Post(id, "C++ Job");
        store.update(secondPost);
        Post postInDb = store.findById(id);
        assertThat(postInDb.getName(), is(secondPost.getName()));
    }

    @Test
    public void whenFindAllPosts() {
        PostDbStore store = new PostDbStore(new Main().loadPool());
        store.clearTable();
        List<Post> addedList = List.of(new Post(0, "Java Job"),
                new Post(1, "C++ Job"),
                new Post(2, "Ruby Job"));
        addedList.forEach(store::add);
        List<Post> findedList = store.findAll();
        List<String> addedPostsNames = new ArrayList<>(3);
        addedList.forEach(post -> addedPostsNames.add(post.getName()));
        List<String> findedPostsNames = new ArrayList<>(3);
        findedList.forEach(post -> findedPostsNames.add(post.getName()));
        assertEquals(addedPostsNames, findedPostsNames);
    }
}
