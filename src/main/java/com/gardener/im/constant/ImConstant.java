package com.gardener.im.constant;

import java.io.File;

/**
 * 聊天常量
 * @author gardener
 *
 */
public class ImConstant {
	
	public static final String LOCATION = System.getProperty("user.dir") + "/src/main/resources/static";
	
	public static final File NOT_FOUND = new File(LOCATION + "/404.html");
	
	public static final int DEFAULT_SERVER_PORT = 8090;
	
	public static final String WEB_SOCKET_PREFIX = "/ws";
	
	public static final String FROM_USER_ID = "fromId";
}
