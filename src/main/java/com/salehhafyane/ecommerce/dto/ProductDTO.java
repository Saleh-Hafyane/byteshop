package com.salehhafyane.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private long id;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String imageUrl;
    private int unitsInStock;
    private Date dateCreated;
    private Date lastUpdated;
    private Long categoryId;
    private String categoryName;
}
