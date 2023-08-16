package com.fiix.api.example.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiix.api.example.utils.CredentialProvider;
import com.fiix.api.example.utils.ResponseLogger;
import com.ma.cmms.api.Error;
import com.ma.cmms.api.client.FiixCmmsClient;
import com.ma.cmms.api.crud.CustomFieldMetaData;
import com.ma.cmms.api.rpc.ParameterizedRpcRequest;
import com.ma.cmms.api.rpc.ParameterizedRpcResponse;
import com.ma.cmms.api.rpc.dto.CustomFieldsMetaData;
import com.ma.cmms.api.rpc.dto.CustomTableMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomFieldRpcExample
{

	public static final Logger LOGGER = LoggerFactory.getLogger(PingRpcExample.class);

	private static FiixCmmsClient fiixCmmsClient;

	private static ObjectMapper objectMapper = new ObjectMapper();

	static{
		fiixCmmsClient = new FiixCmmsClient(CredentialProvider.getCredentials(),
			CredentialProvider.TENANT_API_ENDPOINT);
	}
	/**
	 * Default constructor used by the examples in this repo.
	 */
	public CustomFieldRpcExample() {

	}

	public static ParameterizedRpcResponse<CustomTableMetaData> getCustomTableMetadata(String customTableName)
	{
		ParameterizedRpcRequest rpcRequest = new ParameterizedRpcRequest();
		rpcRequest.setName("CustomFields");
		rpcRequest.setAction("getCustomTableData");

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("customTableName", customTableName);

		rpcRequest.setParameters(parameters);
		ParameterizedRpcResponse<CustomTableMetaData> rpcResponse = fiixCmmsClient.rpc(rpcRequest);
		Error error = rpcResponse.getError();
		if (error == null)
		{
			LOGGER.info("You have successfully made your call to the CustomFields.getCustomTableData API.");
		}
		else
		{
			ResponseLogger.logErrors(rpcResponse);
		}
		return ((error == null) ? rpcResponse : null);
	}

	public static ParameterizedRpcResponse<CustomFieldsMetaData> getWorkOrderCustomFieldsMetaData()
	{
		ParameterizedRpcRequest rpcRequest = new ParameterizedRpcRequest();
		rpcRequest.setName("CustomFields");
		rpcRequest.setAction("getWorkOrderCustomFieldsMetaData");

		ParameterizedRpcResponse<CustomFieldsMetaData> rpcResponse = fiixCmmsClient.rpc(rpcRequest);
		Error error = rpcResponse.getError();
		if (error == null)
		{
			LOGGER.error("You have successfully made your call to the CustomFields.getWorkOrderCustomFieldsMetaData API.");
		}
		else
		{
			ResponseLogger.logErrors(rpcResponse);
		}
		return ((error == null) ? rpcResponse : null);
	}

	public static void logCustomFieldsMetaData(CustomFieldsMetaData customFieldsMetaData){
		ResponseLogger.logString(String.format("CustomFieldsMetaData \nTableName: %s\nCategory ID: %d\n\n", customFieldsMetaData.getTableName(), customFieldsMetaData.getCategoryId()));
	}

	//TODO
	public static void logCustomFieldMetaData(CustomFieldMetaData customFieldMetaData)
	{
		ResponseLogger.logString(toString(customFieldMetaData, ""));
	}

	public static void logCustomTableMetaData(CustomTableMetaData customTableMetaData)
	{
		ResponseLogger.logString(toString(customTableMetaData, ""));
	}

	private static String toString(CustomFieldMetaData customFieldMetaData, String prefix){
		return prefix + "CustomFieldMetaData {\n" +
			prefix + "  fieldName='" + customFieldMetaData.getFieldName() + "\'\n" +
			prefix + ", fieldType=" + customFieldMetaData.getFieldType() + "\n" +
			prefix + ", fieldLabel='" + customFieldMetaData.getFieldLabel() + "\'\n" +
			prefix + ", categoryId=" + customFieldMetaData.getCategoryId() + "\n" +
			prefix + ", categoryName='" + customFieldMetaData.getCategoryName() + "\'\n" +
			prefix + ", linkTableId=" + customFieldMetaData.getLinkTableId() + "\n" +
			prefix + ", linkTableName='" + customFieldMetaData.getLinkTableName() + "\'\n" +
			prefix + ", linkTableLabelFieldType=" + customFieldMetaData.getLinkTableLabelFieldType() + "\n" +
			prefix + ", linkTableData=" + customFieldMetaData.getLinkTableData() + "\n" +
			prefix + "}";
	}

	private static String toString(CustomTableMetaData customTableMetaData, String prefix){

		return prefix + "CustomTableMetaData{\n" +
			prefix + "  customTableInformation=" + toString(customTableMetaData.getCustomTableInformation(), prefix + "  ") + "\n" +
			prefix + ", customTableFields=" + toStringCTF(customTableMetaData.getCustomTableFields(), prefix + "  ") + "\n" +
			prefix + ", customTableFieldValues=" + toStringCTFV(customTableMetaData.getCustomTableFieldValues(),  prefix + "  ") + "\n" +
			prefix + '}';
	}

	private static String toStringCTFV(List<CustomTableMetaData.CustomTableRow> customTableFieldValues, String prefix)
	{
		return customTableFieldValues.stream().map(x -> toString(x, prefix)).collect(Collectors.joining("\n"));
	}

	private static String toString(CustomTableMetaData.CustomTableRow customTableRow, String prefix)
	{
		return prefix + "CustomTableRow{\n" +
			prefix + "  id=" + customTableRow.getId() + "\n" +
			prefix + ", customFieldValues=" + customTableRow.getCustomFieldValues() + "\n" +
			prefix + '}';
	}

	private static String toStringCTF(List<CustomFieldMetaData> customTableFields, String prefix)
	{
		return customTableFields.stream().map(x -> toString(x, prefix)).collect(Collectors.joining("\n"));
	}

	private static String toString(CustomTableMetaData.CustomTableInformation customTableInformation, String prefix)
	{
		return prefix + "CustomTableInformation{\n" +
			prefix + "  tableName='" + customTableInformation.getTableName() + "\'\n" +
			prefix + ", tableId=" + customTableInformation.getTableId() + "\n" +
			prefix + ", tableLabel='" + customTableInformation.getTableLabel() + "\'\n" +
			prefix + ", customTableLabelField='" + customTableInformation.getCustomTableLabelField() + "\n" +
			prefix + '}';
	}

}
