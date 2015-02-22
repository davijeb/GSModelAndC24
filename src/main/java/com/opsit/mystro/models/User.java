package com.opsit.mystro.models;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

import java.util.List;
import java.util.Map;

@SpaceClass
public class User {
    private Long id;
    private String name;
    private Double balance;
    private Double creditLimit;
    private EAccountStatus status;
    private Address address;
    private String[] comment;
    private Map<String, String> contacts;
    private List<Group> groups;
    private List<Integer> ratings;

    public User() {
    }

    @SpaceRouting
    @SpaceId(autoGenerate = false)
    public Long getId() {
        return id;
    }
}