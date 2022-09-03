package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDbStore {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateDbStore.class.getName());
    private static final String SELECT_QUERY = "SELECT * FROM candidate";
    private static final String CREATE_QUERY = "INSERT INTO candidate(name, photo,"
            + " description, created) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE candidate SET name=?, photo=?,"
            + "description=? WHERE id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM candidate WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM candidate";
    private final BasicDataSource pool;

    public CandidateDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_QUERY)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name"),
                            it.getBytes("photo"), it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime()));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return candidates;
    }


    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(CREATE_QUERY,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setBytes(2, candidate.getPhoto());
            ps.setString(3, candidate.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(candidate.getCreated()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE_QUERY)) {
            ps.setString(1, candidate.getName());
            ps.setBytes(2, candidate.getPhoto());
            ps.setString(3, candidate.getDescription());
            ps.setInt(4, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(it.getInt("id"), it.getString("name"),
                            it.getBytes("photo"), it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime());
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return null;
    }

    /*
    Данный метод используется при выполнении метода whenFindAllCandidates() в тестах
     */
    public void clearTable() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_QUERY)
        ) {
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}