package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId")
    List<Booking> findAllForBooker(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.endDate < :date ORDER BY b.id DESC ")
    List<Booking> findPastBookingsForBooker(Long bookerId, LocalDateTime date, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.startDate < :date AND b.endDate > :date " +
            "ORDER BY b.id ")
    List<Booking> findCurrentBookingsForBooker(Long bookerId, LocalDateTime date, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.startDate > :date")
    List<Booking> findFutureBookingsForBooker(Long bookerId, LocalDateTime date, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = 'WAITING'")
    List<Booking> findWaitingBookingsForBooker(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = 'REJECTED'")
    List<Booking> findRejectedBookingsForBooker(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:bookerIds)")
    List<Booking> findAllForItems(List<Long> bookerIds, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.endDate < :date")
    List<Booking> findPastBookingsForItems(List<Long> itemIds, LocalDateTime date, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.startDate < :date AND b.endDate > :date " +
            "ORDER BY b.id ")
    List<Booking> findCurrentBookingsForItems(List<Long> itemIds, LocalDateTime date, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.startDate > :date")
    List<Booking> findFutureBookingsForItems(List<Long> itemIds, LocalDateTime date, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.status = 'WAITING'")
    List<Booking> findWaitingBookingsForItems(List<Long> itemIds, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.status = 'REJECTED'")
    List<Booking> findRejectedBookingsForItems(List<Long> itemIds, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.startDate < :date AND b.status = 'APPROVED' " +
            "ORDER BY b.startDate DESC ")
    List<Booking> findLastBookingForItem(Long itemId, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.startDate > :date AND b.status = 'APPROVED' " +
            "ORDER BY b.startDate ")
    List<Booking> findNextBookingForItem(Long itemId, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.booker.id = :userId AND b.endDate < :date")
    List<Booking> findBookingsForAddComments(Long itemId, Long userId, LocalDateTime date);

}