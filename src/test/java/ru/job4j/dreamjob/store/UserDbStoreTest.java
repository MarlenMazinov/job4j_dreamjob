package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserDbStoreTest {

    @Test
    void whenAddNewUser() {
        UserDbStore store = new UserDbStore(new Main().loadPool());
        User user = new User(0, "user@mail.ru", "myPassword$$89");
        store.add(user);
        User userInDb = store.findById(user.getId());
        assertEquals(userInDb, user);
    }

    @Test
    void whenAddExistsUser() {
        UserDbStore store = new UserDbStore(new Main().loadPool());
        User user1 = new User(0, "user@mail.ru", "myPassword$$89");
        User user2 = new User(1, "user@mail.ru", "myPass$$89");
        store.add(user1);
        assertNull(store.add(user2));
    }
}