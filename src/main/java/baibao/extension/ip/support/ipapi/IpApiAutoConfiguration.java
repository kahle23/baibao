/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.extension.ip.support.ipapi;

import kunlun.action.ActionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * Network physical address auto configuration.
 * @author Kahle
 */
@Configuration
public class IpApiAutoConfiguration implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(IpApiAutoConfiguration.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        IpApiIpActionHandler handler = new IpApiIpActionHandler();
        String actionName = "ip-query-ipapi";
        ActionUtils.registerHandler(actionName, handler);
    }

    @Override
    public void destroy() throws Exception {
    }

}
