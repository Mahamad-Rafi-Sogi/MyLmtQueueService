package com.rafi.lmt.controller;

import com.rafi.lmt.dto.*;
import com.rafi.lmt.model.*;
import com.rafi.lmt.service.LmtQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LmtQueueController {

    @Autowired
    private LmtQueueService service;

    @PostMapping("/enqueue")
    public ResponseEntity<?> enqueue(@RequestBody EnqueueRequest request) {
        service.enqueue(request);
        return ResponseEntity.ok("Enqueued");
    }

    @GetMapping("/retrieve/{lniata}")
    public ResponseEntity<?> retrieve(@PathVariable Long lniata) {
        return ResponseEntity.ok(service.retrieve(lniata));
    }

    @PostMapping("/dequeue/{elementId}")
    public ResponseEntity<?> dequeue(@PathVariable UUID elementId) {
        service.dequeue(elementId);
        return ResponseEntity.ok("Dequeued");
    }

    @PostMapping("/state-change/{lniata}")
    public ResponseEntity<?> changeState(@PathVariable Long lniata, @RequestBody QueueState state) {
        service.changeState(lniata, state);
        return ResponseEntity.ok("State updated");
    }


}