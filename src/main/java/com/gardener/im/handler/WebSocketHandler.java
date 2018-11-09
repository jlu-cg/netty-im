package com.gardener.im.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * websocket处理类
 * @author gardener
 *
 */
public class WebSocketHandler{

	public void handle(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		
	}
	
	public static WebSocketHandler getInstance(){
        return WebSocketHandler.InnerClass.SINGLETON;
    }
	
	private static class InnerClass{
		private static WebSocketHandler SINGLETON = new WebSocketHandler();
	}
}
