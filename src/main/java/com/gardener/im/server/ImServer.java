package com.gardener.im.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gardener.im.constant.ImConstant;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ImServer {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ImServer.class);

	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ImInitializer())
				.option(ChannelOption.SO_BACKLOG, 128);
			
			Channel ch = b.bind(ImConstant.DEFAULT_SERVER_PORT).sync().channel();
			LOGGER.info("start ready");
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
