package com.travelbuddy.demo.RestController;

import com.travelbuddy.demo.AdapterClasses.TripMember;
import com.travelbuddy.demo.AdapterClasses.TripMembersDTO;
import com.travelbuddy.demo.AdapterClasses.TripRole;
import com.travelbuddy.demo.Entities.Trips;
import com.travelbuddy.demo.Entities.UserMessage;
import com.travelbuddy.demo.Services.TripsService;
import com.travelbuddy.demo.Services.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private TripsService tripsService;

    @PostMapping("/save")
    public ResponseEntity<UserMessage> saveMessage(@RequestBody UserMessage userMessage) {
        userMessage.setInitiatorUsername(getCurrentUsername());

        if(userMessage.getType().equals("userLeft_trip")) {
            Optional<Trips> optionalTrip = tripsService.getTripById(userMessage.getTripId());
            if (optionalTrip.isPresent()) {
                Trips trip = optionalTrip.get();
                String tripOrganizer = trip.getMembers()
                        .stream()
                        .filter(member -> member.getRole().equals(TripRole.Organizer.getDescription()))
                        .findFirst()
                        .map(TripMember::getUsername)
                        .orElse(null);
                userMessage.setUsername(tripOrganizer);
            }
        }else if(userMessage.getType().equals("tripDeleted_trip")){
            Optional<Trips> optionalTrip = tripsService.getTripById(userMessage.getTripId());
            if (optionalTrip.isPresent()) {
                Trips trip = optionalTrip.get();
                for (TripMember member : trip.getMembers()) {
                    UserMessage newMessage = new UserMessage(
                            UUID.randomUUID().toString(),
                            userMessage.getType(),
                            userMessage.getTripId(),
                            member.getUsername(),
                            userMessage.getInitiatorUsername(),
                            userMessage.getNameOfTrip(),
                            userMessage.getRoleIfRoleChange()
                    );
                    userMessageService.saveOrUpdateMessage(newMessage);
                }
            }
        }
        userMessage.setNameOfTrip(tripsService.getTripById(userMessage.getTripId()).get().getNameTrip());
        if(!(userMessage.getType().equals("tripDeleted_trip"))){
           userMessageService.saveOrUpdateMessage(userMessage);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping("/update")
    public ResponseEntity<UserMessage> updateMessage(@RequestBody UserMessage userMessage) {
        UserMessage updatedMessage = userMessageService.saveOrUpdateMessage(userMessage);
        return ResponseEntity.ok(updatedMessage);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserMessage>> getAllUserMessages() {
        String loggedInUser = getCurrentUsername();
        List<UserMessage> messages = userMessageService.getAllMessagesByUsername(loggedInUser).orElse(null);

        if (messages != null && !messages.isEmpty()) {
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable String messageId) {
        userMessageService.deleteMessage(messageId);
        return ResponseEntity.ok("Message deleted successfully");
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
