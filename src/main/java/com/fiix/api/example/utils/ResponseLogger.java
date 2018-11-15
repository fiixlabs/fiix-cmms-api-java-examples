package com.fiix.api.example.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ma.cmms.api.Error;
import com.ma.cmms.api.Response;
import com.ma.cmms.api.client.dto.Asset;
import com.ma.cmms.api.client.dto.AssetCategory;
import com.ma.cmms.api.client.dto.Country;
import com.ma.cmms.api.client.dto.WorkOrderStatus;
import com.ma.cmms.api.crud.AddResponse;
import com.ma.cmms.api.crud.ChangeResponse;
import com.ma.cmms.api.crud.FindResponse;
import com.ma.cmms.api.crud.RemoveResponse;
import com.ma.cmms.api.rpc.RpcResponse;

/**
 * A Custom Logging Component created for this example project.
 */
public class ResponseLogger {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseLogger.class);

	/**
	 * A Logger for the returned array from a FindRequest. Valid for types
	 * WorkOrderStatus and Asset, these constraints can be changed in the forEach
	 * section of the method.
	 * 
	 * @param response
	 *            the FindResponse returned by a FindRequest
	 * 
	 * @param methodName
	 *            the Name of the method that is calling the log. Used to display
	 *            "methodName returned x"
	 */
	public static void log(FindResponse<?> response, String methodName) {
		if (response.getError() != null) {
			LOGGER.info(response.getError().getMessage());
		} else {
			LOGGER.info(String.format("%s returned %d objects", methodName, response.getTotalObjects()));
			if (response.getObjects() != null) {
				response.getObjects().forEach(x -> {
					if (x instanceof WorkOrderStatus) {
						LOGGER.info(String.format("\tname: %s", ((WorkOrderStatus) x).getStrName()));
					} else if (x instanceof Asset) {
						LOGGER.info(String.format("\tname: %s", ((Asset) x).getStrName()));
					} else {
						LOGGER.info(String.format("\tname: %s", ((AssetCategory) x).getStrName()));
						LOGGER.info(String.format("\tsysCode: %s", ((AssetCategory) x).getIntSysCode()));

					}
					LOGGER.info(String.format("\tid: %s", x.getId()));
				});
			}
		}
	}

	/**
	 * A logger for the AddResponse object
	 * 
	 * @param response
	 *            the AddResponse returned by an AddRequest
	 * @param methodName
	 *            the name of the method calling the logger
	 */
	public static void log(AddResponse<?> response, String methodName) {
		if (response.getError() != null) {
			LOGGER.info(response.getError().getMessage());
		} else {
			LOGGER.info(String.format("%s returned %d objects", methodName, response.getCount()));
			if (response.getObject() != null) {
				LOGGER.info(String.format("\tid: %s", response.getObject().getId()));
			}
		}
	}

	/**
	 * A logger for the ChangeResponse object
	 * 
	 * @param response
	 *            the ChangeResponse returned by an ChangeRequest
	 * @param methodName
	 *            the name of the method calling the logger
	 */
	public static void log(ChangeResponse<?> response, String methodName) {
		if (response.getError() != null) {
			LOGGER.info(response.getError().getMessage());
		} else {
			LOGGER.info(String.format("%s returned %d objects", methodName, response.getCount()));
			LOGGER.info(String.format("\tid: %s", response.getObject().getId()));
		}
	}

	/**
	 * A logger for the RemoveResponse object
	 * 
	 * @param response
	 *            the RemoveResponse returned by an RemoveRequest
	 * @param methodName
	 *            the name of the method calling the logger
	 */
	public static void log(RemoveResponse<?> response, String methodName) {
		if (response.getError() != null) {
			LOGGER.info(response.getError().getMessage());
		} else {
			LOGGER.info(String.format("%s returned %d objects", methodName, response.getCount()));
		}
	}

	/**
	 * A logger for the RpcResponse object
	 * 
	 * @param response
	 *            the RpcResponse returned by an RpcRequest
	 * @param methodName
	 *            the name of the method calling the logger
	 */
	public static void log(RpcResponse response, String methodName) {
		if (response.getError() != null) {
			LOGGER.info(response.getError().getMessage());
		} else {
			LOGGER.info(String.format("%s returned s", methodName));
			LOGGER.info(String.format("\tObject: %s", response.getObject()));
		}

	}

	/**
	 * A helper function to log errors found in a response
	 * 
	 * @param response
	 *            The response object returned by the api, may contains errors
	 */
	public static void logErrors(Response response) {
		Error error = response.getError();
		if (error != null) {
			LOGGER.error(error.getStackTrace());
			LOGGER.error(error.getMessage());
		}
	}

	/**
	 * A Logger for displaying information provided by an Asset
	 * 
	 * @param asset
	 *            The asset to be logged
	 */
	public static void logAsset(Asset asset) {
		String status = asset.getBolIsOnline() == 1 ? "Online" : "Offline";
		LOGGER.info(
				String.format("Id: %s, Status: %s, Code: %s, Name: %s, CategoryId: %s, Description: %s, Country ID: %s",
						asset.getId(), status, asset.getStrCode(), asset.getStrName(), asset.getIntCategoryID(),
						asset.getStrDescription(), asset.getIntCountryID()));
	}

	/**
	 * A Logger for displaying information provided by each Asset within a List of
	 * Assets
	 * 
	 * @param assets
	 *            The list of assets to be logged
	 */
	public static void logAssets(List<Asset> assets) {
		LOGGER.info("\nAssets (Locations):");
		for (Asset asset : assets) {
			String status = asset.getBolIsOnline() == 1 ? "Online" : "Offline";
			LOGGER.info(String.format("Id: %s, Status: %s, Code: %s, Name: %s", asset.getId(), status,
					asset.getStrCode(), asset.getStrName()));
		}
	}

	/**
	 * A Logger for displaying information provided by each AssetCategory within a
	 * List of AssetCategories
	 * 
	 * @param categories
	 *            The list of AssetCategories to be logged </br>
	 *            </br>
	 * @see {@link com.ma.cmms.api.client.dto.AssetCategory}
	 */
	public static void logCategories(List<AssetCategory> categories) {
		LOGGER.info("Asset category found:");
		for (AssetCategory category : categories) {
			LOGGER.info(String.format("Id: %s, Name: %s, SysCode: %s", category.getId(), category.getStrName(),
					category.getIntSysCode()));
		}
	}

	/**
	 * A Logger for displaying information provided by each Country within a List of
	 * Countries
	 * 
	 * @param countries
	 *            The list of Countries to be logged </br>
	 *            </br>
	 * @see {@link com.ma.cmms.api.client.dto.Country}
	 */
	public static void logCountries(List<Country> countries) {
		LOGGER.info("Country found:");
		for (Country country : countries) {
			LOGGER.info(String.format("Id: %s, Name: %s, ShortName: %s", country.getId(), country.getStrName(),
					country.getStrShort2()));
		}
	}
}
