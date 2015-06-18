package de.pm.mindcloud.web.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

/**
 * Created on 29.04.15
 * This class is responsible
 */
@Component
public class InBoundChannel extends ChannelInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(InBoundChannel.class);
}
