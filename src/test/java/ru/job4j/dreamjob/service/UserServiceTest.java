package ru.job4j.dreamjob.service;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.UserDbStore;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void whenAddExistsUser() {
        UserDbStore store = new UserDbStore(new Main().loadPool());
        store.clearTable();
        UserService service = new UserService(store);
        User user1 = new User(0, "user@mail.ru", "myPassword$$89");
        User user2 = new User(1, "user@mail.ru", "myPass$$89");
        service.add(user1);
        assertTrue(service.add(user2).isEmpty());
    }
}