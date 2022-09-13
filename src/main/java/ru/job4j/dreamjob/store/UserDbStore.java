package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDbStore {
    private static final Logger LOG = LoggerFactory.getLogger(UserDbStore.class.getName());
    private static final String SELECT_QUERY = "SELECT * FROM users";
    private static final String CREATE_QUERY = "INSERT INTO users(email, password) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email=?, password=?"
            + "WHERE id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM users WHERE id = ?";

    private static final String SELECT_BY_EMAIL_AND_PWD = "SELECT * FROM users WHERE email = ? "
            + "and password = ?";

    private static final String DELETE_QUERY = "DELETE FROM users";
    private final BasicDataSource pool;

    public UserDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_QUERY)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(new User(it.getInt("id"), it.getString("email"),
                            it.getString("password")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return users;
    }


    public User add(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(CREATE_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                    return user;
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return null;
    }

    public void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE_QUERY)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    public User findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new User(it.getInt("id"), it.getString("email"),
                            it.getString("password"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return null;
    }

    public User findUserByEmailAndPwd(String email, String pwd) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_EMAIL_AND_PWD)
        ) {
            ps.setString(1, email);
            ps.setString(2, pwd);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new User(it.getInt("id"), it.getString("email"),
                            it.getString("password"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return null;
    }

    /*
    Данный метод используется при выполнении метода whenFindAllPosts() в тестах
     */
    public void clearTable() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_QUERY)
        ) {
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }
}
