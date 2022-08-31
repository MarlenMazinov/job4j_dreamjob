package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CandidateDbStoreTest {
    @Test
    public void whenCreateCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Alexandr");
        store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenUpdateCandidate() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        Candidate firstCandidate = new Candidate(1, "Alexandr");
        int id = store.add(firstCandidate).getId();
        Candidate secondCandidate = new Candidate(id, "Alexandr Petrov");
        store.update(secondCandidate);
        Candidate candidateInDb = store.findById(id);
        assertThat(candidateInDb.getName(), is(secondCandidate.getName()));
    }

    @Test
    public void whenFindAllCandidates() {
        CandidateDbStore store = new CandidateDbStore(new Main().loadPool());
        store.clearTable();
        List<Candidate> addedList = List.of(new Candidate(0, "Alex"),
                new Candidate(1, "Olga"),
                new Candidate(2, "Igor"));
        addedList.forEach(store::add);
        List<Candidate> findedList = store.findAll();
        List<String> addedCandidatesNames = new ArrayList<>(3);
        addedList.forEach(candidate -> addedCandidatesNames.add(candidate.getName()));
        List<String> findedCandidatesNames = new ArrayList<>(3);
        findedList.forEach(candidate -> findedCandidatesNames.add(candidate.getName()));
        assertEquals(addedCandidatesNames, findedCandidatesNames);
    }
}
