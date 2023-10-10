package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i " +
            "WHERE i.available = true " +
            "AND (upper(i.name) LIKE upper(concat('%', :text, '%')) " +
            "    OR upper(i.description) LIKE upper(concat('%', :text, '%')))")
    List<Item> search(String text, Pageable pageable);

    List<Item> findByRequestIdIn(List<Long> requestId);

    List<Item> findByRequestId(Long requestId);

}
