package baibao.extension.ip;

import java.io.Serializable;

public class IpQuery implements Serializable {
    private String ipAddress;
    private String language;

    public IpQuery(String ipAddress) {

        this.ipAddress = ipAddress;
    }

    public IpQuery() {

    }

    public String getIpAddress() {

        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {

        this.ipAddress = ipAddress;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(String language) {

        this.language = language;
    }

}
