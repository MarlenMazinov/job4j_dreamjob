package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CandidateDbStoreTest {
    @Test
    public void whenCreateCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Alexandr", new byte[0],
                "description of Alexandr", LocalDateTime.now());
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertEquals(candidateInDb, candidate);
    }

    @Test
    public void whenUpdateCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        LocalDateTime created = LocalDateTime.now();
        Candidate firstCandidate = new Candidate(1, "Alexandr", new byte[0],
                "description of Alexandr", created);
        int id = store.add(firstCandidate).getId();
        Candidate secondCandidate = new Candidate(id, "Alexandr Petrov", new byte[0],
                "description of Alexandr", created);
        store.update(secondCandidate);
        Candidate candidateInDb = store.findById(id);
        assertEquals(candidateInDb, secondCandidate);
    }

    @Test
    public void whenFindAllCandidates() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        store.clearTable();
        List<Candidate> addedList = List.of(new Candidate(0, "Alex", new byte[0],
                        "Alex descr", LocalDateTime.now()),
                new Candidate(1, "Olga", new byte[0], "Olga descr", LocalDateTime.now()),
                new Candidate(2, "Igor", new byte[0], "Igor descr", LocalDateTime.now()));
        addedList.forEach(store::add);
        List<Candidate> findedList = store.findAll();
        boolean result = true;
        for (int i = 0; i < 3; i++) {
            if (!addedList.get(i).equals(findedList.get(i))) {
                result = false;
                break;
            }
        }
        assertTrue(result);
    }
}
