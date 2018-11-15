package com.fiix.api.example.workorder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fiix.api.example.utils.CredentialProvider;
import com.fiix.api.example.utils.ResponseLogger;
import com.ma.cmms.api.client.FiixCmmsClient;
import com.ma.cmms.api.client.dto.Asset;
import com.ma.cmms.api.client.dto.WorkOrder;
import com.ma.cmms.api.client.dto.WorkOrderAsset;
import com.ma.cmms.api.client.dto.WorkOrderStatus;
import com.ma.cmms.api.client.dto.WorkOrderTask;
import com.ma.cmms.api.crud.AddRequest;
import com.ma.cmms.api.crud.AddResponse;
import com.ma.cmms.api.crud.FindFilter;
import com.ma.cmms.api.crud.FindRequest;
import com.ma.cmms.api.crud.FindResponse;
import com.ma.cmms.api.crud.RemoveRequest;

/**
 * A set of methods to show the process of creating WorkOrders, and associating <br>
 * them with Assets, Locations, and Tasks, using the Fiix API Java Client
 */
public class WorkOrderExample {

	protected FiixCmmsClient fiixCmmsClient;

	public WorkOrderExample() {
		this.fiixCmmsClient = new FiixCmmsClient(CredentialProvider.getCredentials(),
				CredentialProvider.TENANT_API_ENDPOINT);
	}

	public WorkOrderExample(FiixCmmsClient fiixCmmsClient) {
		this.fiixCmmsClient = fiixCmmsClient;
	}

	public AssetExamples assets;

	public WorkOrder workOrder;
	public WorkOrderAsset workOrderAsset;
	public WorkOrderTask workOrderTask;

	public Long draftStatusID;

	public void addExampleAssets() {
		assets = new AssetExamples(fiixCmmsClient);
		assets.init();
	}

	public void listSites() {
		FindFilter filter = new FindFilter();
		filter.setQl("intKind = ?");
		filter.setParameters(Arrays.asList((Object) 1));
		List<FindFilter> filters = Arrays.asList((filter));

		FindRequest<Asset> findReq = new FindRequest<Asset>();
		findReq.setClassName("Asset");
		findReq.setFilters(filters);
		findReq.setFields("id, strName");

		FindResponse<Asset> findRes = fiixCmmsClient.find(findReq);
		ResponseLogger.log(findRes, "listSites");
	}

	public void listWorkOrderStatuses() {
		FindRequest<WorkOrderStatus> findReq = new FindRequest<>();
		findReq.setClassName("WorkOrderStatus");
		findReq.setFields("id,strName,intSysCode");
		findReq.setOrderBy("intSysCode");

		FindResponse<WorkOrderStatus> findRes = fiixCmmsClient.find(findReq);
		Optional<WorkOrderStatus> draftOptional = findRes.getObjects().stream()
				.filter(x -> x.getStrName().equals("Draft")).findFirst();
		if (draftOptional.isPresent()) {
			this.draftStatusID = draftOptional.get().getId();
		}
		ResponseLogger.log(findRes, "listWorkOrderStatuses");
	}

	public void createWorkOrderWithoutAssets() {
		workOrder = new WorkOrder();
		workOrder.setIntSiteID(assets.location.getId());
		workOrder.setIntWorkOrderStatusID(draftStatusID);
		workOrder.setStrDescription("Find 1.21 Gigawatts");

		System.out.println("\n\nWorkOrderSiteID:\t" + workOrder.getIntSiteID());

		AddRequest<WorkOrder> workOrderAddRequest = new AddRequest<>();
		workOrderAddRequest.setClassName("WorkOrder");
		workOrderAddRequest.setObject(workOrder);
		workOrderAddRequest.setFields("id, intSiteID, intWorkOrderStatusID, strDescription");

		AddResponse<WorkOrder> workOrderAddResponse = fiixCmmsClient.add(workOrderAddRequest);

		// check for errors and log the resulting ids and site ids
		ResponseLogger.log(workOrderAddResponse, "createWorkOrderWithoutAssets");
		workOrder = workOrderAddResponse.getObject();
	}

	public void updateWorkOrderWithAssets() {
		workOrderAsset = new WorkOrderAsset();
		workOrderAsset.setIntWorkOrderID(workOrder.getId());
		workOrderAsset.setIntAssetID(assets.exampleOne.getId());

		AddRequest<WorkOrderAsset> addAssetReq = new AddRequest<>();
		addAssetReq.setClassName("WorkOrderAsset");
		addAssetReq.setObject(workOrderAsset);
		addAssetReq.setFields("id,intWorkOrderID,intAssetID");

		AddResponse<WorkOrderAsset> addRes = fiixCmmsClient.add(addAssetReq);

		// check for errors and log resulting ids
		ResponseLogger.log(addRes, "updateWorkOrderWithAssets");
		workOrderAsset = addRes.getObject();
	}

	public void updateWorkOrderWithTasks() {
		workOrderTask = new WorkOrderTask();
		workOrderTask.setIntWorkOrderID(workOrder.getId());
		workOrderTask.setIntAssetID(assets.exampleTwo.getId());
		workOrderTask.setIntTaskType(0L);
		workOrderTask.setIntOrder(1L);
		workOrderTask.setStrDescription("GREAT SCOTT IT WORKED");

		AddRequest<WorkOrderTask> addRequest = new AddRequest<>();
		addRequest.setClassName("WorkOrderTask");
		addRequest.setObject(workOrderTask);
		addRequest.setFields("id,intWorkOrderID,intAssetID,intTaskType,intOrder, strDescription");

		AddResponse<WorkOrderTask> addRes = fiixCmmsClient.add(addRequest);
		ResponseLogger.log(addRes, "updateWorkOrderWithTasks");
		workOrderTask = addRes.getObject();
	}

	public void cleanUp() {
		assets.cleanUp();
		RemoveRequest<WorkOrder> removeWorkOrder = new RemoveRequest<>();
		removeWorkOrder.setClassName("WorkOrder");
		removeWorkOrder.setId(workOrder.getId());

		RemoveRequest<WorkOrderAsset> removeWorkOrderAsset = new RemoveRequest<>();
		removeWorkOrderAsset.setClassName("WorkOrderAsset");
		removeWorkOrderAsset.setId(workOrderAsset.getId());

		RemoveRequest<WorkOrderTask> removeWorkOrderTask = new RemoveRequest<>();
		removeWorkOrderTask.setClassName("WorkOrderTask");
		removeWorkOrderTask.setId(workOrderTask.getId());

		fiixCmmsClient.remove(removeWorkOrder);
		fiixCmmsClient.remove(removeWorkOrderAsset);
		fiixCmmsClient.remove(removeWorkOrderTask);
	}
}
