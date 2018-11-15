package com.fiix.api.example.app;

import com.fiix.api.example.crud.CrudRequestExample;
import com.fiix.api.example.rpc.PingRpcExample;
import com.fiix.api.example.workorder.WorkOrderExample;
import com.ma.cmms.api.client.dto.Asset;

/**
 * The class is the starting point for executing all the provided examples <br>
 * once the {@link com.fiix.api.example.utils.CredentialProvider} class<br> is setup
 * with valid <a href=
 * "https://fiixlabs.github.io/api-documentation/guide.html#getting_started">api
 * keys and endpoint</a>
 */
public class ExampleDriver {

	public static void main(String[] args) {
		if (isServerReachable()) {
			executeCrudExamples();
			executeWorkOrderExample();
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
}
