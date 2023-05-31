package com.revature.eMarket.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Product {
    private String id;
    private String name;
    private float price;
    private int stock;
    private String category_id;
}