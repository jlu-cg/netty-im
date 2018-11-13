package com.gardener.im.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * websocket处理类
 * @author gardener
 *
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
	
	private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static Map<ChannelId, String> USER_MAP = new ConcurrentHashMap<>(280);

	public void handle(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
 
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        LOGGER.info("{} received {}", ctx.channel(), request);
        ctx.channel().write(new TextWebSocketFrame(" 收到客户端请求："+request));
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		handle(ctx, msg);
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(HttpHandler.class);
            channelGroup.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            channelGroup.add(ctx.channel());
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            if (stateEvent.state() == IdleState.READER_IDLE) {
            	channelGroup.remove(ctx.channel());
                ctx.writeAndFlush(new TextWebSocketFrame("由于您长时间不在线，系统已自动把你踢下线！")).addListener(ChannelFutureListener.CLOSE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
	}
}
