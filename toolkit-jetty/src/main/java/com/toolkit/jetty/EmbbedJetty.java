package com.toolkit.jetty;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 内 嵌 jetty http 服务
 * 
 * @author joe.zhao(zhaohaolin2010@gmail.com) 2012-5-17
 */
public class EmbbedJetty {
	
	private final static Logger	LOG			= Logger.getAnonymousLogger();
	private String				hostname	= "localhost";
	private int					port		= 8089;
	private String				webAppDir	= "webapp";
	private String				contextPath	= "/";
	private int					threadNum	= 256;
	private String				URIEncoding	= "UTF-8";
	private Server				server		= null;
	private String				baseDir		= ".";
	private String				appBase		= ".";
	
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getWebAppDir() {
		return webAppDir;
	}
	
	public void setWebAppDir(String webAppDir) {
		this.webAppDir = webAppDir;
	}
	
	public String getContextPath() {
		return contextPath;
	}
	
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public String getBaseDir() {
		return baseDir;
	}
	
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	
	public void setURIEncoding(String uRIEncoding) {
		URIEncoding = uRIEncoding;
	}
	
	public void setAppBase(String appBase) {
		this.appBase = appBase;
	}
	
	public void setWebPath(String webPath) {
		this.webAppDir = webPath;
	}
	
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	private void start() throws Exception {
		server = new Server();
		
		// 连接池
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		connector.setMaxIdleTime(30000);
		connector.setRequestHeaderSize(8192);
		
		QueuedThreadPool threadPool = new QueuedThreadPool(threadNum);
		threadPool.setName("embbed-jetty-http");
		connector.setThreadPool(threadPool);
		
		server.setConnectors(new Connector[] { connector });
		// Context context = null;
		ServletContextHandler context = null;
		
		if (webAppDir != null) {
			LOG.info("load webPath=" + webAppDir);
			
			context = new WebAppContext(System.getProperty("user.dir")
					+ File.separator + webAppDir, contextPath);
			
		} else {
			context = new ServletContextHandler(server, contextPath);
		}
		
		server.setHandler(context);
		
		server.start();
		LOG.info("jetty embbed server started, port=" + port);
		
		// add shutdown hook to stop server
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				try {
					server.stop();
					server.destroy();
				} catch (Exception e) {
					LOG.log(Level.WARNING, "run jetty stop error!", e);
				}
			}
			
		});
		
		server.join();
	}
	
	public static void main(String[] args) {
		int port = 0;
		String appBase = null;
		String contextPath = null;
		String webAppDir = null;
		String baseDir = null;
		String URIEncoding = null;
		for (String arg : args) {
			if (arg.startsWith("-httpPort")) {
				port = Integer.parseInt(arg.substring(arg.indexOf("=") + 1));
			}
			if (arg.startsWith("-appBase")) {
				appBase = arg.substring(arg.indexOf("=") + 1);
			}
			if (arg.startsWith("-contextPath")) {
				contextPath = arg.substring(arg.indexOf("=") + 1);
			}
			if (arg.startsWith("-webAppDir")) {
				webAppDir = arg.substring(arg.indexOf("=") + 1);
			}
			if (arg.startsWith("-baseDir")) {
				baseDir = arg.substring(arg.indexOf("=") + 1);
			}
			if (arg.startsWith("-URIEncoding")) {
				URIEncoding = arg.substring(arg.indexOf("=") + 1);
			}
		}
		
		final EmbbedJetty jetty = new EmbbedJetty();
		if (port > 0) {
			jetty.setPort(port);
		}
		if (appBase != null && appBase.length() > 0) {
			jetty.setAppBase(appBase);
		}
		if (contextPath != null && contextPath.length() > 0) {
			jetty.setContextPath(contextPath);
		}
		if (webAppDir != null && webAppDir.length() > 0) {
			jetty.setWebAppDir(webAppDir);
		}
		if (baseDir != null && baseDir.length() > 0) {
			jetty.setBaseDir(baseDir);
		}
		
		try {
			jetty.start();
			LOG.info("server started");
		} catch (Throwable e) {
			LOG.log(Level.WARNING, "Server Start Error: ", e);
			System.exit(-1);
		}
	}
	
}
