package com.rafi.lmt.service.impl;

import com.rafi.lmt.dto.EnqueueRequest;
import com.rafi.lmt.exception.QueueStoppedException;
import com.rafi.lmt.model.LmtQueue;
import com.rafi.lmt.model.LmtQueueElement;
import com.rafi.lmt.model.QueueState;
import com.rafi.lmt.repository.LmtQueueElementRepository;
import com.rafi.lmt.repository.LmtQueueRepository;
import com.rafi.lmt.service.LmtQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
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

        Optional<LmtQueue> optionalQueue = queueRepo.findById(request.getLniata());
        if (optionalQueue.isEmpty()) {
            throw new IllegalArgumentException("invalid lniata");
        }
        LmtQueue queue = optionalQueue.get();

        if (queue.getState() == QueueState.STOPPED) {
            throw new IllegalStateException("Queue is in stopped status");
        }

        if (queue.getState() == QueueState.HELD) {
            throw new QueueStoppedException("Queue is in held status");
        }

        LmtQueueElement newElement = new LmtQueueElement();
        newElement.setLniata(request.getLniata());
        newElement.setData(request.getData());
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

        queueRepo.save(queue);
    }

    @Override
    public LmtQueue retrieve(String lniata) {
        return queueRepo.findById(lniata).orElse(null);
    }

    @Override
    public void dequeue(UUID elementId) {
        Optional<LmtQueueElement> optElement = elementRepo.findById(elementId);
        if (optElement.isEmpty()) throw new NoSuchElementException("Element ID not found: " + elementId);;

        LmtQueueElement element = optElement.get();
        LmtQueue queue = queueRepo.findById(element.getLniata()).orElse(null);
        if (queue == null) return;

        if (queue != null && queue.getState() == QueueState.HELD) {
            throw new QueueStoppedException("Queue is in held status");
        }

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
    public void dequeueHead(String lniata) {
        LmtQueue queue = queueRepo.findById(lniata)
                .orElseThrow(() -> new NoSuchElementException("lniata not found: " + lniata));
        if (queue.getState() == QueueState.STOPPED) {
            throw new QueueStoppedException("Queue is in STOPPED status");
        }
        LmtQueueElement head = queue.getHead();
        if (head == null) {
            throw new NoSuchElementException("Queue head is null");
        }
        dequeue(head.getId());
    }

    @Override
    public void changeState(String lniata, QueueState state) {
        queueRepo.findById(lniata).ifPresent(queue -> {
            queue.setState(state);
            queueRepo.save(queue);
        });
    }
}