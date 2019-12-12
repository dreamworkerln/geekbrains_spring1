package jsonrpc.server.entities.order;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.Client;
import jsonrpc.server.entities.Manager;
import jsonrpc.server.entities.base.AbstractEntity;
import org.apache.commons.lang3.SerializationUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


// Заказ пользователя
@Entity
@Table(name="orderz")  // Ambiguous with hsql keyword 'order'
public class Order extends AbstractEntity {


    // orphanRemoval - чтоб осиротевшие заказы сами удалились. Хибер крут!
    @NotNull
    @OneToMany(mappedBy= "order", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<OrderItem> itemList = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name="client_id", referencedColumnName="id")
    private Client client;

    @ManyToOne
    @JoinColumn(name="manager_id", referencedColumnName="id")
    private Manager manager;


    //ToDo Need create enum table

    // QUEUED - просто корзина, товар не резервируется, оплата не производится,
    // ползователи бьются друг с другом кто быстрее оплатит и уведет последний товар.
    //
    // ORDERED - товар зарезервирован и оплачен (тут можно сделать еще режим бронь(RESERVED) с ожиданием оплаты,
    // можно и в корзине блокировать с таймаутом). При переходе заказа в это состояния происходит резервирование.
    //
    // CANCELED - заказ отменен, товар возвращен на склад.
    // COMPLETED - заказ выполнен, товар отгружен покупателю.
    //
    // Чего делать, когда пользователь раздербанил заказ, часть вещей  взял, часть оставил?
    // (как по типу, так и по количеству)

    @NotNull
    private OrderDto.Status status;


//    public Long getId() {
//        return id;
//    }

    public void addItem(OrderItem item) {
        itemList.add(item);
        item.setOrder(this);
    }

//    public void addList(Collection<OrderItem> list) {
//        list.forEach(item -> {
//            itemList.add(item);
//            item.setOrder(this);
//        });

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
        itemList.forEach(item -> item.setOrder(this));
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }


    public OrderDto.Status getStatus() {
        return status;
    }

    public void setStatus(OrderDto.Status status) {
        this.status = status;
    }

    public static Order clone(Order order) {

        Order result = null;

        if (order != null) {
            result = SerializationUtils.clone(order);
        }
        return result;
    }


    @Override
    public String toString() {
        return "OrderN{" +
               "id=" + id +
               ", created=" + created +
               ", updated=" + updated +
               ", client=" + client +
               ", manager=" + manager +
               ", itemList=" + itemList +
               '}';
    }

}
