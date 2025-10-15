package com.cartmodel.cartmodel.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class Userdto {
    private UUID userId;
    private String userName;
}
