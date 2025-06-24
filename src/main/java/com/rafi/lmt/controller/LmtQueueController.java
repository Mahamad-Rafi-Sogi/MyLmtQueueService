package com.rafi.lmt.controller;

import com.rafi.lmt.dto.EnqueueRequest;
import com.rafi.lmt.dto.LmtQueueDto;
import com.rafi.lmt.dto.StateChangeRequest;
import com.rafi.lmt.model.LmtQueue;
import com.rafi.lmt.service.LmtQueueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class LmtQueueController {

    @Autowired
    private LmtQueueService service;

    @GetMapping("/hello")
    public ResponseEntity<?> greeting() {
        return ResponseEntity.ok("Welcome Rafi");
    }

    @PostMapping("/enqueue")
    public ResponseEntity<?> enqueue(@Valid @RequestBody EnqueueRequest request) {
        service.enqueue(request); // Exceptions will bubble to GlobalExceptionHandler
        return ResponseEntity.ok("Enqueued successfully");
    }

    @GetMapping("/retrieve/{lniata}")
    public ResponseEntity<?> retrieve(@PathVariable String lniata) {
        LmtQueue queue = service.retrieve(lniata);
        if (queue == null) {
            throw new IllegalArgumentException("Invalid LNIATA / LNIATA not found");
        }

        LmtQueueDto dto = new LmtQueueDto();
        dto.setLniata(queue.getLniata());
        dto.setHeadId(queue.getHead() != null ? queue.getHead().getId() : null);
        dto.setTailId(queue.getTail() != null ? queue.getTail().getId() : null);
        dto.setPrinterGatewayUrl(queue.getPrinterGatewayUrl());
        dto.setState(queue.getState().name());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/dequeue/{lniata}")
    public ResponseEntity<?> dequeue(
            @PathVariable String lniata,
            @RequestParam(required = false) UUID elementId) {

        if (elementId != null) {
            service.dequeueElement(lniata, elementId);
            return ResponseEntity.ok("Dequeued elementId successfully");
        } else {
            service.dequeueHead2(lniata);
            return ResponseEntity.ok("Dequeued head successfully");
        }
    }

    @PostMapping("/state-change")
    public ResponseEntity<?> changeState(@Valid @RequestBody StateChangeRequest request) {
        service.changeState(request.getLniata(), request.getState());
        return ResponseEntity.ok("Status updated successfully");
    }
}
