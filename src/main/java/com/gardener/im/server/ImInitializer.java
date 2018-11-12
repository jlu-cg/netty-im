package com.gardener.im.server;

import com.gardener.im.handler.HttpHandler;
import com.gardener.im.handler.WebSocketHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ImInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(65536));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpHandler("/ws"));
		pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true));
		pipeline.addLast(new WebSocketHandler());
	}

}
