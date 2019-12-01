package jsonrpc.protocol.dto.product;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Component("productItemDto") // need qualifier due to inheritance issue
@Scope("prototype")
public class ProductItemDto extends AbstractDtoPersisted {

    // lazy
    // только ссылка на продукт (by id)
    // нужно описание и поля продукта - иди в productHandler и получай подробную информацию
    // о интересующих тебя продуктах

    //@NotBlank(message = "Name is mandatory")
    @Min(10)
    protected Long productId;

    @Min(10)
    protected Integer count;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    public static void validate(ProductItemDto productDto) {

        if (productDto == null) {
            throw new IllegalArgumentException("productDto == null");
        }
    }

    @Override
    public String toString() {
        return "ProductItemDto{" +
                "id=" + id +
                ",productId=" + productId +
                ", count=" + count +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
