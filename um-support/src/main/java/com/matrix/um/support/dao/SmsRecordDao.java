package com.matrix.um.support.dao;

import com.matrix.um.support.domain.SmsRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * @author yihaosun
 * @date 2022/7/5 17:53
 */
public interface SmsRecordDao extends CrudRepository<SmsRecord, Long> {
}