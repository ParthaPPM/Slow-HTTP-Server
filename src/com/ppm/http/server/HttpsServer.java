package com.ppm.http.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpsServer implements Server
{
	private final int port;
	private final String host;
	private boolean isServerRunning;
	private ServerSocket serverSocket;
	private RequestProcessor requestProcessor;
	private int timeoutInMilliSeconds;

	public HttpsServer()
	{
		this(443);
	}

	public HttpsServer(int port)
	{
		this(port, null);
	}

	public HttpsServer(int port, String host)
	{
		// these steps are for ssl certificate
		String keyStoreFileName = "";
		String keyStorePassword = "";
		try
		{
			String currentDirectory = System.getProperty("user.dir");
			File file = new File(currentDirectory + "\\server.config");
			if (file.isFile())
			{
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				while((line = br.readLine()) != null)
				{
					int equalsIndex = line.indexOf('=');
					String key = line.substring(0, equalsIndex);
					String value = line.substring(equalsIndex+1);
					if (key.equals("keyStoreFileName"))
					{
						keyStoreFileName = value;
					}
					if (key.equals("password"))
					{
						keyStorePassword = value;
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.setProperty("javax.net.ssl.keyStore", keyStoreFileName);
		System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);

		this.port = port;
		this.host = host;
		isServerRunning = false;
		requestProcessor = new RequestProcessor();
		this.timeoutInMilliSeconds = 60000;
	}

	public void setRequestProcessor(RequestProcessor requestProcessor)
	{
		this.requestProcessor = requestProcessor;
	}

	public void setTimeout(int milliSeconds)
	{
		this.timeoutInMilliSeconds = milliSeconds;
	}

	public void start()
	{
		System.out.println("Server started at port "+port);
		isServerRunning = true;
		try
		{
			ServerSocketFactory socketFactory = SSLServerSocketFactory.getDefault();
			if (host == null)
			{
				serverSocket = socketFactory.createServerSocket(port);
			}
			else
			{
				serverSocket = socketFactory.createServerSocket(port, 0, InetAddress.getByName(host));
			}
			while (isServerRunning)
			{
				Socket socket = serverSocket.accept();
				RequestHandler requestHandler = new RequestHandler(socket, requestProcessor, timeoutInMilliSeconds);
				Thread t = new Thread(requestHandler::handle);
				t.start();
			}
			serverSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			stop();
		}
	}

	public void stop()
	{
		isServerRunning = false;
		try
		{
			if (serverSocket!=null)
			{
				serverSocket.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean isServerRunning()
	{
		return isServerRunning;
	}
}
