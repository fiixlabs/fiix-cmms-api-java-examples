package com.fiix.api.example.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fiix.api.example.utils.CredentialProvider;
import com.fiix.api.example.utils.ResponseLogger;
import com.ma.cmms.api.Error;
import com.ma.cmms.api.client.FiixCmmsClient;
import com.ma.cmms.api.rpc.RpcRequest;
import com.ma.cmms.api.rpc.RpcResponse;

/**
 * An example demostrating the process of making an RPC request using the
 * <b>FiixCmmsClient</b>.
 *
 */
public class PingRpcExample {

	public static final Logger LOGGER = LoggerFactory.getLogger(PingRpcExample.class);

	private FiixCmmsClient fiixCmmsClient;

	/**
	 * Default constructor used by the examples in this repo.
	 */
	public PingRpcExample() {
		this.fiixCmmsClient = new FiixCmmsClient(CredentialProvider.getCredentials(),
				CredentialProvider.TENANT_API_ENDPOINT);
	}

	/**
	 * Constructor used for injecting a <b>FiixCmmsClient</b> object. <br><br>
	 * (Used primarily for testing).
	 * 
	 * @param client
	 *            The FiixCmmsClient object to be injected.
	 */
	public PingRpcExample(FiixCmmsClient client) {
		this.fiixCmmsClient = client;
	}

	/**
	 * An example to demonstrate how to ping our server, by calling <b>'Ping'</b>
	 * RPC. <br>
	 * Learn about other RPCs <a href=
	 * "https://fiixlabs.github.io/api-documentation/#/ApiDoc#RPCObjects">here</a>
	 * 
	 */
	public boolean pingServer() {
		RpcRequest rpcRequest = new RpcRequest();
		rpcRequest.setName("Ping");
		RpcResponse rpcResponse = fiixCmmsClient.rpc(rpcRequest);
		Error error = rpcResponse.getError();
		if (error == null) {
			LOGGER.error("You have successfully made your first call to the API. Server is up and running.");
		} else {
			ResponseLogger.logErrors(rpcResponse);
		}
		return error == null;
	}

}
