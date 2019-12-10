package jsonrpc.protocol.dto.product;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

//@Component("productItemDto") // need qualifier due to inheritance issue
//@Scope("prototype")
public class ProductItemDto extends AbstractDto {

    // lazy
    // только ссылка на продукт (by id)
    // нужно описание и поля продукта - иди в productHandler и получай подробную информацию
    // о интересующих тебя продуктах

    //@NotBlank(message = "Name is mandatory")
    @NotNull
    protected Long productId;

    @NotNull
    @Min(0)
    protected Integer count;


    public ProductItemDto() {
    }

    public ProductItemDto(@NotNull Long productId, @NotNull @Min(0) Integer count) {
        this.productId = productId;
        this.count = count;
    }

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


//    public static void validate(ProductItemDto productDto) {
//
//        if (productDto == null) {
//            throw new IllegalArgumentException("productDto == null");
//        }
//    }

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
