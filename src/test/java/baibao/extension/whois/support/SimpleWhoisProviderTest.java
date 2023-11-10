package baibao.extension.whois.support;

import artoria.data.json.JsonUtils;
import artoria.data.json.support.FastJsonHandler;
import baibao.extension.whois.WhoisObject;
import baibao.extension.whois.WhoisProvider;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Boolean.TRUE;

@Ignore
public class SimpleWhoisProviderTest {
    private static Logger log = LoggerFactory.getLogger(SimpleWhoisProviderTest.class);
    private WhoisProvider whoisProvider = new SimpleWhoisProvider();

    @Test
    public void test1() {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        WhoisObject whoisObject = whoisProvider.findByDomainName("aaaa.com");
        log.info("{}", JSON.toJSONString(whoisObject, TRUE));
        log.info("{}", whoisObject.rawData());
    }

}
