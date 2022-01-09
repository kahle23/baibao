package misaka.pay;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.COLON;

public class PayUtils {
    private static final Map<String, PayProvider> PROVIDER_MAP = new ConcurrentHashMap<String, PayProvider>();
    private static Logger log = LoggerFactory.getLogger(PayUtils.class);

    private static String providerName(String appId, String appType, String payWay) {
        Assert.notBlank(appId, "Parameter \"appId\" must not blank. ");
        Assert.notBlank(payWay, "Parameter \"payWay\" must not blank. ");
        Assert.notBlank(appType, "Parameter \"appType\" must not blank. ");
        return (appId + COLON + appType + COLON + payWay).toUpperCase();
    }

    public static void unregister(String appId, String[] appTypes, String payWay) {
        Assert.notEmpty(appTypes, "Parameter \"appTypes\" must not empty. ");
        for (String appType : appTypes) {
            String providerName = providerName(appId, appType, payWay);
            PayProvider remove = PROVIDER_MAP.remove(providerName);
            if (remove == null) { continue; }
            String className = remove.getClass().getName();
            log.info("Unregister \"{}\" to \"{}\". ", className, providerName);
        }
    }

    public static void register(String appId, String[] appTypes, String payWay, PayProvider payProvider) {
        Assert.notNull(payProvider, "Parameter \"payProvider\" must not null. ");
        Assert.notEmpty(appTypes, "Parameter \"appTypes\" must not empty. ");
        for (String appType : appTypes) {
            String providerName = providerName(appId, appType, payWay);
            String className = payProvider.getClass().getName();
            log.info("Register \"{}\" to \"{}\". ", className, providerName);
            PROVIDER_MAP.put(providerName, payProvider);
        }
    }

    public static PayProvider payProvider(String appId, String appType, String payWay) {
        String providerName = providerName(appId, appType, payWay);
        PayProvider payProvider = PROVIDER_MAP.get(providerName);
        Assert.notNull(payProvider, "Can not find the pay provider. ");
        return payProvider;
    }

    public static OrderPayResult payOrder(OrderPayModel orderPayModel) {
        Assert.notNull(orderPayModel, "Parameter \"orderPayModel\" must not null. ");
        String appId = orderPayModel.getAppId();
        String payWay = orderPayModel.getPayWay();
        String appType = orderPayModel.getAppType();
        PayProvider payProvider = payProvider(appId, appType, payWay);
        return payProvider.payOrder(orderPayModel);
    }

    public static OrderQueryResult queryOrder(OrderQueryModel orderQueryModel) {
        Assert.notNull(orderQueryModel, "Parameter \"orderQueryModel\" must not null. ");
        String appId = orderQueryModel.getAppId();
        String payWay = orderQueryModel.getPayWay();
        String appType = orderQueryModel.getAppType();
        PayProvider payProvider = payProvider(appId, appType, payWay);
        return payProvider.queryOrder(orderQueryModel);
    }

    public static OrderCloseResult closeOrder(OrderCloseModel orderCloseModel) {
        Assert.notNull(orderCloseModel, "Parameter \"orderCloseModel\" must not null. ");
        String appId = orderCloseModel.getAppId();
        String payWay = orderCloseModel.getPayWay();
        String appType = orderCloseModel.getAppType();
        PayProvider payProvider = payProvider(appId, appType, payWay);
        return payProvider.closeOrder(orderCloseModel);
    }

    public static PayNotifyResult payNotify(PayNotifyModel payNotifyModel) {
        Assert.notNull(payNotifyModel, "Parameter \"payNotifyModel\" must not null. ");
        String appId = payNotifyModel.getAppId();
        String payWay = payNotifyModel.getPayWay();
        String appType = payNotifyModel.getAppType();
        PayProvider payProvider = payProvider(appId, appType, payWay);
        return payProvider.payNotify(payNotifyModel);
    }

}
