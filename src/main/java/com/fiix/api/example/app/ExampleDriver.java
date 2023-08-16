package com.fiix.api.example.app;

import com.fiix.api.example.crud.CrudRequestExample;
import com.fiix.api.example.rpc.CustomFieldRpcExample;
import com.fiix.api.example.rpc.PagedRpcExample;
import com.fiix.api.example.rpc.PingRpcExample;
import com.fiix.api.example.utils.MyProxyCredentials;
import com.fiix.api.example.workorder.WorkOrderExample;
import com.ma.cmms.api.client.dto.Asset;
import com.ma.cmms.api.crud.CustomFieldMetaData;
import com.ma.cmms.api.rpc.ParameterizedRpcResponse;
import com.ma.cmms.api.rpc.RpcResponse;
import com.ma.cmms.api.rpc.dto.CustomFieldsMetaData;
import com.ma.cmms.api.rpc.dto.CustomTableMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class is the starting point for executing all the provided examples <br>
 * once the {@link com.fiix.api.example.utils.CredentialProvider} class<br> is setup
 * with valid <a href=
 * "https://fiixlabs.github.io/api-documentation/guide.html#getting_started">api
 * keys and endpoint</a>
 */
public class ExampleDriver {

	private static RpcResponse workOrderCustomFieldMetadataResponse;
	public static final Logger LOGGER = LoggerFactory.getLogger(ExampleDriver.class);
	private static Long testDescendentIDforAssetResolvedRPC = 12L;	//This should be a valid Asset ID

	public static void main(String[] args) {
		if (isServerReachable()) {
			executeCrudExamples(false);
//			executeCrudExamples(true); //Test CRUD with proxy credentials. Enable this and disable the previous call; MyProxyCredentials:host=localhost:port=8888
			executeWorkOrderExample();
			executeCustomFieldApi();
			executePagedResponseRpc(testDescendentIDforAssetResolvedRPC);
		} else {
			System.out.println("Server is not reachable!");
		}
	}

	private static boolean isServerReachable() {
		// Ping to see if server is up!
		LOGGER.info("****************************************************************");
		LOGGER.info("Pinging the server to check if it is up and running");
		return new PingRpcExample().pingServer();
	}

	private static void executeCrudExamples(Boolean executeWithProxy) {
		CrudRequestExample crudRequestExample;
		if(executeWithProxy)
		{
			crudRequestExample = new CrudRequestExample(new MyProxyCredentials());
			LOGGER.info("****************************************************************");
			LOGGER.info("Starting the CRUD Tests with Proxy Credentials");
		}
		else {
			crudRequestExample = new CrudRequestExample();
			LOGGER.info("****************************************************************");
			LOGGER.info("Starting the CRUD Tests");
		}

		// Create
		LOGGER.info("Creating an Asset");
		Asset asset = crudRequestExample.addAnAssetWithCategoryLocation();

		Long assetId = asset.getId();

		// Update
		LOGGER.info("Updating the Asset with Asset ID: "+assetId);
		crudRequestExample.change(assetId);
		// Read by Id
		LOGGER.info("Finding the Asset with ID: "+assetId);
		crudRequestExample.findById(assetId);
		// Read All
		LOGGER.info("Finding all Locations");
		crudRequestExample.findAllLocations();
		// Remove
		LOGGER.info("Removing the Asset with ID: "+assetId);
		crudRequestExample.remove(assetId);
		// Read All
		LOGGER.info("Finding all Locations");
		crudRequestExample.findAllLocations();
	}


	private static void executeWorkOrderExample() {
		WorkOrderExample workOrderExample = new WorkOrderExample();

		LOGGER.info("****************************************************************");
		LOGGER.info("Starting the Work Order Tests");

		// Creates assets we can use to make work orders
		LOGGER.info("Creating Assets to test work orders");
		workOrderExample.addExampleAssets();
		// List sites
		LOGGER.info("Listing sites to test work orders");
		workOrderExample.listSites();
		// List statuses
		LOGGER.info("Listing Work Order Statuses to test work orders");
		workOrderExample.listWorkOrderStatuses();
		// Create a basic work order
		LOGGER.info("Create work orders without assets");
		workOrderExample.createWorkOrderWithoutAssets();
		// Associate an asset with that work order
		LOGGER.info("Update the work order with assets");
		workOrderExample.updateWorkOrderWithAssets();
		// Associate a task with that work order
		LOGGER.info("Add tasks to the work order");
		workOrderExample.updateWorkOrderWithTasks();
		// Remove everything created by this example
		LOGGER.info("Removing the artefacts created for testing");
		workOrderExample.cleanUp();
	}

	private static void executeCustomFieldApi(){

		LOGGER.info("****************************************************************");
		LOGGER.info("Starting the tests for custom fields via API");

		ParameterizedRpcResponse<CustomFieldsMetaData> workOrderCustomFieldMetadataResponse
			= CustomFieldRpcExample.getWorkOrderCustomFieldsMetaData();

		if(workOrderCustomFieldMetadataResponse != null)
		{
			CustomFieldsMetaData customFieldsMetaData = workOrderCustomFieldMetadataResponse.getDataObject();
			CustomFieldRpcExample.logCustomFieldsMetaData(customFieldsMetaData);

			if (customFieldsMetaData.getCustomTableFields() != null)
			{
				customFieldsMetaData.getCustomTableFields().stream().forEach(customFieldMetaData -> {
					CustomFieldRpcExample.logCustomFieldMetaData(customFieldMetaData);
					if (customFieldMetaData.getLinkTableName() != null
						&& customFieldMetaData.getLinkTableData() != null
						&& customFieldMetaData.getLinkTableData().size() > 0)
					{
						ParameterizedRpcResponse<CustomTableMetaData> customTableMetadataResponse =
							CustomFieldRpcExample.getCustomTableMetadata(customFieldMetaData.getLinkTableName());
						if(customTableMetadataResponse != null && customTableMetadataResponse.getDataObject() != null)
						{
							CustomTableMetaData customTableMetaData = customTableMetadataResponse.getDataObject();
							CustomFieldRpcExample.logCustomTableMetaData(customTableMetaData);
						}
					}
				});
			}
		}

	}

	private static void executePagedResponseRpc(Long testDescendentIDforAssetResolvedRPC) {
		LOGGER.info("****************************************************************");
		LOGGER.info("Starting the tests for Paged RPC Response - Asset Resolved RPC");
		PagedRpcExample pagedRpcExample = new PagedRpcExample();
		RpcResponse assetResolvedPagedResponse = pagedRpcExample.getAssetResolvedData(testDescendentIDforAssetResolvedRPC);
	}
}
