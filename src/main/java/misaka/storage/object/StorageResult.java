package misaka.storage.object;

import artoria.data.AbstractExtendedData;

import java.io.Serializable;

/**
 * The stored result of the object.
 * @author Kahle
 */
public class StorageResult extends AbstractExtendedData implements Serializable {
    /**
     * Business id.
     */
    private String businessId;

    public String getBusinessId() {

        return businessId;
    }

    public void setBusinessId(String businessId) {

        this.businessId = businessId;
    }

}
