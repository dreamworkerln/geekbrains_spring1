package jsonrpc.protocol.dto.product;

import jsonrpc.protocol.dto.base.AbstractDto;

import java.math.BigDecimal;
import java.time.Instant;


//@Component
//@Scope("prototype")
public class ProductDto extends AbstractDto {

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
