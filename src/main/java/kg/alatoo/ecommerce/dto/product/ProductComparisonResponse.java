package kg.alatoo.ecommerce.dto.product;

import kg.alatoo.ecommerce.enums.Color;
import kg.alatoo.ecommerce.enums.Size;
import kg.alatoo.ecommerce.enums.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductComparisonResponse {
    private Long id1, id2;
    private String title1, title2;
    private String rating1, rating2;
    private Integer revSize1, revSize2;
    private Integer price1, price2;
    private List<Color> colors1, colors2;
    private List<Tag> tags1, tags2;
    private List<Size> sizes1, sizes2;
    private String category1, category2;
    private String sku1, sku2;
}
