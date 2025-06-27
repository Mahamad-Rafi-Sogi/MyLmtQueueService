package com.rafi.lmt.controller;

import com.rafi.lmt.dto.*;
import com.rafi.lmt.model.LmtQueue;
import com.rafi.lmt.service.LmtQueueService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
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

    @Hidden
    @GetMapping("/hello")
    public ResponseEntity<ApiResponse> greeting(HttpServletRequest request) {
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Welcome Rafi", request.getRequestURI())
        );
    }

    @Operation(summary = "Enqueue", tags = {"PosQSystem"})
    @PostMapping("/enqueue")
    public ResponseEntity<ApiResponse> enqueue(@Valid @RequestBody EnqueueRequest requestBody, HttpServletRequest request) {
        service.enqueue(requestBody);
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Enqueued successfully", request.getRequestURI())
        );
    }

    @Operation(summary = "Retrieve", tags = {"PrinterGatewaySystem"})
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

    @Operation(summary = "Dequeue", tags = {"PrinterGatewaySystem"})
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

    @Operation(summary = "Status Update", tags = {"PrinterGatewaySystem"})
    @PostMapping("/state-change")
    public ResponseEntity<ApiResponse> changeState(@Valid @RequestBody StateChangeRequest requestBody, HttpServletRequest request) {
        service.changeState(requestBody.getLniata(), requestBody.getState());
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Status updated successfully", request.getRequestURI())
        );
    }

    @Operation(summary = "Create Lniata", tags = {"ProvisionSystem"})
    @PostMapping("/provision/create")
    public ResponseEntity<ApiResponse> createQueue(@Valid @RequestBody LmtQueueDto dto, HttpServletRequest request) {
        LmtQueue created = service.createLniata(dto);
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Lniata created successfully", request.getRequestURI())
        );
    }

    @Operation(summary = "Delete Lniata", tags = {"ProvisionSystem"})
    @DeleteMapping("/provision/delete")
    public ResponseEntity<ApiResponse> deleteQueue(@Valid @RequestBody DeleteQueueRequest deleteRequest, HttpServletRequest request) {
        service.deleteLniata(deleteRequest.getLniata());
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Lniata deleted successfully", request.getRequestURI())
        );
    }

    @Operation(summary = "Configure Lniata", tags = {"ProvisionSystem"})
    @PutMapping("/provision/configure")
    public ResponseEntity<ApiResponse> configureQueue(@Valid @RequestBody LmtQueueDto dto, HttpServletRequest request) {
        LmtQueue updated = service.configureLniata(dto.getLniata(), dto);
        return ResponseEntity.ok(
                new ApiResponse(LocalDateTime.now(), 200, null, "Lniata configured successfully", request.getRequestURI())
        );
    }
}