package com.example.hospital_management.service;

import com.example.hospital_management.entity.Ticket;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ITicketService {
    Ticket registerTicket(String name,String idCard,String phone,String email,LocalDate date,boolean isPriority);
    Ticket callTickets(Long ticketId);
    List<Ticket> getAllTodayTicketsOrdered();
    Ticket findById(Long id);
    List<Ticket> findWaitingTicketsToday();
}
