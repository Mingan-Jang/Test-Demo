package com.event.dao.clickhouse;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.event.po.clickhouse.EventHistoryPO;

@Repository
public class EventHistoryDAO {

	@Autowired
	@Qualifier("clickHouseJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public void save(EventHistoryPO po) {
		String sql = "INSERT INTO event_history "
				+ "(event_id, event_type, priority, description, soce, message_id, occur_time, create_time, update_time, creator, updater) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, po.getEventId(), po.getEventType(), po.getPriority(), po.getDescription(),
				po.getSoce(), po.getMessageId(), po.getOccurTime(), po.getCreateTime(), po.getUpdateTime(),
				po.getCreator(), po.getUpdater());
	}

	public List<EventHistoryPO> findAll() {
		String sql = "SELECT event_id, event_type, priority, description, soce, message_id, occur_time, create_time, update_time, creator, updater FROM event_history";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			EventHistoryPO po = new EventHistoryPO();
			po.setEventId(rs.getString("event_id"));
			po.setEventType(rs.getString("event_type"));
			po.setPriority(rs.getString("priority"));
			po.setDescription(rs.getString("description"));
			po.setSoce(rs.getString("soce"));
			po.setMessageId(rs.getString("message_id"));
			po.setOccurTime(rs.getTimestamp("occur_time").toLocalDateTime());
			po.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
			po.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
			po.setCreator(rs.getString("creator"));
			po.setUpdater(rs.getString("updater"));
			return po;
		});
	}

	public List<EventHistoryPO> findByOccurTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
		String sql = """
				SELECT event_id, event_type, priority, description, soce, message_id,
				       occur_time, create_time, update_time, creator, updater
				FROM event_history
				WHERE occur_time BETWEEN ? AND ?
				""";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			EventHistoryPO po = new EventHistoryPO();
			po.setEventId(rs.getString("event_id"));
			po.setEventType(rs.getString("event_type"));
			po.setPriority(rs.getString("priority"));
			po.setDescription(rs.getString("description"));
			po.setSoce(rs.getString("soce"));
			po.setMessageId(rs.getString("message_id"));
			po.setOccurTime(rs.getTimestamp("occur_time").toLocalDateTime());
			po.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
			po.setUpdateTime(rs.getTimestamp("update_time").toLocalDateTime());
			po.setCreator(rs.getString("creator"));
			po.setUpdater(rs.getString("updater"));
			return po;
		}, startTime, endTime);
	}
}
