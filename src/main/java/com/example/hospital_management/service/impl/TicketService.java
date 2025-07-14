package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Ticket;
import com.example.hospital_management.repository.ITicketRepository;
import com.example.hospital_management.service.ITicketService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements ITicketService {
    private ITicketRepository ticketRepository;

    public TicketService(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket registerTicket(String name, String idCard, String phone, String email, LocalDate date, boolean isPriority) {
        int nextQueue;
        if (isPriority) {
            long priorityCount = ticketRepository.countPriorityByDate(date);
            nextQueue = (int) (priorityCount + 1);
        } else {
            long totalCount = ticketRepository.countAllByDate(date);
            nextQueue = (int) (totalCount + 1);
        }
        Ticket ticket = new Ticket();
        ticket.setName(name);
        ticket.setIdCard(idCard);
        ticket.setPhone(phone);
        ticket.setEmail(email);
        ticket.setAppointmentDate(date);
        ticket.setQueueNumber(nextQueue);
        ticket.setPriority(isPriority);
        ticket.setCreatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket callTickets(Long ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            ticket.setCalled(true);
             return ticketRepository.save(ticket);
        }
        return null;
    }

    @Override
    public List<Ticket> getAllTodayTicketsOrdered() {
        return ticketRepository.findAllTodayTicketsOrdered(LocalDate.now());
    }

    @Override
    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ticket> findWaitingTicketsToday() {
        return ticketRepository.findWaitingTicketsToday(LocalDate.now());
    }
}
