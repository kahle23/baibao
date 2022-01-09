package misaka.whois;

public interface WhoisProvider {

    WhoisObject findByDomainName(String domainName);

}
