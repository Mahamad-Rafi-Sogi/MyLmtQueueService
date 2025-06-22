package com.rafi.lmt.service;

import com.rafi.lmt.dto.EnqueueRequest;
import com.rafi.lmt.model.*;
import java.util.UUID;

public interface LmtQueueService {
    void enqueue(EnqueueRequest request);
    LmtQueue retrieve(String lniata);
    void dequeue(UUID elementId);
    void changeState(String lniata, QueueState state);

    void dequeueHead(String lniata);

    void dequeueHead2(String lniata);

    void dequeueElement(String lniata, UUID elementId);
}