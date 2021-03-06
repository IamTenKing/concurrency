package com.example.concurrency_demo.mapper;

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
public interface OrderMapper extends Mapper<Order>, MySqlMapper<Order> {

    List<Order> selectOrderForUpdate(@Param("n")Long n);

    Order selectMax();

    void insertOrder(Order order1, int version);
}
