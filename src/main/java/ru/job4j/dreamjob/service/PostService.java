package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostDbStore;

import java.util.Collection;
import java.util.List;

@Service
@ThreadSafe
public class PostService {

    private final PostDbStore store;
    private final CityService cityService;

    public PostService(PostDbStore store, CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    public List<Post> findAll() {
        List<Post> posts = store.findAll();
        posts.forEach(
                post -> {
                    City city = post.getCity();
                    if (city != null) {
                        post.setCity(
                                cityService.findById(city.getId())
                        );
                    }
                }
        );
        return posts;
    }

    public Post findById(int id) {
        return store.findById(id);
    }

    public void add(Post post) {
        store.add(post);
    }

    public void update(Post post) {
        store.update(post);
    }
}
