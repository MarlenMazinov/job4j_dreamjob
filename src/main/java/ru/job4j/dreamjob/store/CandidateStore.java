package ru.job4j.dreamjob.store;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class CandidateStore {

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private final AtomicInteger counter;

    public CandidateStore() {
        candidates.put(1, new Candidate(1, "Junior Java Developer",
                "Tech Stack : Java Core, Spring, Hibernate, Git, PostgreSQL."
                        + "Experience 1 year",
                new Date(2022, Calendar.MAY, 22, 17, 55)));
        candidates.put(2, new Candidate(2, "Middle Java Developer",
                "Stack: Java Core, Spring Framework, Hibernate, Kafka, MySQL, MongoDB"
                        + "Experience 3 years",
                new Date(2022, Calendar.JUNE, 11, 9, 22)));
        candidates.put(3, new Candidate(3, "Senior Java Job",
                "Java Core, Spring (Core, Boot, MVC, JPA, Security), Hibernate,"
                        + " Oracle/PostgreSQL/Redis, REST, SOAP, Kafka/RabbitMQ, Docker,"
                        + " Kubernetes/OpenShift, XML, JSON",
                new Date(2022, Calendar.JUNE, 5, 13, 0)));
        counter = new AtomicInteger(3);
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void add(Candidate candidate) {
        candidate.setId(counter.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
    }

    public void update(Candidate candidate) {
        candidates.replace(candidate.getId(), candidate);
    }
}