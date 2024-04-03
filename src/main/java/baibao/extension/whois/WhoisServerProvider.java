package baibao.extension.whois;

public interface WhoisServerProvider {

    WhoisServer findByDomainName(String domainName);

}
