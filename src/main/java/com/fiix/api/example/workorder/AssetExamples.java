package com.fiix.api.example.workorder;

import java.util.Optional;

import com.fiix.api.example.utils.ResponseLogger;
import com.ma.cmms.api.client.FiixCmmsClient;
import com.ma.cmms.api.client.dto.Asset;
import com.ma.cmms.api.client.dto.AssetCategory;
import com.ma.cmms.api.crud.AddRequest;
import com.ma.cmms.api.crud.AddResponse;
import com.ma.cmms.api.crud.FindRequest;
import com.ma.cmms.api.crud.FindResponse;
import com.ma.cmms.api.crud.RemoveRequest;

/**
 * A set of Example objects to be used with Fiix's Java API client. These <br>
 * include a custom Asset Category, two sample Equipment Assets, and one <br>
 * Location Asset to house the others. Also included are AddRequests for each <br>
 * Asset respectively.
 */
public class AssetExamples {

	public static final String ASSET_CATEGORY_NAME = "Locations And Facilities";

	private FiixCmmsClient fiixCmmsClient;

	public AssetCategory locationsAndFacilities;
	public Asset location = new Asset();
	public Asset exampleOne = new Asset();
	public Asset exampleTwo = new Asset();

	public AddRequest<AssetCategory> addFactoryCategory;
	public AddRequest<Asset> addLocation;
	public AddRequest<Asset> addReqOne;
	public AddRequest<Asset> addReqTwo;

	public AssetExamples(FiixCmmsClient fiixCmmsClient) {
		this.fiixCmmsClient = fiixCmmsClient;
	}

	public void init() {
		this.buildAssets();
		this.buildRequests();
		this.makeRequests();
	}

	public void cleanUp() {
		RemoveRequest<Asset> removeExampleOne = new RemoveRequest<>();
		removeExampleOne.setClassName("Asset");
		removeExampleOne.setId(this.exampleOne.getId());

		RemoveRequest<Asset> removeExampleTwo = new RemoveRequest<>();
		removeExampleTwo.setClassName("Asset");
		removeExampleTwo.setId(this.exampleTwo.getId());

		RemoveRequest<Asset> removeLocation = new RemoveRequest<>();
		removeLocation.setClassName("Asset");
		removeLocation.setId(this.location.getId());

		this.fiixCmmsClient.remove(removeExampleOne);
		this.fiixCmmsClient.remove(removeExampleTwo);
		this.fiixCmmsClient.remove(removeLocation);
	}

	public void buildAssets() {

		this.location.setStrName("Flux Capacitor Factory");
		this.location.setStrDescription("Founded 2015");

		this.exampleOne.setStrName("Delorean");
		this.exampleOne.setStrDescription("A Time Machine built with style");

		this.exampleTwo.setStrName("Einstein the Dog");
		this.exampleTwo.setStrDescription("A faithful companian");
	}

	public void buildRequests() {
		this.addReqOne = new AddRequest<Asset>();
		this.addReqOne.setClassName("Asset");
		this.addReqOne.setFields("id, strName, strDescription");
		this.addReqOne.setObject(this.exampleOne);

		this.addReqTwo = new AddRequest<Asset>();
		this.addReqTwo.setClassName("Asset");
		this.addReqTwo.setFields("id, strName, strDescription");
		this.addReqTwo.setObject(this.exampleTwo);
	}

	public void makeRequests() {
		AddResponse<Asset> responseOne = this.fiixCmmsClient.add(this.addReqOne);
		AddResponse<Asset> responseTwo = this.fiixCmmsClient.add(this.addReqTwo);

		ResponseLogger.log(responseOne, "responseOne");
		ResponseLogger.log(responseTwo, "responseTwo");

		this.exampleOne = responseOne.getObject();
		this.exampleTwo = responseTwo.getObject();

		this.locationsAndFacilities = this.getExistingAssetCategory(ASSET_CATEGORY_NAME);

		this.buildLocation(this.locationsAndFacilities);

		AddResponse<Asset> responseLocation = this.fiixCmmsClient.add(this.addLocation);
		ResponseLogger.log(responseLocation, "responseLocation");
		this.location = responseLocation.getObject();
	}

	private void buildLocation(AssetCategory category) {
		this.location.setIntCategoryID(category.getId());

		this.addLocation = new AddRequest<Asset>();
		this.addLocation.setClassName("Asset");
		this.addLocation.setFields("id, intSiteID, intCategoryID, strName, strDescription");
		this.addLocation.setObject(this.location);
	}

	private AssetCategory getExistingAssetCategory(String name) {
		FindRequest<AssetCategory> findReq = new FindRequest<AssetCategory>();
		findReq.setClassName("AssetCategory");
		findReq.setFields("id, strName, intSysCode");

		FindResponse<AssetCategory> findRes = this.fiixCmmsClient.find(findReq);
		ResponseLogger.log(findRes, "getExistingAssetCategory");
		Optional<AssetCategory> category = findRes.getObjects().stream().filter(x -> x.getStrName().equals(name))
				.findFirst();
		if (category.isPresent()) {
			return category.get();
		} else {
			return null;
		}
	}
}
