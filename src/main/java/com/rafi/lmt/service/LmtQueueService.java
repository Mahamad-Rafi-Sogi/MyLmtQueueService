package com.rafi.lmt.service;

import com.rafi.lmt.dto.EnqueueRequest;
import com.rafi.lmt.model.*;
import java.util.UUID;

public interface LmtQueueService {
    void enqueue(EnqueueRequest request);
    LmtQueue retrieve(Long lniata);
    void dequeue(UUID elementId);
    void changeState(Long lniata, QueueState state);

    void dequeueHead(Long lniata);
}