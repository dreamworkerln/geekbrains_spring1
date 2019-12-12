package jsonrpc.protocol.dto.order;

import jsonrpc.protocol.dto.base.AbstractDto;
import jsonrpc.protocol.dto.client.ClientDto;
import jsonrpc.protocol.dto.manager.ManagerDto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//@Component
//@Scope("prototype")
public class OrderDto extends AbstractDto {

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

    public void addItem(OrderItemDto orderItemDto) {

        itemList.add(orderItemDto);
    }

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

    // для IRL нужна еще куча состояний

    // ToDO: Добавлять новые элементы только в конец иначе капец логике программы ,
    // а так надо завести отдельную таблицу под Enum

    public enum Status {
        NULL,       // Заказ не существует
        ORDERED,    // Заказ оформлен, товары зарезервированы (для нового заказа верно после успешного завершения транзакции)
        TRANSIT,    // Заказ оформлен, товары зарезервированы, едет к клиенту, редактирвать нельзя
        CANCELED,   // Заказ отменен, товар возвращен на склад
        COMPLETED   // Заказ выполнен, товар вручен покупателю

        //TRANSIT_BACK Заказ отменен, поехал обратно на склад, все еще в пути, ничего с ним делать нельзя
        // отдельный статус IN_TRANSIT ????
    }
}
