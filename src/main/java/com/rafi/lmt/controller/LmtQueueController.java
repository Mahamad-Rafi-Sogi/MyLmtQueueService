package com.rafi.lmt.controller;

import com.rafi.lmt.dto.*;
import com.rafi.lmt.exception.QueueHeldException;
import com.rafi.lmt.model.*;
import com.rafi.lmt.service.LmtQueueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LmtQueueController {

    @Autowired
    private LmtQueueService service;

    @GetMapping("/hello")
    public ResponseEntity<?> greeting() {
        return ResponseEntity.ok("Welcome Rafi");
    }

    @PostMapping("/enqueue")
    public ResponseEntity<?> enqueue(@Valid @RequestBody EnqueueRequest request) {
        service.enqueue(request);
        return ResponseEntity.ok("Enqueued successfully");
    }

    @GetMapping("/retrieve/{lniata}")
    public LmtQueueDto retrieve(@PathVariable Long lniata) {

        LmtQueue queue = service.retrieve(lniata);

        // Map entity to DTO
        LmtQueueDto dto = new LmtQueueDto();
        dto.setLniata(queue.getLniata());
        dto.setHeadId(queue.getHead() != null ? queue.getHead().getId() : null);
        dto.setTailId(queue.getTail() != null ? queue.getTail().getId() : null);
        dto.setPrinterGatewayUrl(queue.getPrinterGatewayUrl());
        dto.setState(queue.getState().name());

        return dto;
    }

    @PostMapping("/dequeue/{lniata}")
    public ResponseEntity<?> dequeue(
            @PathVariable Long lniata,
            @RequestParam(required = false) UUID elementId) {
        try {
            if (elementId != null) {
                service.dequeue(elementId);
            } else {
                service.dequeueHead(lniata);
            }
            return ResponseEntity.ok("Dequeued successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("ID not found");
        } catch (QueueHeldException e) {
            return ResponseEntity.status(423).body("Queue is in held status");
        }
    }

    @PostMapping("/state-change")
    public ResponseEntity<?> changeState(@RequestBody StateChangeRequest request) {
        service.changeState(request.getLniata(), request.getState());
        return ResponseEntity.ok("Status updated successfully");
    }


}