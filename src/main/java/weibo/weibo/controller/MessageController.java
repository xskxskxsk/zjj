package weibo.weibo.controller;


import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import weibo.weibo.annotation.Audit;
import weibo.weibo.annotation.LoginUser;
import weibo.weibo.model.*;
import weibo.weibo.model.RetObject.MsgRet;
import weibo.weibo.model.UserHolder;
import weibo.weibo.service.MessageService;
import weibo.weibo.service.UserService;
import weibo.weibo.util.Common;
import weibo.weibo.util.ReturnObject;
import weibo.weibo.util.WeiboUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    UserHolder userHolder;



    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "Token",required = true),
            @ApiImplicitParam(paramType = "path",dataType = "String",name = "conversationId",value = "资讯id",required = true)
    })
    @Audit
    @ResponseBody
    @GetMapping("msg/{conversationId}")
    public Object ConsersationDetail(@LoginUser @ApiIgnore @RequestParam(required = false) Long userId,
                                     @PathVariable("conversationId") String converstionId) {
        try {
            List<MsgRet> messages = new ArrayList<>();
            List<Message> messageList = messageService.getConversationDetail(converstionId, 0, 10);
            for (Message msg : messageList) {
                User userFrom=userService.getUser(msg.getFromId());
                User userTo=userService.getUser(msg.getToId());
                MsgRet msgRet=new MsgRet(msg,userFrom.getHeadUrl(),userFrom.getName(),userTo.getHeadUrl(),userTo.getName(),0);
                messages.add(msgRet);
            }
            return Common.getListRetObject(new ReturnObject<>(messages));
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
            return WeiboUtil.getJSONString(1,"获取站内信失败");
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "Token",required = true)
    })
    @Audit
    @ResponseBody
    @GetMapping("msg/list")
    public Object ConversationList(@LoginUser @ApiIgnore @RequestParam(required = false) Long userId,Model model) {
        try {
            int localUserId = userId.intValue();
            List<MsgRet> conversations = new ArrayList<>();
            List<Message> messageList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : messageList) {

                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);

                int totalCount=messageService.getTotalCount(localUserId, msg.getConversationId())+messageService.getTotalCount(targetId, msg.getConversationId());
                MsgRet vo = new MsgRet(msg,null,null,user.getHeadUrl(),user.getName(),totalCount);
//                vo.set("setUnreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            return Common.getListRetObject(new ReturnObject<>(conversations));
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
            return WeiboUtil.getJSONString(1,"获取站内信列表失败");
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "Token",required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "toId",value = "接受者id",required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "content", value = "消息内容", required = false)
    })
    @Audit
    @PostMapping("/msg/addMessage/{toId}")
    @ResponseBody
    public String addMessage(@LoginUser @ApiIgnore @RequestParam(required = false) Long fromId,
                             @PathVariable("toId") int toId,
                             @RequestParam("content") String content) {
        Message message = new Message();
        message.setContent(content);
        message.setFromId(fromId.intValue());
        message.setToId(toId);
        message.setCreateDate(new Date());
        message.setConversationId(fromId.intValue() < toId ? String.format("%s_%s", userService.getUser(fromId.intValue()).getName(), userService.getUser(toId).getName()) : String.format("%s_%s", userService.getUser(toId).getName(), userService.getUser(fromId.intValue()).getName()));
        messageService.addMessage(message);
        return WeiboUtil.getJSONString(0,"发送成功");
    }

}
