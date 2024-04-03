package misaka.hw.cloud.obs;

import artoria.exception.ExceptionUtils;
import artoria.storage.support.AbstractStreamStorage;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import artoria.util.ObjectUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.DeleteObjectsRequest;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObsObject;

import java.io.InputStream;
import java.util.Collection;

import static artoria.util.ObjectUtils.cast;

/**
 * https://support.huaweicloud.com/productdesc-obs/obs_03_0370.html
 */
@Deprecated // TODO: 2023/3/23 Deletable
public class ObsStorage extends AbstractStreamStorage {
    private final ObsClient obsClient;
    private final String bucketName;

    public ObsStorage(String name, ObsClient obsClient, String bucketName) {

        this(name, obsClient, bucketName, null);
    }

    public ObsStorage(String name, ObsClient obsClient, String bucketName, String charset) {
        super(name, charset);
        Assert.notBlank(bucketName, "Parameter \"bucketName\" must not blank. ");
        Assert.notNull(obsClient, "Parameter \"obsClient\" must not null. ");
        this.bucketName = bucketName;
        this.obsClient = obsClient;
    }

    @Override
    public ObsClient getNative() {

        return obsClient;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ObsObject obsObject = obsClient.getObject(bucketName, getKeyString(key));
        if (obsObject == null) { return null; }
        if (ObsObject.class.isAssignableFrom(type)) {
            return cast(obsObject);
        }
        InputStream inputStream = null;
        try {
            inputStream = obsObject.getObjectContent();
            return convertToResult(inputStream, getCharset(), type);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public Object get(Object key) {

        return obsClient.getObject(bucketName, getKeyString(key));
    }

    @Override
    public boolean containsKey(Object key) {

        return obsClient.doesObjectExist(bucketName, getKeyString(key));
    }

    @Override
    public Object put(Object key, Object value) {
        String keyStr = getKeyString(key);
        InputStream inputStream = null;
        try {
            inputStream = convertToStream(value, getCharset());
            return obsClient.putObject(bucketName, keyStr, inputStream);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public Object remove(Object key) {

        return obsClient.deleteObject(bucketName, getKeyString(key));
    }

    @Override
    public <T> Collection<T> keys(Object pattern, Class<T> type) {
        // todo obsClient listObjects
        String keyString = getKeyString(pattern);
        ObjectListing objectListing = obsClient.listObjects(keyString);
        return ObjectUtils.cast(objectListing.getObjects());
    }

}
