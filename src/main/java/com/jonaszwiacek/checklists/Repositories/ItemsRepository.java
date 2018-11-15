package com.jonaszwiacek.checklists.Repositories;

import com.jonaszwiacek.checklists.Models.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemsRepository extends CrudRepository<Item, Long> {
    @Query(value = "SELECT item FROM Items item WHERE item.id = itemId")
    Item findByIdAndChecklistId(long itemId, long checklistId);
}
