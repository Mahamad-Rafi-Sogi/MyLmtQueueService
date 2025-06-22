package com.rafi.lmt.controller;

import com.rafi.lmt.exception.QueueStoppedException;
import com.rafi.lmt.model.LmtQueue;
import com.rafi.lmt.model.LmtQueueElement;
import com.rafi.lmt.model.QueueState;
import com.rafi.lmt.service.LmtQueueService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LmtQueueController.class)
class LmtQueueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private LmtQueueService service;

    @Test
    void greeting_returnsWelcome() throws Exception {
        mockMvc.perform(get("/api/v1/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome Rafi"));
    }

    @Test
    void enqueue_validRequest_returnsOk() throws Exception {
        String json = "{\"lniata\":\"ABC123\",\"data\":\"testdata\"}";
        mockMvc.perform(post("/api/v1/enqueue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Enqueued successfully"));
    }

    @Test
    void retrieve_found_returnsDto() throws Exception {
        LmtQueue queue = new LmtQueue();
        queue.setLniata("ABC123");
        queue.setState(QueueState.ACTIVE);
        queue.setPrinterGatewayUrl("url");
        LmtQueueElement head = new LmtQueueElement();
        head.setId(UUID.randomUUID());
        queue.setHead(head);
        queue.setTail(head);

        Mockito.when(service.retrieve("ABC123")).thenReturn(queue);

        mockMvc.perform(get("/api/v1/retrieve/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lniata").value("ABC123"));
    }

    @Test
    void dequeue_withElementId_success() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(post("/api/v1/dequeue/ABC123")
                        .param("elementId", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Dequeued successfully"));
    }

    @Test
    void dequeueHead_queueStopped_returns423() throws Exception {
        Mockito.doThrow(new QueueStoppedException("Queue is in stopped status"))
                .when(service).dequeueHead2("ABC123");

        mockMvc.perform(post("/api/v1/dequeue/ABC123"))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.message").value("Queue is in stopped status"));
    }

    @Test
    void dequeueHead_notFound_returns404() throws Exception {
        Mockito.doThrow(new NoSuchElementException("ID not found"))
                .when(service).dequeueHead2("ABC123");

        mockMvc.perform(post("/api/v1/dequeue/ABC123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ID not found"));
    }

    @Test
    void changeState_success_returnsOk() throws Exception {
        String json = "{\"lniata\":\"ABC123\",\"state\":\"ACTIVE\"}";
        mockMvc.perform(post("/api/v1/state-change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Status updated successfully"));
    }
}