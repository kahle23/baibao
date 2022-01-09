package misaka.whois;

public interface WhoisServerProvider {

    WhoisServer findByDomainName(String domainName);

}
