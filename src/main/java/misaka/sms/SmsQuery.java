package misaka.sms;

import java.io.Serializable;
import java.util.Date;

/**
 * Short messaging service query object.
 * @author Kahle
 */
public class SmsQuery implements Serializable {
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
     * The sending time of SMS.
     */
    private Date sendTime;
    /**
     * Begin send time to query.
     */
    private Date beginSendTime;
    /**
     * End send time to query.
     */
    private Date endSendTime;
    /**
     * Business id.
     */
    private String businessId;
    /**
     * Number of pages to query.
     */
    private Integer pageNum;
    /**
     * A single page number.
     * The number of queries per query if paging is not supported.
     */
    private Integer pageSize;

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

    public Date getSendTime() {

        return sendTime;
    }

    public void setSendTime(Date sendTime) {

        this.sendTime = sendTime;
    }

    public Date getBeginSendTime() {

        return beginSendTime;
    }

    public void setBeginSendTime(Date beginSendTime) {

        this.beginSendTime = beginSendTime;
    }

    public Date getEndSendTime() {

        return endSendTime;
    }

    public void setEndSendTime(Date endSendTime) {

        this.endSendTime = endSendTime;
    }

    public String getBusinessId() {

        return businessId;
    }

    public void setBusinessId(String businessId) {

        this.businessId = businessId;
    }

    public Integer getPageNum() {

        return pageNum;
    }

    public void setPageNum(Integer pageNum) {

        this.pageNum = pageNum;
    }

    public Integer getPageSize() {

        return pageSize;
    }

    public void setPageSize(Integer pageSize) {

        this.pageSize = pageSize;
    }

}
