/**
 * ==================================================================================
 * <p>
 * <p>
 * <p>
 * ==================================================================================
 */
package com.gameengine.system.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.sd4324530.fastweixin.handle.EventHandle;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.req.BaseEvent;
import com.github.sd4324530.fastweixin.message.req.EventType;

/**
 * @公司名称: 深圳原型信息技术有限公司
 * @类功能说明：
 * @类修改者： caoyangjie
 * @类作者： caoyangjie
 * @创建日期： 17:50 2018/8/28
 * @修改说明：
 * @修改日期： 17:50 2018/8/28
 * @版本号： V1.0.0
 */
@Service("subscribeHandler")
public class WeixinSubscribeHandler implements EventHandle<BaseEvent> {
    private Logger logger = LoggerFactory.getLogger(WeixinSubscribeHandler.class);

    @Override
    public BaseMsg handle(BaseEvent event) {
        String openId = event.getFromUserName();
        if( isNotExist(openId) ){
            logger.info("事件 eventType：" + event.getMsgType() + ",openid=" + openId);
        }
        return null;
    }

    private boolean isNotExist(String openId) {
        return true;
    }

    @Override
    public boolean beforeHandle(BaseEvent event) {
        return EventType.SUBSCRIBE.equalsIgnoreCase(event.getEvent());
    }
}
