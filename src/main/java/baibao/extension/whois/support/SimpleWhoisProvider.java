package baibao.extension.whois.support;

import artoria.exception.ExceptionUtils;
import artoria.io.IOUtils;
import artoria.io.stream.StringBuilderWriter;
import artoria.time.DateUtils;
import artoria.util.CloseUtils;
import artoria.util.CollectionUtils;
import artoria.util.StringUtils;
import baibao.extension.whois.WhoisObject;
import baibao.extension.whois.WhoisProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.*;
import static artoria.io.IOUtils.EOF;

public class SimpleWhoisProvider implements WhoisProvider {
    private static final Map<String, String> SERVER_MAP = new ConcurrentHashMap<String, String>();
    private static Logger log = LoggerFactory.getLogger(SimpleWhoisProvider.class);

    @Override
    public WhoisObject findByDomainName(String domainName) {
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;
        Socket socket = null;
        try {
            if (domainName == null) { return null; }
            WhoisObject result = new WhoisObject();
            result.setDomainName(domainName);
            if (StringUtils.isBlank(domainName)) {
                return result;
            }
            domainName = StringUtils.trimAllWhitespace(domainName);
            int indexOf = domainName.indexOf("://");
            if (indexOf != EOF) {
                domainName = domainName.substring(indexOf + THREE);
            }
            if (domainName.startsWith("www.")) {
                domainName = StringUtils.replace(domainName, "www.", EMPTY_STRING);
            }
            result.setDomainName(domainName);
            int lastIndexOf = domainName.lastIndexOf(DOT);
            if (lastIndexOf == EOF) { return result; }
            String suffix = domainName.substring(lastIndexOf + ONE, domainName.length());
            String server = SERVER_MAP.get(suffix);
            if (StringUtils.isBlank(server)) { return result; }


            domainName += NEWLINE;
            socket = new Socket(server,43);
            OutputStream out = socket.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, UTF_8));
            bufferedWriter.write(domainName);
            bufferedWriter.flush();
            socket.shutdownOutput();

            InputStream in = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(in, UTF_8));
            StringBuilderWriter stringBuilderWriter = new StringBuilderWriter();
            IOUtils.copyLarge(bufferedReader, stringBuilderWriter);
            socket.shutdownInput();
            String resultString = stringBuilderWriter.toString();
            if (StringUtils.isBlank(resultString)) { return null; }
            result.rawData(resultString);
            parse(resultString, result);

            if (StringUtils.isBlank(result.getRegistryDomainId())
                    && CollectionUtils.isEmpty(result.getDomainStatuses())) {
                log.info("The registry domain id and domain statuses of the domain \"{}\" are empty, " +
                        "so will return null. ", result.getDomainName());
                return null;
            }
            return result;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(bufferedWriter);
            CloseUtils.closeQuietly(bufferedReader);
            CloseUtils.closeQuietly(socket);
        }
    }

    private void parse(String content, WhoisObject whoisObject) {
        String datetimePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        String[] split = content.split("\r\n");
        if (split.length == ONE) {
            split = content.split("\n");
        }
        List<String> domainStatuses = new ArrayList<String>();
        List<String> nameServers = new ArrayList<String>();
        for (String line : split) {
            if (StringUtils.isBlank(line)) { continue; }
            line = line.trim();
            if (StringUtils.isBlank(line)) { continue; }
            if (line.startsWith("Registry Domain ID:")) {
                String lineValue = cutout(line, "Registry Domain ID:");
                whoisObject.setRegistryDomainId(lineValue);
            }
            if (line.startsWith("Registrar WHOIS Server:")) {
                String lineValue = cutout(line, "Registrar WHOIS Server:");
                whoisObject.setRegistrarWhoisServer(lineValue);
            }
            if (line.startsWith("Registrar URL:")) {
                String lineValue = cutout(line, "Registrar URL:");
                whoisObject.setRegistrarUrl(lineValue);
            }
            if (line.startsWith("Updated Date:")) {
                String lineValue = cutout(line, "Updated Date:");
                if (StringUtils.isNotBlank(lineValue)) {
                    Date date = DateUtils.parse(lineValue, datetimePattern);
                    whoisObject.setRegistryUpdateTime(date);
                }
            }
            if (line.startsWith("Creation Date:")) {
                String lineValue = cutout(line, "Creation Date:");
                if (StringUtils.isNotBlank(lineValue)) {
                    Date date = DateUtils.parse(lineValue, datetimePattern);
                    whoisObject.setRegistryRegistrationTime(date);
                }
            }
            if (line.startsWith("Registry Expiry Date:")) {
                String lineValue = cutout(line, "Registry Expiry Date:");
                if (StringUtils.isNotBlank(lineValue)) {
                    Date date = DateUtils.parse(lineValue, datetimePattern);
                    whoisObject.setRegistryExpirationTime(date);
                }
            }
            if (line.startsWith("Registrar Registration Expiration Date:")) {
                String lineValue = cutout(line, "Registrar Registration Expiration Date:");
                if (StringUtils.isNotBlank(lineValue)) {
                    Date date = DateUtils.parse(lineValue, datetimePattern);
                    whoisObject.setRegistrarRegistrationExpirationDate(date);
                }
            }
            if (line.startsWith(">>> Last update of whois database:")) {
                String lineValue = cutout(line, ">>> Last update of whois database:");
                if (StringUtils.isNotBlank(lineValue)) {
                    Date date = DateUtils.parse(lineValue, datetimePattern);
                    whoisObject.setWhoisLastUpdateTime(date);
                }
            }
            if (line.startsWith("Registrar:")) {
                String lineValue = cutout(line, "Registrar:");
                whoisObject.setRegistrar(lineValue);
            }
            if (line.startsWith("Registrar IANA ID:")) {
                String lineValue = cutout(line, "Registrar IANA ID:");
                whoisObject.setRegistrarIanaId(lineValue);
            }
            if (line.startsWith("Registrar Abuse Contact Email:")) {
                String lineValue = cutout(line, "Registrar Abuse Contact Email:");
                whoisObject.setRegistrarAbuseContactEmail(lineValue);
            }
            if (line.startsWith("Registrar Abuse Contact Phone:")) {
                String lineValue = cutout(line, "Registrar Abuse Contact Phone:");
                whoisObject.setRegistrarAbuseContactPhone(lineValue);
            }
            if (line.startsWith("Reseller:")) {
                String lineValue = cutout(line, "Reseller:");
                whoisObject.setReseller(lineValue);
            }
            if (line.startsWith("Domain Status:")) {
                String lineValue = cutout(line, "Domain Status:");
                domainStatuses.add(lineValue);
            }
            if (line.startsWith("Registrant Organization:")) {
                String lineValue = cutout(line, "Registrant Organization:");
                whoisObject.setRegistrantOrganization(lineValue);
            }
            if (line.startsWith("Registrant State/Province:")) {
                String lineValue = cutout(line, "Registrant State/Province:");
                whoisObject.setRegistrantRegion(lineValue);
            }
            if (line.startsWith("Registrant Country:")) {
                String lineValue = cutout(line, "Registrant Country:");
                whoisObject.setRegistrantCountry(lineValue);
            }
            if (line.startsWith("Name Server:")) {
                String lineValue = cutout(line, "Name Server:");
                nameServers.add(lineValue.toLowerCase());
            }
            if (line.startsWith("DNSSEC:")) {
                String lineValue = cutout(line, "DNSSEC:");
                whoisObject.setDnsSec(lineValue);
            }
            if (line.startsWith("URL of the ICANN")) {
                whoisObject.setRemark(line);
            }
            // // // //
            if (line.startsWith("Registrant ID:")) {
                String lineValue = cutout(line, "Registrant ID:");
                whoisObject.setRegistrantId(lineValue);
            }
            if (line.startsWith("Registrant:")) {
                String lineValue = cutout(line, "Registrant:");
                whoisObject.setRegistrant(lineValue);
            }
            if (line.startsWith("Registrant Contact Email:")) {
                String lineValue = cutout(line, "Registrant Contact Email:");
                whoisObject.setRegistrantContactEmail(lineValue);
            }
            if (line.startsWith("Sponsoring Registrar:")) {
                String lineValue = cutout(line, "Sponsoring Registrar:");
                whoisObject.setRegistrar(lineValue);
            }
            if (line.startsWith("Registration Time:")) {
                String lineValue = cutout(line, "Registration Time:");
                if (StringUtils.isNotBlank(lineValue)) {
                    Date date = DateUtils.parse(lineValue, "yyyy-MM-dd HH:mm:ss");
                    whoisObject.setRegistryRegistrationTime(date);
                }
            }
            if (line.startsWith("Expiration Time:")) {
                String lineValue = cutout(line, "Expiration Time:");
                if (StringUtils.isNotBlank(lineValue)) {
                    Date date = DateUtils.parse(lineValue, "yyyy-MM-dd HH:mm:ss");
                    whoisObject.setRegistryExpirationTime(date);
                }
            }
        }
        whoisObject.setDomainStatuses(domainStatuses);
        whoisObject.setNameServers(nameServers);
    }

    private String cutout(String data, String pattern) {
        String val = StringUtils.remove(data, pattern);
        return val.trim();
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
