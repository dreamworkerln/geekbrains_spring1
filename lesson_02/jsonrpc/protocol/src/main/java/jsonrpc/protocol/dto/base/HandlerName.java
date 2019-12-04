package jsonrpc.protocol.dto.base;

public class HandlerName {

    // classic jpa CRUD
    private static final String findById = "findById";
    private static final String findAllById = "findAllById";
    private static final String findAll = "findAll";
    private static final String save = "save";
    private static final String delete = "delete";

    // Storage increase/decrease product count
    private static final String put = "put";
    private static final String remove = "remove";




    public static class Order {
        public static final String path = "shop.entities.order";

        public static final String findById = HandlerName.findById;
        public static final String save = HandlerName.save;
        public static final String delete = HandlerName.delete;
    }


    public static class Product {
        public static final String path = "shop.entities.product";

        public static final String findById = HandlerName.findById;
        public static final String findAllById = HandlerName.findAllById;
        public static final String findAll = HandlerName.findAll;
        public static final String save = HandlerName.save;
        public static final String delete = HandlerName.delete;
    }



    public static class Storage {
        public static final String path = "shop.entities.storage";

        public static final String findByProductId = "findByProductId";
        public static final String findAllByProductId = "findAllByProductId";
        public static final String findAll = HandlerName.findAll;
        public static final String save = HandlerName.save;     // сохранить значение ProductItem
        public static final String put =    HandlerName.put;    // increase product count
        public static final String remove = HandlerName.remove; // decrease product count
        public static final String delete = HandlerName.delete; // remove self ProductItem
    }

}
