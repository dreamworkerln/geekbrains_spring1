package jsonrpc.server.entities.base.param;

public abstract class AbstractParam {

    protected abstract void validate();

    public static void validate(AbstractParam request) {

        // check for non-null
        if (request == null) {
            throw new IllegalArgumentException("Error parsing request: params == null");
        }

        // check custom (шаблонный метод)
        request.validate();
    }


}
