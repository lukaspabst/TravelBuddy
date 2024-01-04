package com.travelbuddy.demo.RestController;

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
        userMessage.setNameOfTrip(tripsService.getTripById(userMessage.getTripId()).get().getNameTrip());
        UserMessage savedMessage = userMessageService.saveOrUpdateMessage(userMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
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
