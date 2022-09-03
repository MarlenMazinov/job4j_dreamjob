package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
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
    private static final String SELECT_QUERY = "SELECT * FROM post";
    private static final String CREATE_QUERY = "INSERT INTO post(name, visible, city_id, "
            + "description, created) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE post SET name=?, visible=?,"
            + "city_id=?, description=? WHERE id = ?";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM post WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM post";
    private final BasicDataSource pool;

    public PostDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_QUERY)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    int id = it.getInt("id");
                    posts.add(new Post(id, it.getString("name"),
                            it.getBoolean("visible"),
                            it.getInt("city_id"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime()));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return posts;
    }


    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(CREATE_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setBoolean(2, post.isVisible());
            ps.setInt(3, post.getCityId());
            ps.setString(4, post.getDescription());
            ps.setTimestamp(5, Timestamp.valueOf(post.getCreated()));
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
             PreparedStatement ps = cn.prepareStatement(UPDATE_QUERY)
        ) {
            ps.setString(1, post.getName());
            ps.setBoolean(2, post.isVisible());
            ps.setInt(3, post.getCityId());
            ps.setString(4, post.getDescription());
            ps.setInt(5, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_QUERY)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(it.getInt("id"), it.getString("name"),
                            it.getBoolean("visible"), it.getInt("city_id"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime());
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