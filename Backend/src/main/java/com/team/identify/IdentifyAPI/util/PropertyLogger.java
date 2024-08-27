package com.team.identify.IdentifyAPI.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.logging.Logger;

// logs active properties to console upon startup
@Component
public class PropertyLogger implements InitializingBean {
    private static final Logger LOG
            = Logger.getLogger(PropertyLogger.class.getName());

    @Autowired
    private Environment environment;
    @Override
    public void afterPropertiesSet() {
        LOG.info(Arrays.asList(environment.getDefaultProfiles()).toString());
    }

}