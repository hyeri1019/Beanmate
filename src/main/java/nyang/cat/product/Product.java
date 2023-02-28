package nyang.cat.product;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private Long id;

    private String name;
    private Long price;
    private Long stock;
    private String content;

    public Product(String name, Long price, Long stock, String content) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.content = content;
    }

    public Product() {

    }
}
