package misaka.sms;

import artoria.exception.UncheckedException;

/**
 * Short messaging service exception.
 * @author Kahle
 */
public class SmsException extends UncheckedException {

    public SmsException() {

        super();
    }

    public SmsException(String message) {

        super(message);
    }

    public SmsException(Throwable cause) {

        super(cause);
    }

    public SmsException(String message, Throwable cause) {

        super(message, cause);
    }

}
