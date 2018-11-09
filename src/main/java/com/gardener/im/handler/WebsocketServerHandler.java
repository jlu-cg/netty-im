package com.gardener.im.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * websocket服务
 * @author gardener
 *
 */
public class WebsocketServerHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			HttpHandler.getInstance().handle(ctx, (FullHttpRequest) msg);
		}else if (msg instanceof WebSocketFrame) {
			WebSocketHandler.getInstance().handle(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
