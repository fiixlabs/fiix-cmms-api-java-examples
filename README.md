# Fiix Cmms Client Java Examples

## Overview

This project contains various examples of how to use the [Fiix-Cmms-Java-Client SDK](https://github.com/fiixlabs/fiix-cmms-client-java). It covers the basics of making CRUD and RPC requests using the SDK, as well as a more in depth overview on creating Work Orders, and associating them with Tasks and Assets.

Documentation for the API can be found [here](https://fiixlabs.github.io/api-documentation).

Examples are separated into 3 classes, with added helper classes.

### RpcExample
Describes the process of creating and making an Rpc Request. The request made in this example simply pings the server, and returns `true` or `false` depending on the success of the ping request.

### CRUDExample:
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
3. Run as a Standard Java Application using the main method found in `ExampleDriver.java`
