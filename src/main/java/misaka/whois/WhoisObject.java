package misaka.whois;

import artoria.data.AbstractExtraData;
import artoria.data.RawData;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WhoisObject extends AbstractExtraData implements RawData, Serializable {
    private String domainName;
    private List<String> domainStatuses;
    private List<String> nameServers;
    private String registryDomainId;
    private Date registryRegistrationTime;
    private Date registryExpirationTime;
    private Date registryUpdateTime;
    private String reseller;
    private String registrar;
    private String registrarUrl;
    private String registrarWhoisServer;
    private String registrarAbuseContactEmail;
    private String registrarAbuseContactPhone;
    private String registrarIanaId;
    private Date registrarRegistrationExpirationDate;
    private String registrant;
    private String registrantId;
    private String registrantOrganization;
    private String registrantCountry;
    private String registrantRegion;
    private String registrantCity;
    private String registrantContactEmail;
    private String registrantContactPhone;
    private String dnsSec;
    private Date whoisLastUpdateTime;
    private String remark;
    private Object rawData;

    public String getDomainName() {

        return domainName;
    }

    public void setDomainName(String domainName) {

        this.domainName = domainName;
    }

    public List<String> getDomainStatuses() {

        return domainStatuses;
    }

    public void setDomainStatuses(List<String> domainStatuses) {

        this.domainStatuses = domainStatuses;
    }

    public List<String> getNameServers() {

        return nameServers;
    }

    public void setNameServers(List<String> nameServers) {

        this.nameServers = nameServers;
    }

    public String getRegistryDomainId() {

        return registryDomainId;
    }

    public void setRegistryDomainId(String registryDomainId) {

        this.registryDomainId = registryDomainId;
    }

    public Date getRegistryRegistrationTime() {

        return registryRegistrationTime;
    }

    public void setRegistryRegistrationTime(Date registryRegistrationTime) {

        this.registryRegistrationTime = registryRegistrationTime;
    }

    public Date getRegistryExpirationTime() {

        return registryExpirationTime;
    }

    public void setRegistryExpirationTime(Date registryExpirationTime) {

        this.registryExpirationTime = registryExpirationTime;
    }

    public Date getRegistryUpdateTime() {

        return registryUpdateTime;
    }

    public void setRegistryUpdateTime(Date registryUpdateTime) {

        this.registryUpdateTime = registryUpdateTime;
    }

    public String getReseller() {

        return reseller;
    }

    public void setReseller(String reseller) {

        this.reseller = reseller;
    }

    public String getRegistrar() {

        return registrar;
    }

    public void setRegistrar(String registrar) {

        this.registrar = registrar;
    }

    public String getRegistrarUrl() {

        return registrarUrl;
    }

    public void setRegistrarUrl(String registrarUrl) {

        this.registrarUrl = registrarUrl;
    }

    public String getRegistrarWhoisServer() {

        return registrarWhoisServer;
    }

    public void setRegistrarWhoisServer(String registrarWhoisServer) {

        this.registrarWhoisServer = registrarWhoisServer;
    }

    public String getRegistrarAbuseContactEmail() {

        return registrarAbuseContactEmail;
    }

    public void setRegistrarAbuseContactEmail(String registrarAbuseContactEmail) {

        this.registrarAbuseContactEmail = registrarAbuseContactEmail;
    }

    public String getRegistrarAbuseContactPhone() {

        return registrarAbuseContactPhone;
    }

    public void setRegistrarAbuseContactPhone(String registrarAbuseContactPhone) {

        this.registrarAbuseContactPhone = registrarAbuseContactPhone;
    }

    public String getRegistrarIanaId() {

        return registrarIanaId;
    }

    public void setRegistrarIanaId(String registrarIanaId) {

        this.registrarIanaId = registrarIanaId;
    }

    public Date getRegistrarRegistrationExpirationDate() {

        return registrarRegistrationExpirationDate;
    }

    public void setRegistrarRegistrationExpirationDate(Date registrarRegistrationExpirationDate) {

        this.registrarRegistrationExpirationDate = registrarRegistrationExpirationDate;
    }

    public String getRegistrant() {

        return registrant;
    }

    public void setRegistrant(String registrant) {

        this.registrant = registrant;
    }

    public String getRegistrantId() {

        return registrantId;
    }

    public void setRegistrantId(String registrantId) {

        this.registrantId = registrantId;
    }

    public String getRegistrantOrganization() {

        return registrantOrganization;
    }

    public void setRegistrantOrganization(String registrantOrganization) {

        this.registrantOrganization = registrantOrganization;
    }

    public String getRegistrantCountry() {

        return registrantCountry;
    }

    public void setRegistrantCountry(String registrantCountry) {

        this.registrantCountry = registrantCountry;
    }

    public String getRegistrantRegion() {

        return registrantRegion;
    }

    public void setRegistrantRegion(String registrantRegion) {

        this.registrantRegion = registrantRegion;
    }

    public String getRegistrantCity() {

        return registrantCity;
    }

    public void setRegistrantCity(String registrantCity) {

        this.registrantCity = registrantCity;
    }

    public String getRegistrantContactEmail() {

        return registrantContactEmail;
    }

    public void setRegistrantContactEmail(String registrantContactEmail) {

        this.registrantContactEmail = registrantContactEmail;
    }

    public String getRegistrantContactPhone() {

        return registrantContactPhone;
    }

    public void setRegistrantContactPhone(String registrantContactPhone) {

        this.registrantContactPhone = registrantContactPhone;
    }

    public String getDnsSec() {

        return dnsSec;
    }

    public void setDnsSec(String dnsSec) {

        this.dnsSec = dnsSec;
    }

    public Date getWhoisLastUpdateTime() {

        return whoisLastUpdateTime;
    }

    public void setWhoisLastUpdateTime(Date whoisLastUpdateTime) {

        this.whoisLastUpdateTime = whoisLastUpdateTime;
    }

    public String getRemark() {

        return remark;
    }

    public void setRemark(String remark) {

        this.remark = remark;
    }

    @Override
    public Object rawData() {

        return rawData;
    }

    @Override
    public void rawData(Object rawData) {

        this.rawData = rawData;
    }

}
