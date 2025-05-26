package com.rafi.lmt.service.impl;

import com.rafi.lmt.dto.EnqueueRequest;
import com.rafi.lmt.model.LmtQueue;
import com.rafi.lmt.model.LmtQueueElement;
import com.rafi.lmt.model.QueueState;
import com.rafi.lmt.repository.LmtQueueElementRepository;
import com.rafi.lmt.repository.LmtQueueRepository;
import com.rafi.lmt.service.LmtQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class LmtQueueServiceImpl implements LmtQueueService {

    @Autowired
    private LmtQueueRepository queueRepo;

    @Autowired
    private LmtQueueElementRepository elementRepo;

    @Override
    public void enqueue(EnqueueRequest request) {
        LmtQueue queue = queueRepo.findById(request.lniata).orElseGet(() -> {
            LmtQueue newQueue = new LmtQueue();
            newQueue.setLniata(request.lniata);
            newQueue.setState(QueueState.ACTIVE);
            return queueRepo.save(newQueue);
        });

        LmtQueueElement newElement = new LmtQueueElement();
        newElement.setLniata(request.lniata);
        newElement.setData(Arrays.toString(request.data));
        newElement = elementRepo.save(newElement);

        if (queue.getHead() == null) {
            queue.setHead(newElement);
            queue.setTail(newElement);
        } else {
            LmtQueueElement previousTail = queue.getTail();
            previousTail.setNext(newElement);
            newElement.setPrevious(previousTail);
            elementRepo.save(previousTail);
            queue.setTail(newElement);
        }

        queue.setTail(newElement);
        queueRepo.save(queue);
    }

    @Override
    public LmtQueue retrieve(Long lniata) {
        return queueRepo.findById(lniata).orElse(null);
    }

    @Override
    public void dequeue(UUID elementId) {
        Optional<LmtQueueElement> optElement = elementRepo.findById(elementId);
        if (optElement.isEmpty()) return;

        LmtQueueElement element = optElement.get();
        LmtQueue queue = queueRepo.findById(element.getLniata()).orElse(null);
        if (queue == null) return;

        if (element.equals(queue.getHead())) {
            queue.setHead(element.getNext());
        }
        if (element.equals(queue.getTail())) {
            queue.setTail(element.getPrevious());
        }

        if (element.getPrevious() != null) {
            element.getPrevious().setNext(element.getNext());
            elementRepo.save(element.getPrevious());
        }

        if (element.getNext() != null) {
            element.getNext().setPrevious(element.getPrevious());
            elementRepo.save(element.getNext());
        }

        elementRepo.delete(element);
        queueRepo.save(queue);
    }

    @Override
    public void changeState(Long lniata, QueueState state) {
        queueRepo.findById(lniata).ifPresent(queue -> {
            queue.setState(state);
            queueRepo.save(queue);
        });
    }
}