package com.example.concurrency_demo.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author jt
 * @date 2020-4-27
 */

@Table(name="os_order")
@Data
public class Order implements Serializable {


    @Id
    private Long id;

    private String name;
    private Long num;

    private int version;
}
