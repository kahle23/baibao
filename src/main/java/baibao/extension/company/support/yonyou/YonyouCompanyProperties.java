package baibao.extension.company.support.yonyou;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "misaka.company.yonyou")
public class YonyouCompanyProperties {
    private Boolean enabled;
    private String  baseInfoApiCode;
    private String  searchApiCode;
    private Integer timeout;

    public Boolean getEnabled() {

        return enabled;
    }

    public void setEnabled(Boolean enabled) {

        this.enabled = enabled;
    }

    public String getBaseInfoApiCode() {

        return baseInfoApiCode;
    }

    public void setBaseInfoApiCode(String baseInfoApiCode) {

        this.baseInfoApiCode = baseInfoApiCode;
    }

    public String getSearchApiCode() {

        return searchApiCode;
    }

    public void setSearchApiCode(String searchApiCode) {

        this.searchApiCode = searchApiCode;
    }

    public Integer getTimeout() {

        return timeout;
    }

    public void setTimeout(Integer timeout) {

        this.timeout = timeout;
    }

}
