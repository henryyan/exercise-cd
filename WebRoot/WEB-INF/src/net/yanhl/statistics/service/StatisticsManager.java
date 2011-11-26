package net.yanhl.statistics.service;

import java.util.Map;

import net.yanhl.base.service.BaseManager;

public interface StatisticsManager extends BaseManager {

	public Map<String, Object> getStatisticsDatas(Long venueId) throws Exception;

}