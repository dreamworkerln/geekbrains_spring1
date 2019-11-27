package jsonrpc.protocol.dto.base;

public class HandlerName {

    private static final String getById = "getById";
    private static final String getByListId = "getByListId";
    private static final String getAll = "getAll";
    private static final String put = "put";
    private static final String remove = "remove";
    private static final String delete = "delete";


    public static class Order {
        public static final String path = "shop.entities.order";

        public static final String getById = HandlerName.getById;
        public static final String put = HandlerName.put;
        public static final String delete = HandlerName.delete;
    }


    public static class Product {
        public static final String path = "shop.entities.product";

        public static final String getById = HandlerName.getById;
        public static final String getByListId = HandlerName.getByListId;
        public static final String getAll = HandlerName.getAll;
        public static final String put = HandlerName.put;
        public static final String delete = HandlerName.delete;
    }



    public static class Storage {
        public static final String path = "shop.entities.storage";

        public static final String getById = HandlerName.getById;
        public static final String getByListId = HandlerName.getByListId;
        public static final String getAll = HandlerName.getAll;
        public static final String put = HandlerName.put;
        public static final String remove = HandlerName.remove; // decrease Product count
        public static final String delete = HandlerName.delete; // remove self ProductItem
    }

}
