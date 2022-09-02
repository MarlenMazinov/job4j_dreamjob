package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDbStore {

    private static final Logger LOG = LoggerFactory.getLogger(PostDbStore.class.getName());
    private final BasicDataSource pool;

    public PostDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    int id = it.getInt("id");
                    Post post = new Post(id, it.getString("name"),
                            it.getBoolean("visible"),
                            null,
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime());
                    posts.add(setCity(post, id, cn));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return posts;
    }


    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name, visible, city, "
                             + "description, created) VALUES (?, ?, row(?,?), ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setBoolean(2, post.isVisible());
            ps.setObject(3, post.getCity().getId());
            ps.setObject(4, post.getCity().getName());
            ps.setString(5, post.getDescription());
            ps.setTimestamp(6, Timestamp.valueOf(post.getCreated()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE post SET name=?, visible=?,"
                     + "city=row(?,?), description=? WHERE id = ?")
        ) {
            ps.setString(1, post.getName());
            ps.setBoolean(2, post.isVisible());
            ps.setObject(3, post.getCity().getId());
            ps.setObject(4, post.getCity().getName());
            ps.setString(5, post.getDescription());
            ps.setInt(6, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    Post post = new Post(it.getInt("id"), it.getString("name"),
                            it.getBoolean("visible"), null,
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime());
                    return setCity(post, id, cn);
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
             PreparedStatement ps = cn.prepareStatement("DELETE FROM post")
        ) {
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    private Post setCity(Post post, int id, Connection cn) throws Exception {
        try (PreparedStatement psCity =
                     cn.prepareStatement("select (post.city).city_id, "
                             + "(post.city).city_name from post where post.id = ?")) {
            psCity.setInt(1, id);
            try (ResultSet itCity = psCity.executeQuery()) {
                if (itCity.next()) {
                    post.setCity(new City(itCity.getInt("city_id"),
                            itCity.getString("city_name")));
                }
            }
        }
        return post;
    }
}