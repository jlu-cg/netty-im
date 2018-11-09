package com.gardener.im.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * websocket处理类
 * @author gardener
 *
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame>{

	@Override
	public void messageReceived(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		
	}

}
