package ru.job4j.dreamjob.controller;

import org.junit.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostControllerTest {
    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post"),
                new Post(2, "New post")
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("posts"));
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "New post");
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.findById(1)).thenReturn(new City(1, "Москва"));
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input, Integer.toString(1));
        verify(postService).add(input);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenAddPost() {
        List<City> cities = Arrays.asList(
                new City(1, "Moscow"),
                new City(2, "Ekb")
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.addPost(model, session);
        verify(model).addAttribute("cities", cities);
        verify(model).addAttribute("post", new Post(0, "Заполните поле"));
        assertThat(page, is("addPost"));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(1, "Some post");
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.findById(1)).thenReturn(new City(1, "Москва"));
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.updatePost(post, Integer.toString(1));
        verify(postService).update(post);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenFormUpdatePost() {
        List<City> cities = Arrays.asList(
                new City(1, "Moscow"),
                new City(2, "Ekb")
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        when(postService.findById(1)).thenReturn(new Post(1, "Some post"));
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.formUpdatePost(model, 1, session);
        verify(model).addAttribute("post", new Post(1, "Some post"));
        verify(model).addAttribute("cities", cities);
        assertThat(page, is("updatePost"));
    }
}