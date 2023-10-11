package baibao.extension.device.support;

import artoria.action.handler.AbstractClassicActionHandler;
import artoria.data.bean.BeanUtils;
import artoria.file.Csv;
import artoria.util.Assert;
import artoria.util.ObjectUtils;
import artoria.util.RecombineUtils;
import baibao.extension.device.Device;
import baibao.extension.device.DeviceQuery;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Deprecated
public class FileBasedDeviceActionHandler extends AbstractClassicActionHandler {
    private final Map<String, Device> deviceMap;

    public FileBasedDeviceActionHandler(Csv csv) {
        List<Device> deviceList = BeanUtils.mapToBeanInList(csv.toMapList(), Device.class);
        Map<String, Device> modelMap = RecombineUtils.listToMapBean(deviceList, "model");
        deviceMap = Collections.unmodifiableMap(modelMap);
    }

    @Override
    public <T> T execute(Object input, Class<T> clazz) {
        isSupport(new Class[]{ Device.class }, clazz);
        DeviceQuery deviceQuery = (DeviceQuery) input;
        String model = deviceQuery.getModel();
        Assert.notBlank(model, "Parameter \"model\" must not blank. ");
        return ObjectUtils.cast(deviceMap.get(model));
    }

}
