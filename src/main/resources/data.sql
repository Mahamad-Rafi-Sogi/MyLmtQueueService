-- Insert sample data into LmtQueue table
INSERT INTO lmt_queue (id, lniata, state, head_id, tail_id)
VALUES
(1, 123456, 'ACTIVE', NULL, NULL),
(2, 678910, 'STOPPED', NULL, NULL);

-- Insert sample data into LmtQueueElement table
-- INSERT INTO lmt_queue_element (id, lniata, data, previous_id, next_id)
-- VALUES
-- ('550e8400-e29b-41d4-a716-446655440000', 12345, 'D1', NULL, NULL),
-- ('550e8400-e29b-41d4-a716-446655440001', 67890, 'D2', NULL, NULL);

-- SELECT * FROM LMT_QUEUE ;
--
-- SELECT * FROM LMT_QUEUE_ELEMENT ;