package baibao.extension.company.support.yonyou;

import artoria.action.handler.AbstractClassicActionHandler;
import artoria.data.bean.BeanUtils;
import artoria.data.json.JsonUtils;
import artoria.exception.ExceptionUtils;
import artoria.net.HttpMethod;
import artoria.net.HttpRequest;
import artoria.net.HttpResponse;
import artoria.net.HttpUtils;
import artoria.util.*;
import baibao.extension.company.Company;
import baibao.extension.company.CompanyQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.UTF_8;
import static artoria.util.ObjectUtils.cast;
import static java.lang.String.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

/**
 * Yonyou cloud (https://yonyoucloud.com).
 */
public class YonyouCompanyActionHandler extends AbstractClassicActionHandler {
    private static final String BASE_INFO_URL_FORMAT = "https://api.yonyoucloud.com/apis/dst/baseinfo/baseinfoV3?name=%s";
    private static final String SEARCH_URL_FORMAT = "https://api.yonyoucloud.com/apis/dst/Search/search?word=%s";
    private static final String AUTHORIZATION_HEADER = "authoration";
    private static final String API_CODE_HEADER = "apicode";
    private static final String FAILURE = "-1";
    private static Logger log = LoggerFactory.getLogger(YonyouCompanyActionHandler.class);
    private String baseInfoApiCode;
    private String searchApiCode;
    private Integer timeout;

    public YonyouCompanyActionHandler(String baseInfoApiCode, String searchApiCode, Integer timeout) {
        Assert.notBlank(baseInfoApiCode, "Parameter \"baseInfoApiCode\" must not blank. ");
        Assert.notBlank(searchApiCode, "Parameter \"searchApiCode\" must not blank. ");
        this.baseInfoApiCode = baseInfoApiCode;
        this.searchApiCode = searchApiCode;
        this.timeout = timeout != null ? timeout : 30000;
    }

    protected Map<String, Object> parseResult(String dataStr) throws IOException {
        if (StringUtils.isBlank(dataStr)) {
            throw new IOException("The response is returned with no content. ");
        }
        Type type = TypeUtils.parameterizedOf(Map.class, String.class, Object.class);
        Map<String, Object> dataMap = JsonUtils.parseObject(dataStr, type); Object value;
        String errorCode = (value = dataMap.get("error_code")) != null ? valueOf(value) : null;
        String message = (value = dataMap.get("msg")) != null ? valueOf(value) : null;
        String status = (value = dataMap.get("status")) != null ? valueOf(value) : null;
        if (FAILURE.equals(status) || FAILURE.equals(errorCode)) {
            throw new IOException(message);
        }
        Map<String, Object> resultMap = BeanUtils.beanToMap(dataMap.get("result"));
        if (resultMap == null) { resultMap = emptyMap(); }
        return resultMap;
    }

    protected Company build(Map<String, Object> map) {
        Company result = new Company();
//        result.rawData(map);
//        result.fromMap(map);
        Object value;
        result.setId((value = map.get("id")) != null ? valueOf(value) : null);
        result.setName((value = map.get("name")) != null ? valueOf(value) : null);
        result.setCode((value = map.get("creditCode")) != null ? valueOf(value) : null);
        result.setLogo((value = map.get("logo")) != null ? valueOf(value) : null);
        result.setTelephone((value = map.get("phoneNumber")) != null ? valueOf(value) : null);
        result.setWebsite((value = map.get("websiteList")) != null ? valueOf(value) : null);
        result.setEmail((value = map.get("email")) != null ? valueOf(value) : null);
//        result.put("address", (value = map.get("regLocation")) != null ? valueOf(value) : null);
//        result.put("industry", (value = map.get("industry")) != null ? valueOf(value) : null);
        return result;
    }

    protected <T> T logOutput(T output) {
        if (output instanceof Collection) {
            log.info("Output size: {}", ((Collection) output).size());
        }
        else {
            log.info("Output exist: {}", !ObjectUtils.isEmpty(output));
        }
        log.debug("Output data: {}", JsonUtils.toJsonString(output));
        return output;
    }

    public Company info(CompanyQuery companyQuery) {
        // https://api.yonyoucloud.com/apilink/tempServicePages/3cd7c462-04d3-4878-b498-0f1e4b3219c5_true.html
        String name = companyQuery.getName();
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        try {
            log.info("---- Begin \"info\" ----");
            log.info("Input data: {}", name);
            name = URLEncoder.encode(name, UTF_8);
            // Build request.
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setUrl(String.format(BASE_INFO_URL_FORMAT, name));
            httpRequest.setMethod(HttpMethod.GET);
            httpRequest.setReadTimeout(timeout);
            httpRequest.setCharset(UTF_8);
            httpRequest.addHeader(AUTHORIZATION_HEADER, API_CODE_HEADER);
            httpRequest.addHeader(API_CODE_HEADER, baseInfoApiCode);
            HttpResponse httpResponse = HttpUtils.getHttpClient().execute(httpRequest);
            log.info("Response status: {}", httpResponse.getStatusCode());
            log.debug("Response data: {}", JsonUtils.toJsonString(httpResponse));
            // Parse result.
            String bodyAsString = httpResponse.getBodyAsString(UTF_8);
            Map<String, Object> resultMap = parseResult(bodyAsString);
            if (MapUtils.isEmpty(resultMap)) { return logOutput(null); }
            // Handle result.
            return cast(logOutput(build(resultMap)));
        }
        catch (Exception e) {
            log.info("Error message: {}", e.getMessage());
            throw ExceptionUtils.wrap(e);
        }
        finally {
            log.info("---- End \"info\" ----");
        }
    }

    public List<Company> search(CompanyQuery companyQuery) {
        // https://api.yonyoucloud.com/apilink/tempServicePages/04ef434b-0170-4287-ba45-ec317fac88a3_true.html
        String name = companyQuery.getName();
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        try {
            log.info("---- Begin \"search\" ----");
            log.info("Input data: {}", name);
            name = URLEncoder.encode(name, UTF_8);
            // Build request.
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setUrl(String.format(SEARCH_URL_FORMAT, name));
            httpRequest.setMethod(HttpMethod.GET);
            httpRequest.setReadTimeout(timeout);
            httpRequest.setCharset(UTF_8);
            httpRequest.addHeader(AUTHORIZATION_HEADER, API_CODE_HEADER);
            httpRequest.addHeader(API_CODE_HEADER, searchApiCode);
            HttpResponse httpResponse = HttpUtils.getHttpClient().execute(httpRequest);
            log.info("Response status: {}", httpResponse.getStatusCode());
            log.debug("Response data: {}", JsonUtils.toJsonString(httpResponse));
            // Parse result.
            String bodyAsString = httpResponse.getBodyAsString(UTF_8);
            Map<String, Object> resultMap = parseResult(bodyAsString);
            List<Company> resultList = emptyList();
            if (MapUtils.isEmpty(resultMap)) { return cast(logOutput(resultList)); }
            Iterable items = (Iterable) resultMap.get("items");
            if (items == null) { return cast(logOutput(resultList)); }
            // Handle result.
            resultList = new ArrayList<Company>();
            for (Object item : items) {
                if (item == null) { continue; }
                Map<String, Object> beanMap = BeanUtils.beanToMap(item);
                resultList.add(build(beanMap));
            }
            return cast(logOutput(resultList));
        }
        catch (Exception e) {
            log.info("Error message: {}", e.getMessage());
            throw ExceptionUtils.wrap(e);
        }
        finally {
            log.info("---- End \"search\" ----");
        }
    }

    @Override
    public <T> T execute(Object input, Class<T> clazz) {
        CompanyQuery companyQuery = (CompanyQuery) input;
        if (List.class.equals(clazz)) {
            isSupport(new Class[]{ List.class }, clazz);
            return cast(search(companyQuery));
        }
        else {
            isSupport(new Class[]{ Company.class }, clazz);
            return cast(info(companyQuery));
        }
    }

}
