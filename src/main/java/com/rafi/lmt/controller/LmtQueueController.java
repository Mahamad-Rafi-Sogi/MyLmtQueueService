package com.rafi.lmt.controller;

import com.rafi.lmt.dto.ApiResponse;
import com.rafi.lmt.dto.EnqueueRequest;
import com.rafi.lmt.dto.LmtQueueDto;
import com.rafi.lmt.dto.StateChangeRequest;
import com.rafi.lmt.model.LmtQueue;
import com.rafi.lmt.service.LmtQueueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class LmtQueueController {

    @Autowired
    private LmtQueueService service;

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse> greeting(HttpServletRequest request) {
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Welcome Rafi", request.getRequestURI())
        );
    }

    @PostMapping("/enqueue")
    public ResponseEntity<ApiResponse> enqueue(@Valid @RequestBody EnqueueRequest requestBody, HttpServletRequest request) {
        service.enqueue(requestBody);
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Enqueued successfully", request.getRequestURI())
        );
    }

    @GetMapping("/retrieve/{lniata}")
    public ResponseEntity<LmtQueueDto> retrieve(@PathVariable String lniata) {
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
    public ResponseEntity<ApiResponse> dequeue(
            @PathVariable String lniata,
            @RequestParam(required = false) UUID elementId,
            HttpServletRequest request) {

        if (elementId != null) {
            service.dequeueElement(lniata, elementId);
            return ResponseEntity.ok(
                    new ApiResponse(LocalDateTime.now(), 200, null, "Dequeued elementId successfully", request.getRequestURI())
            );
        } else {
            service.dequeueHead2(lniata);
            return ResponseEntity.ok(
                    new ApiResponse(LocalDateTime.now(), 200, null, "Dequeued head successfully", request.getRequestURI())
            );
        }
    }

    @PostMapping("/state-change")
    public ResponseEntity<ApiResponse> changeState(@Valid @RequestBody StateChangeRequest requestBody, HttpServletRequest request) {
        service.changeState(requestBody.getLniata(), requestBody.getState());
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Status updated successfully", request.getRequestURI())
        );
    }

    @PostMapping("/provision")
    public ResponseEntity<ApiResponse> createQueue(@RequestBody LmtQueueDto dto, HttpServletRequest request) {
        LmtQueue created = service.createLniata(dto);
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Queue created successfully", request.getRequestURI())
        );
    }

    @DeleteMapping("/provision/{lniata}")
    public ResponseEntity<ApiResponse> deleteQueue(@PathVariable String lniata, HttpServletRequest request) {
        service.deleteLniata(lniata);
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Queue deleted successfully", request.getRequestURI())
        );
    }

    @PutMapping("/provision/configure/{lniata}")
    public ResponseEntity<ApiResponse> configureQueue(@PathVariable String lniata, @RequestBody LmtQueueDto dto, HttpServletRequest request) {
        LmtQueue updated = service.configureLniata(lniata, dto);
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Queue configured successfully", request.getRequestURI())
        );
    }
}