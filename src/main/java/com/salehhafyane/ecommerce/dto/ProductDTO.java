// Product as seen by the admin forms: all fields plus flattened category info.
package com.salehhafyane.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

// Flat JSON version of Product; the nested category object is split into two plain fields.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private long id; // catalog id
    private String name; // product title
    private String description; // product detail text
    private BigDecimal unitPrice; // current price
    private String imageUrl; // image path (uploaded file or assets fallback)
    private int unitsInStock; // quantity available to buy
    private Date dateCreated; // when it was listed
    private Date lastUpdated; // when it was last edited
    private Long categoryId; // category id extracted from the nested entity
    private String categoryName; // category label extracted from the nested entity
}
