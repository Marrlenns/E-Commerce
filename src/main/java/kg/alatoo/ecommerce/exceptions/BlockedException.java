package kg.alatoo.ecommerce.exceptions;

public class BlockedException extends RuntimeException {
    public BlockedException() {
        super();
    }

    public BlockedException(String message) {
        super(message);
    }

}