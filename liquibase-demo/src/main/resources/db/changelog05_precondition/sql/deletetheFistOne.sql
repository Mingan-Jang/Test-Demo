DELETE p1
FROM precondition p1
JOIN (SELECT MAX(id) AS max_id FROM precondition) AS p2
ON p1.id = p2.max_id;