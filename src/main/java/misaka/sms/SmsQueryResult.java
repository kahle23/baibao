package misaka.sms;

import artoria.data.AbstractExtendedData;

import java.io.Serializable;
import java.util.Date;

/**
 * Short messaging service query result object.
 * @author Kahle
 */
public class SmsQueryResult extends AbstractExtendedData implements Serializable {
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
     * SMS template code.
     */
    private String templateCode;
    /**
     * SMS template content (if the support).
     */
    private String templateContent;
    /**
     * The sending time of SMS.
     */
    private Date sendTime;
    /**
     * The receiving time of SMS.
     */
    private Date receiveTime;
    /**
     * Error code or status code.
     */
    private String code;
    /**
     * Send status or SMS status.
     */
    private String status;
    /**
     * Description of code.
     */
    private String description;

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

    public Date getSendTime() {

        return sendTime;
    }

    public void setSendTime(Date sendTime) {

        this.sendTime = sendTime;
    }

    public Date getReceiveTime() {

        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {

        this.receiveTime = receiveTime;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

}
