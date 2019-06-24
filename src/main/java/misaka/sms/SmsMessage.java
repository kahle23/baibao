package misaka.sms;

import java.io.Serializable;
import java.util.Map;

/**
 * Short messaging service message.
 * @author Kahle
 */
public class SmsMessage implements Serializable {
    /**
     * Country calling code.
     * @see <a href="https://en.wikipedia.org/wiki/List_of_country_calling_codes">Country calling code</a>
     */
    private String countryCallingCode;
    /**
     * Cell-phone number.
     */
    private String phoneNumber;
    /**
     * Sender name (SMS signature).
     */
    private String senderName;
    /**
     * SMS template code.
     */
    private String templateCode;
    /**
     * SMS template content (if the support).
     */
    private String templateContent;
    /**
     * SMS template parameters.
     */
    private Map<String, Object> parameters;

    public String getCountryCallingCode() {

        return countryCallingCode;
    }

    public void setCountryCallingCode(String countryCallingCode) {

        this.countryCallingCode = countryCallingCode;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public String getSenderName() {

        return senderName;
    }

    public void setSenderName(String senderName) {

        this.senderName = senderName;
    }

    public String getTemplateCode() {

        return templateCode;
    }

    public void setTemplateCode(String templateCode) {

        this.templateCode = templateCode;
    }

    public String getTemplateContent() {

        return templateContent;
    }

    public void setTemplateContent(String templateContent) {

        this.templateContent = templateContent;
    }

    public Map<String, Object> getParameters() {

        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {

        this.parameters = parameters;
    }

}
