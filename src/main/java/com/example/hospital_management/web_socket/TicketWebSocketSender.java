package com.example.hospital_management.web_socket;

import com.example.hospital_management.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketWebSocketSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendUpdatedWaitingList(List<Ticket> waitingTickets) {
        messagingTemplate.convertAndSend("/topic/waiting-list", waitingTickets); // 💡 riêng cho màn hình phòng chờ
    }

    public void sendNewTicket(Ticket ticket) {
        messagingTemplate.convertAndSend("/topic/new-ticket", ticket); // 💡 riêng cho receptionist
    }

    public void sendCurrentCalledTicket(Ticket ticket) {
        messagingTemplate.convertAndSend("/topic/current-ticket", ticket);
    }



}
