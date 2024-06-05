CREATE VIEW myview AS
SELECT vp.id  as VP_ID , vp.category AS VP_category , vp.map_id AS VP_mappid,
   vpm.mainid AS VPM_mainid ,  vpm.category AS VPM_category 
FROM viewpractice vp
INNER JOIN viewpractice_mappid vpm
ON vp.map_id = vpm.mainid;
