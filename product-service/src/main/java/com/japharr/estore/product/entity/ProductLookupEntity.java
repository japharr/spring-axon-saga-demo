package com.japharr.estore.product.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_lookup")
public class ProductLookupEntity implements Serializable {
    @Id
    private String productId;
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductLookupEntity that = (ProductLookupEntity) o;
        return productId != null && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }
}
