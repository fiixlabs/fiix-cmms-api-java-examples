package com.fiix.api.example.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiix.api.example.utils.CredentialProvider;
import com.fiix.api.example.utils.ResponseLogger;
import com.ma.cmms.api.Error;
import com.ma.cmms.api.client.FiixCmmsClient;
import com.ma.cmms.api.client.dto.Asset;
import com.ma.cmms.api.client.dto.AssetCategory;
import com.ma.cmms.api.client.dto.AssetResolved;
import com.ma.cmms.api.crud.AddRequest;
import com.ma.cmms.api.crud.AddResponse;
import com.ma.cmms.api.rpc.PagedResponse;
import com.ma.cmms.api.rpc.RpcRequest;
import com.ma.cmms.api.rpc.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PagedRpcExample
{

	public static final Logger LOGGER = LoggerFactory.getLogger(PagedRpcExample.class);
	private static final Integer LOCATION_SYSCODE = 1;
	private static final String CREATED_BY_A_WEB_SERVICE = "Hi, I am an asset with category 'Locations And Facilities',"
		+ " I was created by a web service";

	private static FiixCmmsClient fiixCmmsClient;

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Default constructor used by the examples in this repo.
	 */

	public PagedRpcExample() {
		fiixCmmsClient = new FiixCmmsClient(CredentialProvider.getCredentials(),
			CredentialProvider.TENANT_API_ENDPOINT);
	}

	public PagedRpcExample(FiixCmmsClient client) {
		this.fiixCmmsClient = client;
	}

	/**
	 * An example to demonstrate the creation of an object in CMMS <br>
	 * The example focuses on creating an asset categorized as <b>'Locations and
	 * Facilities'</b>
	 *
	 * @return created asset object
	 */


	public RpcResponse getAssetResolvedData(Long descendentId)
	{
		RpcRequest assetResolvedRequest = new RpcRequest();
		assetResolvedRequest.setName("AssetResolved");
		assetResolvedRequest.setAction("getAssetParents");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("descendantID", descendentId);
		parameters.put("pageNumber", 1L);
		parameters.put("pageSize", 10L);
		assetResolvedRequest.setParameters(parameters);
		PagedResponse<AssetResolved> pagedResponse = (PagedResponse<AssetResolved>) fiixCmmsClient.rpc(assetResolvedRequest);
		Error error = pagedResponse.getError();
		if (error == null)
		{
			Long assetResolvedId = pagedResponse.getDataList().get(0).getId();
			LOGGER.info("Successfully retrieved the data for asset resolved with ID: "+assetResolvedId);
			LOGGER.info("Descendent ID: "+pagedResponse.getDataList().get(0).getIntDescendantID());
			LOGGER.info("Ancestor ID: "+pagedResponse.getDataList().get(0).getIntAncestorID());

		}
		else
		{
			LOGGER.error("Error occurred during the invocation of the Asset Resolved RPC");
			ResponseLogger.logErrors(pagedResponse);
		}
		return ((error == null) ? pagedResponse : null);
	}

}
