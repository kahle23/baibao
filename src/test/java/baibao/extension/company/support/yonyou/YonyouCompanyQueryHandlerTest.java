package baibao.extension.company.support.yonyou;

import artoria.action.ActionUtils;
import artoria.data.json.JsonUtils;
import artoria.data.json.support.JacksonHandler;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import baibao.extension.company.Company;
import baibao.extension.company.CompanyQuery;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

@Ignore
public class YonyouCompanyQueryHandlerTest {
    private static Logger log = LoggerFactory.getLogger(YonyouCompanyQueryHandlerTest.class);

    static {
        String baseInfoApiCode = "";
        String searchApiCode = "";
        Integer timeout = 3000;
        ActionUtils.registerHandler(CompanyQuery.class,
                new YonyouCompanyActionHandler(baseInfoApiCode, searchApiCode, timeout));
        JsonUtils.registerHandler("jackson", new JacksonHandler());
        JsonUtils.setDefaultHandlerName("jackson");
    }

    @Test
    public void testInfo() {
        CompanyQuery query = new CompanyQuery("微软（中国）有限公司上海分公司");
        Company company = ActionUtils.execute(query, Company.class);
        log.info(JSON.toJSONString(company, Boolean.TRUE));
    }

    @Test
    public void testSearch() {
        CompanyQuery query = new CompanyQuery("Microsoft");
        List<Company> companyList = ActionUtils.execute(query, List.class);
        log.info(JSON.toJSONString(companyList, Boolean.TRUE));
    }

}
