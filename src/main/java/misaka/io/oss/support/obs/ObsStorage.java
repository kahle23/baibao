package misaka.io.oss.support.obs;

import artoria.data.bean.BeanUtils;
import artoria.exception.ExceptionUtils;
import artoria.io.oss.OssBase;
import artoria.io.oss.OssInfo;
import artoria.io.oss.OssObject;
import artoria.io.oss.support.AbstractOssStorage;
import artoria.io.oss.support.OssObjectImpl;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import com.obs.services.ObsClient;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Map;

/**
 * https://support.huaweicloud.com/productdesc-obs/obs_03_0370.html
 */
@Deprecated
public class ObsStorage extends AbstractOssStorage {
    private static final Logger log = LoggerFactory.getLogger(ObsStorage.class);
    private final ObsClient obsClient;

    public ObsStorage(ObsClient obsClient, Map<String, String> objectUrlPrefixes,
                      String defaultBucket) {
        super(objectUrlPrefixes, defaultBucket);
        Assert.notNull(obsClient, "Parameter \"obsClient\" must not null. ");
        this.obsClient = obsClient;
    }

    public ObsStorage(ObsClient obsClient, Map<String, String> objectUrlPrefixes) {

        this(obsClient, objectUrlPrefixes, null);
    }

    @Override
    public ObsClient getNative() {

        return obsClient;
    }

    @Override
    public OssObject get(Object key) {
        OssBase ossBase = getOssBase(key);
        String bucketName = ossBase.getBucketName();
        String objectKey = ossBase.getObjectKey();
        ObsObject obsObject = obsClient.getObject(bucketName, objectKey);
        if (obsObject == null) { return null; }
        return BeanUtils.beanToBean(obsObject, OssObjectImpl.class);
    }

    @Override
    public boolean exist(Object key) {
        OssBase ossBase = getOssBase(key);
        String bucketName = ossBase.getBucketName();
        String objectKey = ossBase.getObjectKey();
        return obsClient.doesObjectExist(bucketName, objectKey);
    }

    @Override
    public OssInfo put(Object data) {
        OssObject ossObject = convertToOssObject(data);
        InputStream inputStream = null;
        try {
            String bucketName = ossObject.getBucketName();
            String objectKey = ossObject.getObjectKey();
            inputStream = ossObject.getObjectContent();
            PutObjectResult putObjectResult = obsClient.putObject(bucketName, objectKey, inputStream);
            return buildOssInfo(putObjectResult.getBucketName(), putObjectResult.getObjectKey(),
                    putObjectResult.getObjectUrl(), putObjectResult);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public Object delete(Object key) {
        OssBase ossBase = getOssBase(key);
        String bucketName = ossBase.getBucketName();
        String objectKey = ossBase.getObjectKey();
        return obsClient.deleteObject(bucketName, objectKey);
    }

    @Override
    public Object list(Object conditions) {
        // todo obsClient listObjects
        // pattern
        String keyString = (String) conditions;
        ObjectListing objectListing = obsClient.listObjects(keyString);
        return objectListing.getObjects();
    }

}
