package misaka.pay;

import artoria.data.AbstractExtraData;

import java.io.Serializable;

public class PayNotifyModel extends AbstractExtraData implements Serializable {
    private String appId;
    private String appType;
    private String payWay;
    private Object notify;

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

    public Object getNotify() {
        return notify;
    }

    public void setNotify(Object notify) {
        this.notify = notify;
    }
}
