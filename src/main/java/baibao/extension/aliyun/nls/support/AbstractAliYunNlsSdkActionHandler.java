package baibao.extension.aliyun.nls.support;

import baibao.extension.aliyun.nls.dto.TokenResp;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nls.client.AccessToken;
import kunlun.action.support.AbstractOperateActionHandler;
import kunlun.exception.ExceptionUtils;
import kunlun.util.Assert;

import java.io.Serializable;

import static java.lang.Boolean.FALSE;

/**
 * The AliYun nature language service.
 * @see <a href="https://ai.aliyun.com/nls/trans">nls</a>
 * @see <a href="https://help.aliyun.com/document_detail/119258.html">nls document</a>
 * @author Kahle
 */
public abstract class AbstractAliYunNlsSdkActionHandler extends AbstractOperateActionHandler {

    /**
     * Get the AliYun nls configuration according to the arguments.
     * @param input The input arguments
     * @return The AliYun nls configuration
     */
    protected abstract Config getConfig(Object input);

    @Override
    public Object operate(Object input, String name, Class<?> clazz) {
        if ("getToken".equals(name)) { return getToken(input, clazz); }
        else {
            throw new UnsupportedOperationException(
                    "The method is unsupported. \n\n" +
                            "The aliyun nls sdk handler. \n" +
                            "\n" +
                            "Supported method:\n" +
                            " - getToken\n"
            );
        }
    }

    public Object getToken(Object input, Class<?> clazz) {
        //
        Assert.isSupport(clazz, FALSE, TokenResp.class, Object.class);
        Config config = getConfig(input);
        String accessKeySecret = config.getAccessKeySecret();
        String accessKeyId = config.getAccessKeyId();
        String domain = config.getDomain();
        String regionId = config.getRegionId();
        String version = config.getVersion();
        //
        AccessToken accessToken;
        if (StrUtil.isNotBlank(domain)&&
                StrUtil.isNotBlank(regionId)&&
                StrUtil.isNotBlank(version)) {
            accessToken = new AccessToken(
                    accessKeyId, accessKeySecret, domain, regionId, version);
        }
        else { accessToken = new AccessToken(accessKeyId, accessKeySecret); }
        //
        try { accessToken.apply(); }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        //
        String token = accessToken.getToken();
        long expireTime = accessToken.getExpireTime();
        return new TokenResp(token, expireTime);
    }

    public static class Config implements Serializable {
        private String  accessKeyId;
        private String  accessKeySecret;
        private String  domain;
        private String  regionId;
        private String  version;
        private Boolean debug;

        public Config(String accessKeyId, String accessKeySecret, Boolean debug) {
            this.accessKeySecret = accessKeySecret;
            this.accessKeyId = accessKeyId;
            this.debug = debug;
        }

        public Config() {

        }

        public String getAccessKeyId() {

            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {

            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {

            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {

            this.accessKeySecret = accessKeySecret;
        }

        public String getDomain() {

            return domain;
        }

        public void setDomain(String domain) {

            this.domain = domain;
        }

        public String getRegionId() {

            return regionId;
        }

        public void setRegionId(String regionId) {

            this.regionId = regionId;
        }

        public String getVersion() {

            return version;
        }

        public void setVersion(String version) {

            this.version = version;
        }

        public Boolean getDebug() {

            return debug;
        }

        public void setDebug(Boolean debug) {

            this.debug = debug;
        }

    }

}
