# Fiix Cmms Client Java Examples

## Overview

This project contains various examples of how to use the [Fiix-Cmms-Java-Client SDK](https://github.com/fiixlabs/fiix-cmms-client-java). It covers the basics of making CRUD and RPC requests using the SDK, as well as a more in depth overview on creating Work Orders, and associating them with Tasks and Assets.

Documentation for the API can be found [here](https://fiixlabs.github.io/api-documentation).

## Authentication
Edit the [CredentialProvider](https://github.com/macmms/fiix-cmms-api-java-examples/blob/master/src/main/java/com/fiix/api/example/utils/CredentialProvider.java) class, to contain your instance's keys and URL. For more details see the [developer's guide](https://fiixlabs.github.io/api-documentation/guide.html#getting_started)

## Examples
Examples are separated into 3 classes, with added helper classes.

### RpcExample
Describes the process of creating and making an Rpc Request. The request made in this example simply pings the server, and returns `true` or `false` depending on the success of the ping request.

### CRUDExample for Assets without Proxy
These Examples describe the process of:
* Creating (adding) an Asset
* Updating (changing) the fields on an Asset based on its Asset ID
* Finding Multiple Assets with a Find Request
* Finding AssetCategories by SysCode
* Finding Assets by LocationId
* Finding a Country by its shortened name
* Removing an Asset

### CRUDExample for Assets with Proxy
These Examples describe the process of:
* Creating (adding) an Asset
* Updating (changing) the fields on an Asset based on its Asset ID
* Finding Multiple Assets with a Find Request
* Finding AssetCategories by SysCode
* Finding Assets by LocationId
* Finding a Country by its shortened name
* Removing an Asset

### WorkOrderExample:
Describes the process of creating Work Orders, associating created Work Orders with Assets or Tasks, setting Work Order Status, and finally, removing Work Orders.

This example uses the class AssetExamples, to create Assets for use with Work Orders. The created Assets include a Location, and two equipment assets.

## Built Using


[Maven](https://maven.apache.org/)

## Running the Application

1. Clone this repo into your project directory
2. Run `mvn clean install`
3. Add the tenant credentials (appKey, accessKey, secretKey and tenantUrl) in CredentialProvider.java
4. Run as a Standard Java Application using the main method found in `ExampleDriver.java`
