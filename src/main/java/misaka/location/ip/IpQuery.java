package misaka.location.ip;

@Deprecated // TODO: 2023/3/27 Deletable
public class IpQuery {
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
