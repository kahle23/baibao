package misaka.whois;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

public class WhoisUtils {
    private static Logger log = LoggerFactory.getLogger(WhoisUtils.class);
    private static WhoisServerProvider whoisServerProvider;
    private static WhoisProvider whoisProvider;

    public static WhoisServerProvider getWhoisServerProvider() {

        return whoisServerProvider;
    }

    public static void setWhoisServerProvider(WhoisServerProvider whoisServerProvider) {
        Assert.notNull(whoisServerProvider, "Parameter \"whoisServerProvider\" must not null. ");
        log.info("Set whois server provider: {}", whoisServerProvider.getClass().getName());
        WhoisUtils.whoisServerProvider = whoisServerProvider;
    }

    public static WhoisProvider getWhoisProvider() {

        return whoisProvider;
    }

    public static void setWhoisProvider(WhoisProvider whoisProvider) {
        Assert.notNull(whoisProvider, "Parameter \"whoisProvider\" must not null. ");
        log.info("Set whois provider: {}", whoisProvider.getClass().getName());
        WhoisUtils.whoisProvider = whoisProvider;
    }

    public static WhoisServer findServerByDomainName(String domainName) {

        return getWhoisServerProvider().findByDomainName(domainName);
    }

    public static WhoisObject findByDomainName(String domainName) {

        return getWhoisProvider().findByDomainName(domainName);
    }

}
