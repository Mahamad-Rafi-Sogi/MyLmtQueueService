package com.rafi.lmt.service.impl;

import com.rafi.lmt.dto.EnqueueRequest;
import com.rafi.lmt.dto.LmtQueueDto;
import com.rafi.lmt.exception.QueueEmptyException;
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

        Optional<LmtQueue> optionalQueue = queueRepo.findByLniata(request.getLniata());
        if (optionalQueue.isEmpty()) {
            throw new IllegalArgumentException("invalid lniata");
        }
        LmtQueue queue = optionalQueue.get();

        if (queue.getState() == QueueState.STOPPED) {
            throw new QueueStoppedException("Queue is in stopped status");
        }

        if (queue.getState() == QueueState.HELD) {
            throw new IllegalStateException("Queue is in held status");
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
        return queueRepo.findByLniata(lniata).orElse(null);
    }

    @Override
    public void dequeue(UUID elementId) {
        Optional<LmtQueueElement> optElement = elementRepo.findById(elementId);
        if (optElement.isEmpty()) throw new NoSuchElementException("Element ID not found: " + elementId);

        LmtQueueElement element = optElement.get();
        LmtQueue queue = queueRepo.findByLniata(element.getLniata()).orElse(null);
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
    public void dequeueHead(String lniata) {
        LmtQueue queue = queueRepo.findByLniata(lniata)
                .orElseThrow(() -> new NoSuchElementException("lniata not found: " + lniata));
        if (queue.getState() == QueueState.STOPPED) {
            throw new QueueStoppedException("");
        }
        LmtQueueElement head = queue.getHead();
        if (head == null) {
            throw new NoSuchElementException("Queue head is null");
        }
        dequeue(head.getId());
    }

    @Override
    public void changeState(String lniata, QueueState state) {
        queueRepo.findByLniata(lniata).ifPresent(queue -> {
            queue.setState(state);
            queueRepo.save(queue);
        });
    }


    @Override
    public void dequeueHead2(String lniata) {
        LmtQueue queue = queueRepo.findByLniata(lniata)
                .orElseThrow(() -> new NoSuchElementException("lniata not found: " + lniata));

        LmtQueueElement head = queue.getHead();

        if (head == null) {
            throw new QueueEmptyException("Queue is empty: No Queue found for this lniata - " + lniata);
        }

        if(queue.getState() == QueueState.STOPPED) {
            throw new QueueStoppedException("Queue is in stopped status, cannot dequeue");
        }

        int messageCount = 1;
        LmtQueueElement temp = head;
        while (temp.getNext() != null) {
            messageCount++;
            temp = temp.getNext();
        }

        switch (queue.getState()) {
            case ACTIVE:
                dequeue(head.getId());
                if (messageCount > 1) {
                    // Transmit next message (need implement transmission logic here)
                }
                // Remain ACTIVE
                break;
            case HELD:
                dequeue(head.getId());
                break;

            case TEMP_HOLD:
                dequeue(head.getId());
                queue.setState(QueueState.ACTIVE);
                queueRepo.save(queue);
                if (messageCount > 1) {
                    // Transmit next message (need implement transmission logic here)
                }
                break;
        }
    }

    @Override
    public void dequeueElement(String lniata, UUID elementId) {
        LmtQueue queue = queueRepo.findByLniata(lniata)
                .orElseThrow(() -> new NoSuchElementException("lniata not found: " + lniata));

        if(queue.getState() == QueueState.STOPPED) {
            throw new QueueStoppedException("Queue is in STOPPED status, cannot dequeue");
        }

        dequeue(elementId);
    }

    public LmtQueue createLniata(LmtQueueDto dto) {
        LmtQueue queue = new LmtQueue();
        queue.setLniata(dto.getLniata());
        queue.setPrinterGatewayUrl(dto.getPrinterGatewayUrl());
        queue.setState(dto.getState() != null ? Enum.valueOf(com.rafi.lmt.model.QueueState.class, dto.getState()) : com.rafi.lmt.model.QueueState.ACTIVE);
        queue.setHead(null);
        queue.setTail(null);
        return queueRepo.save(queue);
    }

    public void deleteLniata(String lniata) {
        LmtQueue queue = queueRepo.findByLniata(lniata)
                .orElseThrow(() -> new NoSuchElementException("LNIATA not found: " + lniata));

        queueRepo.delete(queue);
    }

    public LmtQueue configureLniata(String lniata, LmtQueueDto dto) {
        if (lniata == null || lniata.isEmpty()) {
            throw new IllegalArgumentException("lniata is missing or empty");
        }
        if (dto.getPrinterGatewayUrl() == null || dto.getPrinterGatewayUrl().isEmpty()) {
            throw new IllegalArgumentException("printerGatewayUrl is missing or empty");
        }

        LmtQueue queue = queueRepo.findByLniata(lniata)
                .orElseThrow(() -> new NoSuchElementException("lniata not found: " + lniata));

        queue.setPrinterGatewayUrl(dto.getPrinterGatewayUrl());

        if (dto.getState() != null) {
            queue.setState(Enum.valueOf(com.rafi.lmt.model.QueueState.class, dto.getState()));
        }

        return queueRepo.save(queue);
    }

}