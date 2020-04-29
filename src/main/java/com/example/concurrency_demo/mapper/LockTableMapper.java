package com.example.concurrency_demo.mapper;

import com.example.concurrency_demo.entity.Lock;
import com.example.concurrency_demo.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author jt
 * @date 2020-4-27
 */
@Component
public interface LockTableMapper extends Mapper<Lock>, MySqlMapper<Lock> {

    List<Order> selectLockForUpdate(@Param("name") String  name);
}
