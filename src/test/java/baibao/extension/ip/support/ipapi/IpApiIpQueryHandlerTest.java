package baibao.extension.ip.support.ipapi;

import artoria.action.ActionUtils;
import artoria.data.json.JsonUtils;
import artoria.data.json.support.FastJsonHandler;
import baibao.extension.ip.IpQuery;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class IpApiIpQueryHandlerTest {
    private static final Logger log = LoggerFactory.getLogger(IpApiIpQueryHandlerTest.class);
    private static final String IP_QUERY_NAME = "ip-query-ipapi";

    @Test
    public void test1() {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        ActionUtils.registerHandler(IP_QUERY_NAME, new IpApiIpActionHandler());

        IpQuery query = new IpQuery("223.98.40.191");
        IpApiIpLocation ipLocation = ActionUtils.execute(query, IP_QUERY_NAME, IpApiIpLocation.class);
        log.info("{}", JSON.toJSONString(ipLocation, true));

        query = new IpQuery("106.57.23.1");
        ipLocation = ActionUtils.execute(query, IP_QUERY_NAME, IpApiIpLocation.class);
        log.info("{}", JSON.toJSONString(ipLocation, true));

        /*query = new IpQuery("120.231.22.221");
        ipLocation = ActionUtils.execute(query, IpApiIpLocation.class);
        log.info("{}", JSON.toJSONString(ipLocation, true));

        query = new IpQuery("220.243.135.165");
        ipLocation = ActionUtils.execute(query, IpApiIpLocation.class);
        log.info("{}", JSON.toJSONString(ipLocation, true));

        query = new IpQuery("117.136.7.206");
        ipLocation = ActionUtils.execute(query, IpApiIpLocation.class);
        log.info("{}", JSON.toJSONString(ipLocation, true));

        query = new IpQuery("122.238.172.155");
        ipLocation = ActionUtils.execute(query, IpApiIpLocation.class);
        log.info("{}", JSON.toJSONString(ipLocation, true));*/
        log.info("{}", ipLocation);
    }

}
