package jsonrpc.protocol.dto.order;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import jsonrpc.protocol.dto.client.ClientDto;
import jsonrpc.protocol.dto.manager.ManagerDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class OrderDto extends AbstractDtoPersisted {

    private List<OrderItemDto> itemList = new ArrayList<>();

    private ClientDto client;

    private ManagerDto manager;


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

    // --------------------------------------------------------------------

    public void addOrderItemDto(OrderItemDto orderItemDto) {

        itemList.add(orderItemDto);
    }





    public static void validate(OrderDto orderDto) {

        if (orderDto == null) {
            throw new IllegalArgumentException("orderDto == null");
        }

        if (orderDto.itemList == null) {
            throw new IllegalArgumentException("orderDto.itemList is empty");
        }

        //ToDo implement etc checks ...
    }


    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", client=" + client +
                ", manager=" + manager +
                ", created=" + created +
                ", updated=" + updated +
                ", itemList=" + itemList +
                '}';
    }
}
