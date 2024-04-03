package misaka.storage.minio;

import artoria.exception.ExceptionUtils;
import artoria.storage.support.AbstractStreamStorage;
import artoria.util.Assert;
import artoria.util.CloseUtils;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.ErrorResponse;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collection;

import static artoria.util.ObjectUtils.cast;

@Deprecated // TODO: 2023/3/23 Deletable
public class MinioStorage extends AbstractStreamStorage {
    private static Logger log = LoggerFactory.getLogger(MinioStorage.class);
    private final MinioClient minioClient;
    private final String bucketName;
    private final Long partSize;

    public MinioStorage(String name, MinioClient minioClient, String bucketName) {

        this(name, minioClient, bucketName, null, null);
    }

    public MinioStorage(String name, MinioClient minioClient, String bucketName, Long partSize, String charset) {
        super(name, charset);
        Assert.notBlank(bucketName, "Parameter \"bucketName\" must not blank. ");
        Assert.notNull(minioClient, "Parameter \"minioClient\" must not null. ");
        if (partSize == null) { partSize = 5L * 1024 * 1024; }
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        this.partSize = partSize;
    }

    @Override
    public MinioClient getNative() {

        return minioClient;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        GetObjectResponse objectResponse;
        try {
            objectResponse = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(getKeyString(key))
                    .build());
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        if (objectResponse == null) { return null; }
        if (GetObjectResponse.class.isAssignableFrom(type)) {
            return cast(objectResponse);
        }
        if (InputStream.class.isAssignableFrom(type)) {
            return cast(objectResponse);
        }
        try {
            return convertToResult(objectResponse, getCharset(), type);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        finally {
            CloseUtils.closeQuietly(objectResponse);
        }
    }

    @Override
    public Object get(Object key) {

        return get(key, GetObjectResponse.class);
    }

    @Override
    public boolean containsKey(Object key) {
        String keyStr = getKeyString(key);
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName).object(keyStr).build());
            return true;
        }
        catch (ErrorResponseException e) {
            ErrorResponse errorResponse = e.errorResponse();
            String code = errorResponse.code();
            if ("NoSuchKey".equals(code)) { return false; }
            else { throw ExceptionUtils.wrap(e); }
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public Object put(Object key, Object value) {
        String keyStr = getKeyString(key);
        InputStream inputStream = null;
        try {
            inputStream = convertToStream(value, getCharset());
            return minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(keyStr)
                    .stream(inputStream, -1, partSize)
                    .build());
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
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(getKeyString(key))
                    .build());
            return null;
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    @Override
    public <T> Collection<T> keys(Object pattern, Class<T> type) {
        // todo minioClient keys
        ListObjectsArgs args = null;
        Iterable<Result<Item>> results = minioClient.listObjects(args);
        return super.keys(pattern, type);
    }

}
