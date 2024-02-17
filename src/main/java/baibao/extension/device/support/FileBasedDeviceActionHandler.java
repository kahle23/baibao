package baibao.extension.device.support;

import baibao.extension.device.Device;
import baibao.extension.device.DeviceQuery;
import kunlun.action.support.AbstractClassicActionHandler;
import kunlun.data.bean.BeanUtils;
import kunlun.file.Csv;
import kunlun.util.Assert;
import kunlun.util.ObjectUtils;
import kunlun.util.RecombineUtils;

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
