package jsonrpc.protocol.dto.product;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;


@Component
@Scope("prototype")
public class ProductDto extends AbstractDtoPersisted {

    private String name;
    private String vcode; // Артикул
    private BigDecimal price;
    private Long categoryId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVcode() {return vcode;}

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public BigDecimal getPrice() {return price;}

    public void setPrice(BigDecimal price) {this.price = price;}

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // ----------------------------------------------------------------------


    private Instant testDate;

    public Instant getTestDate() {
        return testDate;
    }

    public void setTestDate(Instant testDate) {
        this.testDate = testDate;
    }



//    public static void validate(ProductDto productDto) {
//
//        if (productDto == null) {
//            throw new IllegalArgumentException("productDto == null");
//        }
//
//        if (StringUtils.isBlank(productDto.vcode)) {
//            throw new IllegalArgumentException("productDto.vcode is empty");
//        }
//
//        //ToDo implement etc checks ...
//    }


    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", vcode='" + vcode + '\'' +
                ", price=" + price +
                ", categoryId=" + categoryId +
                ", testDate=" + testDate +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }

}
