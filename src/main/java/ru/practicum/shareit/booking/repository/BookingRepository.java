package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId ORDER BY b.startDate DESC ")
    List<Booking> findAllForBooker(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.endDate < :date ORDER BY b.id DESC ")
    List<Booking> findPastBookingsForBooker(Long bookerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.startDate < :date AND b.endDate > :date " +
            "ORDER BY b.id ")
    List<Booking> findCurrentBookingsForBooker(Long bookerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.startDate > :date ORDER BY b.startDate DESC ")
    List<Booking> findFutureBookingsForBooker(Long bookerId, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = 'WAITING' ORDER BY b.startDate DESC ")
    List<Booking> findWaitingBookingsForBooker(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status = 'REJECTED' ORDER BY b.startDate DESC ")
    List<Booking> findRejectedBookingsForBooker(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:bookerIds) ORDER BY b.startDate DESC ")
    List<Booking> findAllForItems(List<Long> bookerIds);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.endDate < :date ORDER BY b.id DESC ")
    List<Booking> findPastBookingsForItems(List<Long> itemIds, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.startDate < :date AND b.endDate > :date " +
            "ORDER BY b.id ")
    List<Booking> findCurrentBookingsForItems(List<Long> itemIds, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.startDate > :date ORDER BY b.startDate DESC ")
    List<Booking> findFutureBookingsForItems(List<Long> itemIds, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.status = 'WAITING' ORDER BY b.startDate DESC ")
    List<Booking> findWaitingBookingsForItems(List<Long> itemIds);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN (:itemIds) AND b.status = 'REJECTED' ORDER BY b.startDate DESC ")
    List<Booking> findRejectedBookingsForItems(List<Long> itemIds);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.startDate < :date AND b.status = 'APPROVED' " +
            "ORDER BY b.startDate DESC ")
    List<Booking> findLastBookingForItem(Long itemId, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.startDate > :date AND b.status = 'APPROVED' " +
            "ORDER BY b.startDate ")
    List<Booking> findNextBookingForItem(Long itemId, LocalDateTime date);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.booker.id = :userId AND b.endDate < :date")
    List<Booking> findBookingsForAddComments(Long itemId, Long userId, LocalDateTime date);

}