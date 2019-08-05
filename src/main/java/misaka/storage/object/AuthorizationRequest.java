package misaka.storage.object;

import artoria.data.AbstractExtendedData;

import java.io.Serializable;

/**
 * Object storage authorization request.
 * @author Kahle
 */
public class AuthorizationRequest extends AbstractExtendedData implements Serializable {
    /**
     * The name of the bucket.
     */
    private String bucketName;
    /**
     * Directory to operate on.
     */
    private String directory;
    /**
     * Expire time (Unit: milliseconds).
     */
    private Long expireTime;
    /**
     * Callback address.
     */
    private String callbackAddress;

    public String getBucketName() {

        return bucketName;
    }

    public void setBucketName(String bucketName) {

        this.bucketName = bucketName;
    }

    public String getDirectory() {

        return directory;
    }

    public void setDirectory(String directory) {

        this.directory = directory;
    }

    public Long getExpireTime() {

        return expireTime;
    }

    public void setExpireTime(Long expireTime) {

        this.expireTime = expireTime;
    }

    public String getCallbackAddress() {

        return callbackAddress;
    }

    public void setCallbackAddress(String callbackAddress) {

        this.callbackAddress = callbackAddress;
    }

}
