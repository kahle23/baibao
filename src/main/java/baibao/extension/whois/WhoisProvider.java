package baibao.extension.whois;

public interface WhoisProvider {

    WhoisObject findByDomainName(String domainName);

}
