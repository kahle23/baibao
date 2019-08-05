package misaka.storage.object;

import artoria.exception.UncheckedException;

/**
 * Object storage exception.
 * @author Kahle
 */
public class ObjectStorageException extends UncheckedException {

    public ObjectStorageException() {

        super();
    }

    public ObjectStorageException(String message) {

        super(message);
    }

    public ObjectStorageException(Throwable cause) {

        super(cause);
    }

    public ObjectStorageException(String message, Throwable cause) {

        super(message, cause);
    }

}
