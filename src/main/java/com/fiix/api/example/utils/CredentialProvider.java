package com.fiix.api.example.utils;

import com.ma.cmms.api.client.BasicCredentials;
import com.ma.cmms.api.client.Credentials;

/**
 * The class provides the {@link com.ma.cmms.api.client.BasicCredentials} object <br>
 * required to build an instance of {@link com.ma.cmms.api.client.FiixCmmsClient} <br>
 * Replace the keys and the tenant url with your tenant specific information to run the examples.
 */
public final class CredentialProvider {

	/*
	 * To get the below keys please refer to the 'Getting Started' section of the
	 * developer's guide
	 * https://fiixlabs.github.io/api-documentation/guide.html#getting_started
	 */
	private static final String API_APPLICATION_KEY = "YOUR-API_APPLICATION_KEY";
	private static final String API_ACCESS_KEY = "YOUR-API_ACCESS_KEY";
	private static final String API_SECRET_KEY = "YOUR-API_SECRET_KEY";

	private static final String TENANT_URL = "YOUR-TENANT_URL";
	public static final String TENANT_API_ENDPOINT = TENANT_URL + "/api/";

	private static final Credentials credentials = new BasicCredentials(API_APPLICATION_KEY, API_ACCESS_KEY,
			API_SECRET_KEY);

	private CredentialProvider() {
		// instantiation not allowed
	}

	/**
	 * @see {@link com.ma.cmms.api.client.BasicCredentials}
	 * @return A basic credential object
	 */
	public static Credentials getCredentials() {
		return credentials;
	}

}
