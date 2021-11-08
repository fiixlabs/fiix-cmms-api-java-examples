package com.fiix.api.example.app;

import com.fiix.api.example.crud.CrudRequestExample;
import com.fiix.api.example.rpc.CustomFieldRpcExample;
import com.fiix.api.example.rpc.PingRpcExample;
import com.fiix.api.example.workorder.WorkOrderExample;
import com.ma.cmms.api.client.dto.Asset;
import com.ma.cmms.api.crud.CustomFieldMetaData;
import com.ma.cmms.api.rpc.ParameterizedRpcResponse;
import com.ma.cmms.api.rpc.RpcResponse;
import com.ma.cmms.api.rpc.dto.CustomFieldsMetaData;
import com.ma.cmms.api.rpc.dto.CustomTableMetaData;

/**
 * The class is the starting point for executing all the provided examples <br>
 * once the {@link com.fiix.api.example.utils.CredentialProvider} class<br> is setup
 * with valid <a href=
 * "https://fiixlabs.github.io/api-documentation/guide.html#getting_started">api
 * keys and endpoint</a>
 */
public class ExampleDriver {

	private static RpcResponse workOrderCustomFieldMetadataResponse;

	public static void main(String[] args) {
		if (isServerReachable()) {
//			executeCrudExamples(); //with proxy credentials; MyProxyCredentials:host=localhost:port=8888. Comment this line out if proxy credentials are not to be used.
			executeWorkOrderExample();
			executeCustomFieldApi();
		} else {
			System.out.println("Server is not reachable!");
		}
	}

	private static boolean isServerReachable() {
		// Ping to see if server is up!
		return new PingRpcExample().pingServer();
	}

	private static void executeCrudExamples() {
		CrudRequestExample crudRequestExample = new CrudRequestExample();
		// Create
		Asset asset = crudRequestExample.addAnAssetWithCategoryLocation();

		Long assetId = asset.getId();

		// Update
		crudRequestExample.change(assetId);
		// Read by Id
		crudRequestExample.findById(assetId);
		// Read All
		crudRequestExample.findAllLocations();
		// Remove
		crudRequestExample.remove(assetId);
		// Read All
		crudRequestExample.findAllLocations();
	}

	private static void executeWorkOrderExample() {
		WorkOrderExample workOrderExample = new WorkOrderExample();

		// Creates assets we can use to make work orders
		workOrderExample.addExampleAssets();
		// List sites
		workOrderExample.listSites();
		// List statuses
		workOrderExample.listWorkOrderStatuses();
		// Create a basic work order
		workOrderExample.createWorkOrderWithoutAssets();
		// Associate an asset with that work order
		workOrderExample.updateWorkOrderWithAssets();
		// Associate a task with that work order
		workOrderExample.updateWorkOrderWithTasks();
		// Remove everything created by this example
		workOrderExample.cleanUp();
	}

	private static void executeCustomFieldApi(){

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
}
