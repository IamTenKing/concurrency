package com.example.concurrency_demo.entity;

import lombok.Data;

import javax.persistence.Table;

/**
 * @author jt
 * @date 2020-4-29
 */

@Data
@Table(name="lock_table")
public class Lock {

    private String name;
    private String id;
    private String description;
}
