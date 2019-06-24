package misaka.sms;

import java.util.List;

/**
 * Short messaging service provider.
 * @author Kahle
 */
public interface SmsProvider {

    /**
     * Send SMS message.
     * @param smsMessage SMS message object
     * @return The result of the send operation
     */
    SmsSendResult send(SmsMessage smsMessage);

    /**
     * Send batch SMS message.
     * @param smsMessageList SMS message object list
     * @return The result of the send operation
     */
    List<SmsSendResult> sendBatch(List<SmsMessage> smsMessageList);

    /**
     * Query the one SMS sending result from the service provider.
     * @param smsQuery SMS query conditions
     * @return The result of sending SMS
     */
    SmsQueryResult findOne(SmsQuery smsQuery);

    /**
     * Query the SMS sending result from the service provider.
     * @param smsQuery SMS query conditions
     * @return The result of sending SMS
     */
    List<SmsQueryResult> findSelective(SmsQuery smsQuery);

}
