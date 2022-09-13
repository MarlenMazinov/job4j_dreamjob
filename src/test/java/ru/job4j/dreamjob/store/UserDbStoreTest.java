package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDbStoreTest {

    @Test
    void whenAddNewUser() {
        UserDbStore store = new UserDbStore(new Main().loadPool());
        User user = new User(0, "user@mail.ru", "myPassword$$89");
        store.add(user);
        User userInDb = store.findById(user.getId());
        assertEquals(userInDb, user);
        store.clearTable();
    }

    @Test
    void whenAddExistsUser() {
        UserDbStore store = new UserDbStore(new Main().loadPool());
        User user1 = new User(0, "user@mail.ru", "myPassword$$89");
        User user2 = new User(1, "user@mail.ru", "myPass$$89");
        store.add(user1);
        assertTrue(store.add(user2).isEmpty());
        store.clearTable();
    }

    @Test
    void whenFindByEmailAndPwd() {
        UserDbStore store = new UserDbStore(new Main().loadPool());
        User user = new User("user@mail.ru", "myPwd");
        store.add(user);
        Optional<User> fromStore = store.findUserByEmailAndPwd(user.getEmail(), user.getPassword());
        fromStore.ifPresent(value -> user.setId(value.getId()));
        boolean rsl1 = Optional.of(user).
                equals(store.findUserByEmailAndPwd("user@mail.ru", "myPwd"));
        boolean rsl2 = Optional.of(user).
                equals(store.findUserByEmailAndPwd("user1@mail.ru", "myPwd"));
        boolean rsl3 = Optional.of(user).
                equals(store.findUserByEmailAndPwd("user@mail.ru", "myPwd1"));
        assertTrue(rsl1);
        assertFalse(rsl2);
        assertFalse(rsl3);
        store.clearTable();
    }
}