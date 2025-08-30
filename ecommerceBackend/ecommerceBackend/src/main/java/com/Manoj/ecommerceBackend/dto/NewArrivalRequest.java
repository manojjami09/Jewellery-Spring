package com.Manoj.ecommerceBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewArrivalRequest {
    private String image;
    private String name;
    private Integer price;
}
