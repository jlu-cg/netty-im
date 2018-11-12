package com.gardener.im.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * websocket处理类
 * @author gardener
 *
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	
	private static Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);
	
	private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public void handle(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
 
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        LOGGER.info("{} received {}", ctx.channel(), request);
        ctx.channel().write(new TextWebSocketFrame(" 收到客户端请求："+request));
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		handle(ctx, msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		channelGroup.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		channelGroup.remove(ctx.channel());
	}
}
