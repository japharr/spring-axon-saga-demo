package com.japharr.estore.product.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CreateProductModel {
    @NotBlank(message = "Title is a required field")
    private String title;

    @Min(value = 1, message = "Price cannot be lower than 1")
    private BigDecimal price;

    @Min(value = 1, message = "Quantity cannot be lower than 1")
    @Max(value = 40, message = "Quantity cannot be greater than 40")
    private int quantity;
}
