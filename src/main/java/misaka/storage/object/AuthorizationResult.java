package misaka.storage.object;

import artoria.data.AbstractExtendedData;

import java.io.Serializable;

/**
 * Object storage authorization result.
 * @author Kahle
 */
public class AuthorizationResult extends AbstractExtendedData implements Serializable {
    private String accessId;
    private String host;
    private String directory;
    private String policy;
    private String signature;
    private Long expireTime;
    private String callbackContent;

    public String getAccessId() {

        return accessId;
    }

    public void setAccessId(String accessId) {

        this.accessId = accessId;
    }

    public String getHost() {

        return host;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public String getDirectory() {

        return directory;
    }

    public void setDirectory(String directory) {

        this.directory = directory;
    }

    public String getPolicy() {

        return policy;
    }

    public void setPolicy(String policy) {

        this.policy = policy;
    }

    public String getSignature() {

        return signature;
    }

    public void setSignature(String signature) {

        this.signature = signature;
    }

    public Long getExpireTime() {

        return expireTime;
    }

    public void setExpireTime(Long expireTime) {

        this.expireTime = expireTime;
    }

    public String getCallbackContent() {

        return callbackContent;
    }

    public void setCallbackContent(String callbackContent) {

        this.callbackContent = callbackContent;
    }

}
