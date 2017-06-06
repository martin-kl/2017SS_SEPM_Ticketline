SELECT e.name, COUNT(t.id) as sold_tickets
FROM event e, ticket t, performance p
WHERE
t.performance_id = p.id AND
p.event_id = e.id AND
(e.category = ?1 OR ?1 = "")
EXISTS (SELECT * FROM
            ticket_history th, ticket_transaction tt
            WHERE
            t.id = th.ticket_id AND
            tt.id = th.ticket_transaction_id AND
            tt.status = 'bought' AND
            last_modified_at > ?2
            ORDER BY th.last_modified_at DESC
            LIMIT 1)

GROUP BY e.id
ORDER BY sold_tickets DESC
LIMIT 10;

SELECT e.name, COUNT(t.id) as sold_tickets
FROM event e, ticket t, performance p, ticket_transaction tt, ticket_history th
WHERE
t.performance_id = p.id AND
p.event_id = e.id AND
t.id = th.ticket_id AND
tt.id = th.ticket_transaction_id AND
tt.status = 'bought' AND
tt.outdated = false AND

(e.category = ?1 OR ?1 = "")
EXISTS (SELECT * FROM
            ticket_history th, ticket_transaction tt
            WHERE
            t.id = th.ticket_id AND
            tt.id = th.ticket_transaction_id AND
            tt.status = 'bought' AND
            last_modified_at > ?2
            ORDER BY th.last_modified_at DESC
            LIMIT 1)

GROUP BY e.id
ORDER BY sold_tickets DESC
LIMIT 10;
