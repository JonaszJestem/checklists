package com.jonaszwiacek.checklists.Repositories;

import com.jonaszwiacek.checklists.Models.Checklist;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;


public interface ChecklistRepository extends CrudRepository<Checklist, Long> {
    List<Checklist> findAll();

    Checklist findByName(String name);

    @Transactional
    void deleteByName(String name);

    boolean existsByName(String name);
}
