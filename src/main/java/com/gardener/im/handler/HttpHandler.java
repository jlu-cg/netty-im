package com.gardener.im.handler;

import java.io.File;
import java.io.RandomAccessFile;

import com.gardener.im.constant.ImConstant;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

/**
 * http协议处理
 * @author gardener
 *
 */
public class HttpHandler {

	public void handle(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		String uri = request.uri();
		if ("favicon.ico".equals(uri)) {
			return;
		}

		String path = ImConstant.LOCATION + uri;
		File staticFile = new File(path);

		if (!staticFile.exists()) {
			staticFile = ImConstant.NOT_FOUND;
		}

		RandomAccessFile file = new RandomAccessFile(staticFile, "r");
		HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);

		if (staticFile == ImConstant.NOT_FOUND) {
			response.setStatus(HttpResponseStatus.NOT_FOUND);
		}

		if(path.endsWith(".html")) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
		}else if(path.endsWith(".js")) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/x-javascript");
		}else if(path.endsWith(".ico")) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/x-icon");
		}else if(path.endsWith(".css")) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/css; charset=UTF-8");
		}else if(path.endsWith(".jpeg")) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/jpeg");
		}else if(path.endsWith(".jpg")) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/x-jpg");
		}else if(path.endsWith(".png")) {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/x-png");
		}else {
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
		}

		boolean keepAlive = HttpHeaderUtil.isKeepAlive(request);

		if (keepAlive) {
			response.headers().setLong(HttpHeaderNames.CONTENT_LENGTH, file.length());
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		ctx.write(response);

		if (ctx.pipeline().get(SslHandler.class) == null) {
			ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
		} else {
			ctx.write(new ChunkedNioFile(file.getChannel()));
		}
		// 写入文件尾部
		ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if (!keepAlive) {
			future.addListener(ChannelFutureListener.CLOSE);
		}

		file.close();
	}
	
	public static HttpHandler getInstance(){
        return HttpHandler.InnerClass.SINGLETON;
    }
	
	private static class InnerClass{
		private static HttpHandler SINGLETON = new HttpHandler();
	}
}
