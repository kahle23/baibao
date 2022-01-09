package misaka.app.push;

import artoria.common.GenericResult;
import artoria.data.AbstractExtraData;
import artoria.data.RawData;

import java.io.Serializable;

public class PushResult extends AbstractExtraData implements RawData, GenericResult, Serializable {
    private Object rawData;
    private String code;
    private String message;

    @Override
    public Object rawData() {

        return rawData;
    }

    @Override
    public void rawData(Object rawData) {

        this.rawData = rawData;
    }

    @Override
    public String getCode() {

        return code;
    }

    @Override
    public void setCode(String code) {

        this.code = code;
    }

    @Override
    public String getMessage() {

        return message;
    }

    @Override
    public void setMessage(String message) {

        this.message = message;
    }

}
