package com.pranay.inventory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
@Entity @Table(name="products")
class Product {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 @NotBlank String name;
 @NotBlank @Column(unique=true) String sku;
 @NotBlank String category;
 @NotNull @DecimalMin("0.01") BigDecimal price;
 @Min(0) int quantity;
 @Min(0) int reorderLevel;
 protected Product(){}
 Product(String n,String s,String c,BigDecimal p,int q,int r){name=n;sku=s;category=c;price=p;quantity=q;reorderLevel=r;}
}
