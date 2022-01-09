package misaka.pay;

import artoria.common.GenericResult;
import artoria.data.AbstractExtraData;
import artoria.data.RawData;

import java.io.Serializable;

public class OrderPayResult extends AbstractExtraData implements RawData, GenericResult, Serializable {
    private Object rawData;
    private String code;
    private String message;
    private String appId;
    private String appType;
    private String payWay;
    private Object payResult;

    @Override
    public Object rawData() {

        return rawData;
    }

    @Override
    public void rawData(Object rawData) {

        this.rawData = rawData;
    }

    @Override
    public String getCode() {

        return code;
    }

    @Override
    public void setCode(String code) {

        this.code = code;
    }

    @Override
    public String getMessage() {

        return message;
    }

    @Override
    public void setMessage(String message) {

        this.message = message;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public Object getPayResult() {
        return payResult;
    }

    public void setPayResult(Object payResult) {
        this.payResult = payResult;
    }

}
