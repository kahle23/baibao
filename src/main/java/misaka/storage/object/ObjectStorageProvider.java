package misaka.storage.object;

import artoria.lifecycle.Destroyable;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * Object storage provider.
 * @see <a href="https://en.wikipedia.org/wiki/Object_storage">Object storage</a>
 * @author Kahle
 */
public interface ObjectStorageProvider extends Destroyable {

    /**
     * Get authorization information.
     * @param request Parameters needed to get authorization information
     * @return The result of authorization information
     */
    AuthorizationResult authorization(AuthorizationRequest request);

    /**
     * Get the object from the bucket.
     * @param bucketName The name of the bucket
     * @param objectKey Object's key
     * @return Fetched the object of the specified object key
     */
    StorageObject getObject(String bucketName, String objectKey);

    /**
     * Put Object by local file.
     * @param bucketName The name of the bucket
     * @param objectKey Object's key
     * @param file File that will be uploaded
     * @return OSS generic result
     */
    StorageResult putObject(String bucketName, String objectKey, File file);

    /**
     * Put Object by local file and metadata.
     * @param bucketName The name of the bucket
     * @param objectKey Object's key
     * @param file File that will be uploaded
     * @param metadata The metadata you want to set
     * @return OSS generic result
     */
    StorageResult putObject(String bucketName, String objectKey, File file, Map<String, Object> metadata);

    /**
     * Put Object by input stream.
     * @param bucketName The name of the bucket
     * @param objectKey Object's key
     * @param inputStream Input stream that will be uploaded
     * @return OSS generic result
     */
    StorageResult putObject(String bucketName, String objectKey, InputStream inputStream);

    /**
     * Put Object by input stream and metadata.
     * @param bucketName The name of the bucket
     * @param objectKey Object's key
     * @param inputStream Input stream that will be uploaded
     * @param metadata The metadata you want to set
     * @return OSS generic result
     */
    StorageResult putObject(String bucketName, String objectKey, InputStream inputStream, Map<String, Object> metadata);

}
