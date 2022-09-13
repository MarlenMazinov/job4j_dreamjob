package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.UserDbStore;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
public class UserService {
    UserDbStore store;

    public UserService(UserDbStore store) {
        this.store = store;
    }

    public List<User> findAll() {
        return store.findAll();
    }

    public User findById(int id) {
        return store.findById(id);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String pwd) {

        return Optional.ofNullable(store.findUserByEmailAndPwd(email, pwd));
    }

    public Optional<User> add(User user) {
        return Optional.ofNullable(store.add(user));
    }

    public void update(User user) {
        store.update(user);
    }

}
