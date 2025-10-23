CREATE OR REPLACE VIEW myview AS
SELECT m.id, m.category AS main_category, t.map_id, t.category AS map_category
FROM main_table m
JOIN map_table t ON m.map_id = t.map_id;