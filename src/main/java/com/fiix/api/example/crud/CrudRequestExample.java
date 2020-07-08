package com.fiix.api.example.crud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fiix.api.example.utils.CredentialProvider;
import com.fiix.api.example.utils.MyProxyCredentials;
import com.fiix.api.example.utils.ResponseLogger;
import com.ma.cmms.api.client.FiixCmmsClient;
import com.ma.cmms.api.client.dto.Asset;
import com.ma.cmms.api.client.dto.AssetCategory;
import com.ma.cmms.api.client.dto.Country;
import com.ma.cmms.api.crud.AddRequest;
import com.ma.cmms.api.crud.AddResponse;
import com.ma.cmms.api.crud.ChangeRequest;
import com.ma.cmms.api.crud.ChangeResponse;
import com.ma.cmms.api.crud.FindByIdRequest;
import com.ma.cmms.api.crud.FindByIdResponse;
import com.ma.cmms.api.crud.FindFilter;
import com.ma.cmms.api.crud.FindRequest;
import com.ma.cmms.api.crud.FindResponse;
import com.ma.cmms.api.crud.RemoveRequest;
import com.ma.cmms.api.crud.RemoveResponse;

/**
 * A class demonstrating the process of creating, updating, finding, and
 * removing Assets using the <b>FiixCmmsClient</b>
 */
public class CrudRequestExample {

	private static final String UPDATED_BY_A_WEB_SERVICE = "Hi, I am an asset with category 'Locations And Facilities',"
			+ " I was updated by a web service";
	private static final String CREATED_BY_A_WEB_SERVICE = "Hi, I am an asset with category 'Locations And Facilities',"
			+ " I was created by a web service";

	private static final String CA = "CA";
	private static final Long FALSE = 0L;
	private static final Long TRUE = 1L;
	private static final Integer LOCATION_SYSCODE = 1;

	private FiixCmmsClient fiixCmmsClient;

	/**
	 * Default constructor used by the examples in this repo.
	 */
	public CrudRequestExample() {
		this.fiixCmmsClient = new FiixCmmsClient(CredentialProvider.getCredentials(),
				CredentialProvider.TENANT_API_ENDPOINT, new MyProxyCredentials());
	}

	/**
	 * Constructor used for injecting a <b>FiixCmmsClient</b> object. <br><br>
	 * (Used primarily for testing).
	 * 
	 * @param client
	 *            The FiixCmmsClient object to be injected.
	 */
	public CrudRequestExample(FiixCmmsClient client) {
		this.fiixCmmsClient = client;
	}

	/**
	 * An example to demonstrate the creation of an object in CMMS <br>
	 * The example focuses on creating an asset categorized as <b>'Locations and
	 * Facilities'</b>
	 * 
	 * @return created asset object
	 */
	public Asset addAnAssetWithCategoryLocation() {

		// Find the asset category id for locations
		AssetCategory category = findAssetCategoryBySysCode(LOCATION_SYSCODE);

		// Build an asset with field details
		Asset asset = new Asset();
		asset.setIntCategoryID(category.getId());
		asset.setStrName("Toronto");
		asset.setStrCode("WS-32");
		asset.setBolIsOnline(FALSE); // set offline
		asset.setStrDescription(CREATED_BY_A_WEB_SERVICE);

		// Build an add request
		AddRequest<Asset> request = fiixCmmsClient.prepareAdd(Asset.class);

		// List columns you would like to get back in the response object
		request.setFields("id, intCategoryID, strName, strCode, bolIsOnline, strDescription");
		// Set the asset object to be created
		request.setObject(asset);

		// Finally, create
		AddResponse<Asset> response = fiixCmmsClient.add(request);
		ResponseLogger.logErrors(response);

		Asset responseAsset = response.getObject();
		System.out.println("\nCreated an asset with category location");
		ResponseLogger.logAsset(responseAsset);

		return responseAsset;
	}

	/**
	 * An example to demonstrate a change request <br>
	 * The example focuses on changing certain fields on an asset given its id field
	 * 
	 * @param assetId
	 * @return Asset
	 */
	public Asset change(Long assetId) {
		// Find a country, to obtain its id
		Country country = findCountryByShortName(CA);

		// Create a change request and prepare an asset for change
		ChangeRequest<Asset> request = fiixCmmsClient.prepareChange(Asset.class);
		Asset asset = new Asset();
		asset.setId(assetId);
		asset.setStrName("Greater Toronto Area");
		asset.setBolIsOnline(TRUE);
		asset.setStrDescription(UPDATED_BY_A_WEB_SERVICE);
		asset.setIntCountryID(country.getId());

		// List columns you would like to get back in the response object
		request.setFields("id, intCategoryID, strName, strCode, bolIsOnline, strDescription, intCountryID");
		// List columns you would like to change
		request.setChangeFields("strName, bolIsOnline, strDescription, intCountryID");
		request.setObject(asset);

		ChangeResponse<Asset> response = fiixCmmsClient.change(request);
		ResponseLogger.logErrors(response);

		Asset responseAsset = response.getObject();
		System.out.println("\nUpdated previously created asset's name, descritpion, status and country id");
		ResponseLogger.logAsset(responseAsset);

		return response.getObject();
	}

	/**
	 * A example to demonstrate, fetching a list of specific type of assets <br>
	 * The example focuses on fetching all the assets of type <b>'Locations and
	 * Facilities'</b>
	 * 
	 * @return A list of objects that represent locations ordered by name
	 */
	public List<Asset> findAllLocations() {

		AssetCategory category = findAssetCategoryBySysCode(LOCATION_SYSCODE);

		List<FindFilter> filters = new ArrayList<>();
		FindFilter filter = new FindFilter();
		filter.setQl("intCategoryID=? AND bolIsSite=?");
		List<Object> params = new ArrayList<>();
		params.add(category.getId());
		params.add(FALSE);
		filter.setParameters(params);
		filters.add(filter);

		FindRequest<Asset> request = fiixCmmsClient.prepareFind(Asset.class);
		request.setFields("id, strName, strCode, bolIsOnline, bolIsSite, intCategoryID");
		request.setFilters(filters);
		request.setOrderBy("strName");

		FindResponse<Asset> response = fiixCmmsClient.find(request);
		ResponseLogger.logErrors(response);

		List<Asset> assets = Collections.emptyList();
		System.out.println(String.format("\nTotal %s assets found with category '%s'", response.getTotalObjects(),
				category.getStrName()));
		assets = response.getObjects();
		ResponseLogger.logAssets(assets);

		return assets;
	}

	/**
	 * An example to fetch Asset Category by SysCode
	 * 
	 * @param sysCode
	 *            The integer SysCode value for an AssetCategory, find out more
	 *            SysCode value options <a href=
	 *            "https://fiixlabs.github.io/api-documentation/#/ApiDoc#AssetCategory">here</a>
	 * @return Asset Category object corresponding to the provided SysCode
	 */
	public AssetCategory findAssetCategoryBySysCode(Integer sysCode) {
		System.out.println(String.format("\nFinding Asset category by sysCode = %s ...", sysCode));
		List<FindFilter> filters = new ArrayList<>();
		FindFilter filter = new FindFilter();
		filter.setQl("intSysCode=?");
		List<Object> params = new ArrayList<>();
		params.add(sysCode);
		filter.setParameters(params);
		filters.add(filter);

		FindRequest<AssetCategory> request = fiixCmmsClient.prepareFind(AssetCategory.class);
		request.setFields("id, strName, intSysCode");
		request.setFilters(filters);
		FindResponse<AssetCategory> response = fiixCmmsClient.find(request);
		ResponseLogger.logErrors(response);
		List<AssetCategory> categories = response.getObjects();

		ResponseLogger.logCategories(categories);
		// Get First
		AssetCategory category = categories.get(0);
		return category;
	}

	/**
	 * An example that demonstrates the use of findById and display value <a href=
	 * "https://fiixlabs.github.io/api-documentation/#/ApiDoc#CFDVSection">Learn
	 * More about display value</a>
	 * 
	 * @param locationId
	 * @return An Asset found by id, that has selected columns as defined in the
	 *         request
	 */
	public Asset findById(Long locationId) {

		// Find an asset using the id
		FindByIdRequest<Asset> findByIdRequest = fiixCmmsClient.prepareFindById(Asset.class);

		// Let's add a display value field to the list of selected columns
		findByIdRequest.setFields("id, strName, strCode, strDescription, dv_intCountryID");
		findByIdRequest.setId(locationId);

		System.out.println("\nFinding location by id: " + locationId + "; Note: The usage of country display value.");

		FindByIdResponse<Asset> response = fiixCmmsClient.findById(findByIdRequest);
		Asset locationFecthedById = response.getObject();

		System.out.println(String.format("Id: %s, Code: %s, Name: %s, Description: %s, Country: %s",
				locationFecthedById.getId(), locationFecthedById.getStrCode(), locationFecthedById.getStrName(),
				locationFecthedById.getStrName(), locationFecthedById.getExtraFields().get("dv_intCountryID")));

		return locationFecthedById;
	}

	/**
	 * An example to get a Country by short name
	 * 
	 * @param shortName
	 *            The short name is a 2 letter code for a country, e.g. CA for
	 *            Canada
	 * @return The Country object, corresponding to the provided short name
	 */
	public Country findCountryByShortName(String shortName) {
		System.out.println(String.format("\nFinding Country by short code = %s ...", shortName));
		List<FindFilter> filters = new ArrayList<>();
		FindFilter filter = new FindFilter();
		filter.setQl("strShort2=?");
		List<Object> params = new ArrayList<>();
		params.add(shortName);
		filter.setParameters(params);
		filters.add(filter);

		FindRequest<Country> request = fiixCmmsClient.prepareFind(Country.class);
		request.setFields("id, strName, strShort2");
		request.setFilters(filters);
		FindResponse<Country> response = fiixCmmsClient.find(request);
		ResponseLogger.logErrors(response);
		List<Country> countries = response.getObjects();
		ResponseLogger.logCountries(countries);
		// Get First
		Country country = countries.get(0);
		return country;
	}

	/**
	 * An example to demonstrate how to remove an asset using id
	 * 
	 * @param assetId
	 */
	public void remove(Long assetId) {
		System.out.println("\nRemoving asset ... ");
		RemoveRequest<Asset> request = fiixCmmsClient.prepareRemove(Asset.class);
		request.setId(assetId);
		RemoveResponse<Asset> response = fiixCmmsClient.remove(request);
		ResponseLogger.logErrors(response);
		System.out.println(String.format("Removed %s asset, with id: %s", response.getCount(), assetId));
	}

}
