/**
 * ==================================================================================
 * <p>
 * <p>
 * <p>
 * ==================================================================================
 */
package com.gameengine.system.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.github.sd4324530.fastweixin.handle.EventHandle;
import com.github.sd4324530.fastweixin.handle.MessageHandle;
import com.github.sd4324530.fastweixin.message.BaseMsg;
import com.github.sd4324530.fastweixin.message.TextMsg;
import com.github.sd4324530.fastweixin.message.req.BaseEvent;
import com.github.sd4324530.fastweixin.message.req.TextReqMsg;
import com.github.sd4324530.fastweixin.servlet.WeixinControllerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @公司名称: 深圳原型信息技术有限公司
 * @类功能说明：
 * @类修改者： caoyangjie
 * @类作者： caoyangjie
 * @创建日期： 17:11 2018/8/28
 * @修改说明：
 * @修改日期： 17:11 2018/8/28
 * @版本号： V1.0.0
 */
@RestController
@RequestMapping("/weixinbind")
public class WeixinController extends WeixinControllerSupport {

    @Value("${social.auth.wechat.clientId:123456}")
    private String appId;
    @Value("${social.auth.wechat.clientSecret:123456}")
    private String key;
    @Value("${social.auth.wechat.token:myToken}")
    private String token;

    private Logger log = LoggerFactory.getLogger(WeixinController.class);

    @Autowired
    @Qualifier("subscribeHandler")
    protected EventHandle<BaseEvent> subscribeHandler;

    @Override
    protected String getToken() {
        return token;
    }

    /**
     * 使用安全模式时设置：APPID
     */
    @Override
    protected String getAppId() {
        return appId;
    }
    /**
     * 使用安全模式时设置：密钥
     */
    @Override
    protected String getAESKey() {
        return null;
    }

    /**
     * 用户关注监听事件
     * @param event
     * @return
     */
    @Override
    protected BaseMsg handleSubscribe(BaseEvent event) {
        return null;
    }

    /**
     * 重写父类方法，处理对应的微信消息
     */
    @Override
    protected BaseMsg handleTextMsg(TextReqMsg msg) {
//        String content = msg.getContent();
//        log.debug("用户发送到服务器的内容:{}", content);
//        return new TextMsg("服务器回复用户消息!");
        return null;
    }

    /**
     * 1.1版本新增，重写父类方法，加入自定义微信消息处理器
     * 不是必须的，上面的方法是统一处理所有的文本消息，如果业务觉复杂，上面的会显得比较乱
     * 这个机制就是为了应对这种情况，每个MessageHandle就是一个业务，只处理指定的那部分消息
     */
    @Override
    protected List<MessageHandle> initMessageHandles() {
        List<MessageHandle> handles = new ArrayList<MessageHandle>();
        handles.addAll(SpringUtil.getBeansOfType(MessageHandle.class).values());
        return handles;
    }

    @Override
    protected List<EventHandle> initEventHandles() {
        List<EventHandle> handles = new ArrayList<EventHandle>();
        handles.add(subscribeHandler);
        return handles;
    }
}
