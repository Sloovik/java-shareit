package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.owner.id = :ownerId ORDER BY i.id ")
    List<Item> findByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE i.available = true " +
            "AND (upper(i.name) LIKE upper(concat('%', :text, '%')) " +
            "    OR upper(i.description) LIKE upper(concat('%', :text, '%')))")
    List<Item> search(String text);

}
