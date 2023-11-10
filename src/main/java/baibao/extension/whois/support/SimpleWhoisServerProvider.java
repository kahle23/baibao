package baibao.extension.whois.support;

import artoria.util.StringUtils;
import baibao.extension.whois.WhoisServer;
import baibao.extension.whois.WhoisServerProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.DOT;
import static artoria.common.Constants.ONE;
import static artoria.io.IOUtils.EOF;

public class SimpleWhoisServerProvider implements WhoisServerProvider {
    private static final Map<String, String> SERVER_MAP = new ConcurrentHashMap<String, String>();

    @Override
    public WhoisServer findByDomainName(String domainName) {
        if (domainName == null) { return null; }
        WhoisServer result = new WhoisServer();
        if (StringUtils.isBlank(domainName)) {
            return null;
        }
        int lastIndexOf = domainName.lastIndexOf(DOT);
        if (lastIndexOf == EOF) { return result; }
        String suffix = domainName.substring(lastIndexOf + ONE, domainName.length());
        String server = SERVER_MAP.get(suffix);
        if (StringUtils.isBlank(server)) { return result; }
        return null;
    }

    static {
        SERVER_MAP.put("br.com", "whois.centralnic.com");
        SERVER_MAP.put("cn.com", "whois.centralnic.com");
        SERVER_MAP.put("de.com", "whois.centralnic.com");
        SERVER_MAP.put("eu.com", "whois.centralnic.com");
        SERVER_MAP.put("gb.com", "whois.centralnic.com");
        SERVER_MAP.put("gb.net", "whois.centralnic.com");
        SERVER_MAP.put("hu.com", "whois.centralnic.com");
        SERVER_MAP.put("no.com", "whois.centralnic.com");
        SERVER_MAP.put("qc.com", "whois.centralnic.com");
        SERVER_MAP.put("ru.com", "whois.centralnic.com");
        SERVER_MAP.put("sa.com", "whois.centralnic.com");
        SERVER_MAP.put("se.com", "whois.centralnic.com");
        SERVER_MAP.put("se.net", "whois.centralnic.com");
        SERVER_MAP.put("uk.com", "whois.centralnic.com");
        SERVER_MAP.put("uk.net", "whois.centralnic.com");
        SERVER_MAP.put("us.com", "whois.centralnic.com");
        SERVER_MAP.put("uy.com", "whois.centralnic.com");
        SERVER_MAP.put("za.com", "whois.centralnic.com");
        SERVER_MAP.put("com.au", "whois.ausregistry.net.au");
        SERVER_MAP.put("net.au", "whois.ausregistry.net.au");
        SERVER_MAP.put("org.au", "whois.ausregistry.net.au");
        SERVER_MAP.put("asn.au", "whois.ausregistry.net.au");
        SERVER_MAP.put("id.au ", "whois.ausregistry.net.au");
        SERVER_MAP.put("ac.uk ", "whois.ja.net");
        SERVER_MAP.put("gov.uk", "whois.ja.net");
        SERVER_MAP.put("museum", "whois.nic.museum");
        SERVER_MAP.put("asia", "whois.crsnic.net");
        SERVER_MAP.put("info", "whois.afilias.net");
        SERVER_MAP.put("name", "whois.nic.name");
        SERVER_MAP.put("aero", "whois.aero");
        SERVER_MAP.put("coop", "whois.nic.coop");
        SERVER_MAP.put("com", "whois.crsnic.net");
        SERVER_MAP.put("net", "whois.crsnic.net");
        SERVER_MAP.put("org", "whois.publicinterestregistry.net");
        SERVER_MAP.put("edu", "whois.educause.net");
        SERVER_MAP.put("gov", "whois.nic.gov");
        SERVER_MAP.put("int", "whois.iana.org");
        SERVER_MAP.put("mil", "whois.nic.mil");
        SERVER_MAP.put("biz", "whois.neulevel.biz");
        SERVER_MAP.put("as", "whois.nic.as");
        SERVER_MAP.put("ac", "whois.nic.ac");
        SERVER_MAP.put("al", "whois.ripe.net");
        SERVER_MAP.put("am", "whois.amnic.net");
        SERVER_MAP.put("at", "whois.nic.at");
        SERVER_MAP.put("au", "whois.aunic.net");
        SERVER_MAP.put("az", "whois.ripe.net");
        SERVER_MAP.put("ba", "whois.ripe.net");
        SERVER_MAP.put("be", "whois.dns.be");
        SERVER_MAP.put("bg", "whois.ripe.net");
        SERVER_MAP.put("br", "whois.nic.br");
        SERVER_MAP.put("by", "whois.ripe.net");
        SERVER_MAP.put("ca", "whois.cira.ca");
//        SERVER_MAP.put("cc", "whois.nic.cc");
        SERVER_MAP.put("cc", "ccwhois.verisign-grs.com");
        SERVER_MAP.put("cd", "whois.nic.cd");
        SERVER_MAP.put("ch", "whois.nic.ch");
        SERVER_MAP.put("cl", "whois.nic.cl");
        SERVER_MAP.put("cn", "whois.cnnic.net.cn");
        SERVER_MAP.put("cx", "whois.nic.cx");
        SERVER_MAP.put("cy", "whois.ripe.net");
        SERVER_MAP.put("cz", "whois.ripe.net");
        SERVER_MAP.put("de", "whois.denic.de");
        SERVER_MAP.put("dk", "whois.dk-hostmaster.dk");
        SERVER_MAP.put("dz", "whois.ripe.net");
        SERVER_MAP.put("ee", "whois.eenet.ee");
        SERVER_MAP.put("eg", "whois.ripe.net");
        SERVER_MAP.put("es", "whois.ripe.net");
        SERVER_MAP.put("eu", "whois.eu");
        SERVER_MAP.put("fi", "whois.ripe.net");
        SERVER_MAP.put("fo", "whois.ripe.net");
        SERVER_MAP.put("fr", "whois.nic.fr");
        SERVER_MAP.put("gb", "whois.ripe.net");
        SERVER_MAP.put("ge", "whois.ripe.net");
        SERVER_MAP.put("gr", "whois.ripe.net");
        SERVER_MAP.put("gs", "whois.nic.gs");
        SERVER_MAP.put("hk", "whois.hkirc.hk");
        SERVER_MAP.put("hr", "whois.ripe.net");
        SERVER_MAP.put("hu", "whois.ripe.net");
        SERVER_MAP.put("ie", "whois.domainregistry.ie");
        SERVER_MAP.put("il", "whois.isoc.org.il");
        SERVER_MAP.put("in", "whois.inregistry.net");
        SERVER_MAP.put("ir", "whois.nic.ir");
        SERVER_MAP.put("is", "whois.ripe.net");
        SERVER_MAP.put("it", "whois.nic.it");
        SERVER_MAP.put("jp", "whois.jp");
        SERVER_MAP.put("kh", "whois.nic.net.kh");
        SERVER_MAP.put("kr", "whois.kr");
        SERVER_MAP.put("li", "whois.nic.ch");
        SERVER_MAP.put("lt", "whois.ripe.net");
        SERVER_MAP.put("lu", "whois.dns.lu");
        SERVER_MAP.put("lv", "whois.ripe.net");
        SERVER_MAP.put("ma", "whois.ripe.net");
        SERVER_MAP.put("me", "whois.nic.me");
        SERVER_MAP.put("md", "whois.ripe.net");
        SERVER_MAP.put("mk", "whois.ripe.net");
        SERVER_MAP.put("ms", "whois.nic.ms");
        SERVER_MAP.put("mt", "whois.ripe.net");
        SERVER_MAP.put("mx", "whois.nic.mx");
        SERVER_MAP.put("nl", "whois.domain-registry.nl");
        SERVER_MAP.put("no", "whois.norid.no");
        SERVER_MAP.put("nu", "whois.nic.nu");
        SERVER_MAP.put("nz", "whois.srs.net.nz");
        SERVER_MAP.put("pl", "whois.dns.pl");
        SERVER_MAP.put("pt", "whois.ripe.net");
        SERVER_MAP.put("ro", "whois.ripe.net");
        SERVER_MAP.put("ru", "whois.tcinet.ru");
        SERVER_MAP.put("se", "whois.nic-se.se");
        SERVER_MAP.put("sg", "whois.nic.net.sg");
        SERVER_MAP.put("si", "whois.ripe.net");
        SERVER_MAP.put("sh", "whois.nic.sh");
        SERVER_MAP.put("sk", "whois.ripe.net");
        SERVER_MAP.put("sm", "whois.ripe.net");
        SERVER_MAP.put("su", "whois.ripn.net");
        SERVER_MAP.put("tc", "whois.nic.tc");
        SERVER_MAP.put("tf", "whois.nic.tf");
        SERVER_MAP.put("th", "whois.thnic.net");
        SERVER_MAP.put("tj", "whois.nic.tj");
        SERVER_MAP.put("tn", "whois.ripe.net");
        SERVER_MAP.put("to", "whois.tonic.to");
        SERVER_MAP.put("tr", "whois.ripe.net");
        SERVER_MAP.put("tv", "tvwhois.verisign-grs.com");
        SERVER_MAP.put("tw", "whois.twnic.net");
        SERVER_MAP.put("ua", "whois.ripe.net");
        SERVER_MAP.put("uk", "whois.nic.uk");
        SERVER_MAP.put("us", "whois.nic.us");
        SERVER_MAP.put("va", "whois.ripe.net");
        SERVER_MAP.put("vg", "whois.nic.vg");
        SERVER_MAP.put("ws", "whois.website.ws");
    }

}
