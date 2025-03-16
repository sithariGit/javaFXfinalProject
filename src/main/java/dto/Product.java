package dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import util.Category;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category; // Enum for product categories

     @Column(name = "buying_unit_price", nullable = false)
    private BigDecimal buyingUnitPrice;

    @Column(name = "selling_unit_price", nullable = false)
    private BigDecimal sellingUnitPrice;

    @Column(name = "buying_quantity", nullable = false)
    private Integer buyingQuantity;

    @Column(name = "in_stock", nullable = false)
    private Integer inStock;

    @Column(name = "date", nullable = false)
    private java.util.Date date;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = true)
    private Supplier supplier;

}
