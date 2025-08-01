package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ITicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = "select count (t) from Ticket t where t.appointmentDate = :date and t.isPriority = true ")
    long countPriorityByDate(@Param("date") LocalDate date);

    @Query(value = "select count (t) from Ticket t where t.appointmentDate=:date")
    long countAllByDate(@Param("date") LocalDate date);

    @Query("SELECT t FROM Ticket t WHERE t.appointmentDate = :today ORDER BY t.isCalled ASC, t.queueNumber DESC")
    Page<Ticket> findAllTodayTicketsOrdered(@Param("today") LocalDate today, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.isCalled = false AND t.appointmentDate = :today")
    List<Ticket> findWaitingTicketsToday(@Param("today") LocalDate today);

}
