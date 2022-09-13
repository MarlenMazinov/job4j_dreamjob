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
        store.clearTable();
    }

    @Test
    void whenAddExistsUser() {
        UserDbStore store = new UserDbStore(new Main().loadPool());
        User user1 = new User(0, "user@mail.ru", "myPassword$$89");
        User user2 = new User(1, "user@mail.ru", "myPass$$89");
        store.add(user1);
        assertNull(store.add(user2));
        store.clearTable();
    }

    @Test
    void whenFindByEmailAndPwd() {
        UserDbStore store = new UserDbStore(new Main().loadPool());
        User user = new User("user@mail.ru", "myPwd");
        store.add(user);
        user.setId(store.findUserByEmailAndPwd(user.getEmail(), user.getPassword()).getId());
        boolean rsl1 = user.equals(store.findUserByEmailAndPwd("user@mail.ru", "myPwd"));
        boolean rsl2 = user.equals(store.findUserByEmailAndPwd("user1@mail.ru", "myPwd"));
        boolean rsl3 = user.equals(store.findUserByEmailAndPwd("user@mail.ru", "myPwd1"));
        assertTrue(rsl1);
        assertFalse(rsl2);
        assertFalse(rsl3);
        store.clearTable();
    }
}