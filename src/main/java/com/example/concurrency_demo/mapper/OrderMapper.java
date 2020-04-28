package com.example.concurrency_demo.mapper;

import com.example.concurrency_demo.entity.Order;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author jt
 * @date 2020-4-27
 */
@Component
public interface OrderMapper extends Mapper<Order>, MySqlMapper<Order> {
}
