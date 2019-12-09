package jsonrpc.protocol.dto.order;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import jsonrpc.protocol.dto.client.ClientDto;
import jsonrpc.protocol.dto.manager.ManagerDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//@Component
//@Scope("prototype")
public class OrderDto extends AbstractDtoPersisted {

    @NotNull
    private List<OrderItemDto> itemList = new ArrayList<>();

    private ClientDto client;

    private ManagerDto manager;

    @NotNull
    private Status status;

    @NotNull
    public List<OrderItemDto> getItemList() {
        return itemList;
    }


    public void setItemList(List<OrderItemDto> itemList) {
        this.itemList = itemList;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public ManagerDto getManager() {
        return manager;
    }

    public void setManager(ManagerDto manager) {
        this.manager = manager;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    // --------------------------------------------------------------------

    public void addItemDto(OrderItemDto orderItemDto) {

        itemList.add(orderItemDto);
    }





//    public static void validate(OrderDto orderDto) {
//
//        if (orderDto == null) {
//            throw new IllegalArgumentException("orderDto == null");
//        }
//
//        if (orderDto.itemList == null) {
//            throw new IllegalArgumentException("orderDto.itemList is empty");
//        }
//
//        //ToDo implement etc checks ...
//    }


    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                ", client=" + client +
                ", manager=" + manager +
                ", itemList=" + itemList +
                '}';
    }


    public enum Status {
        QUEUED,
        ORDERED,
        CANCELED,
        COMPLETED
    }
}
