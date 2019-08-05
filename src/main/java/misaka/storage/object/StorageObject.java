package misaka.storage.object;

import artoria.util.CloseUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Object in the object storage.
 * @author Kahle
 */
public class StorageObject implements Closeable, Serializable {
    /**
     * Original function object.
     */
    private Object original;
    /**
     * Object's bucket name.
     */
    private String bucketName;
    /**
     * Object key (name).
     */
    private String objectKey;
    /**
     * Object's content.
     */
    private InputStream objectContent;
    /**
     * Object's metadata.
     */
    private Map<String, Object> metadata = new LinkedHashMap<String, Object>();

    public Object getOriginal() {

        return original;
    }

    public void setOriginal(Object original) {

        this.original = original;
    }

    public String getBucketName() {

        return bucketName;
    }

    public void setBucketName(String bucketName) {

        this.bucketName = bucketName;
    }

    public String getObjectKey() {

        return objectKey;
    }

    public void setObjectKey(String objectKey) {

        this.objectKey = objectKey;
    }

    public InputStream getObjectContent() {

        return objectContent;
    }

    public void setObjectContent(InputStream objectContent) {

        this.objectContent = objectContent;
    }

    public Map<String, Object> getMetadata() {

        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {

        this.metadata = metadata;
    }

    @Override
    public void close() throws IOException {
        CloseUtils.closeQuietly(objectContent);
        if (original instanceof Closeable) {
            CloseUtils.closeQuietly((Closeable) original);
        }
    }

}
