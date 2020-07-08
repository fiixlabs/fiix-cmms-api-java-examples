package com.fiix.api.example.utils;

import com.ma.cmms.api.client.ProxyCredentials;

public class MyProxyCredentials implements ProxyCredentials
{
	String proxyHost = "localhost";

	int proxyPort = 8888;

	String proxyUser;

	String proxyPassword;

	@Override public String getProxyHost()
	{
		return proxyHost;
	}

	public void setProxyHost(String proxyHost)
	{
		this.proxyHost = proxyHost;
	}

	@Override public int getProxyPort()
	{
		return proxyPort;
	}

	public void setProxyPort(int proxyPort)
	{
		this.proxyPort = proxyPort;
	}

	@Override public String getProxyUser()
	{
		return proxyUser;
	}

	public void setProxyUser(String proxyUser)
	{
		this.proxyUser = proxyUser;
	}

	@Override public String getProxyPassword()
	{
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword)
	{
		this.proxyPassword = proxyPassword;
	}

}
