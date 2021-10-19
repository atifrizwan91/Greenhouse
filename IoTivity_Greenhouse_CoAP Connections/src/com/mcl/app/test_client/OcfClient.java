package com.mcl.app.test_client;

import java.util.Arrays;
import java.util.Iterator;
import org.iotivity.CborEncoder;
import org.iotivity.OCClientResponse;
import org.iotivity.OCDiscoveryFlags;
import org.iotivity.OCDiscoveryHandler;
import org.iotivity.OCEndpoint;
import org.iotivity.OCEndpointParseException;
import org.iotivity.OCEndpointUtil;
import org.iotivity.OCMain;
import org.iotivity.OCMethod;
import org.iotivity.OCQos;
import org.iotivity.OCRep;
import org.iotivity.OCResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OcfClient {
	public static String L = "-------------------------------";
	private String resourceUri;
	private String uriQuery;
	private String payloadJsonStr;
	private String resourceType;
	private OCEndpoint serverEndpoint;
	private String resultJsonStr;
	private boolean isRequesting;

	public String doReqeust(String uri, String resourceUri, String uriQuery, String payloadJsonStr, OCMethod ocMethod) {
		this.resourceUri = resourceUri;
		this.uriQuery = uriQuery;
		this.payloadJsonStr = payloadJsonStr;
		isRequesting = true;
		try {
			serverEndpoint = OCEndpointUtil.stringToEndpoint(uri);
		} catch (OCEndpointParseException e) {
			e.printStackTrace();
		}

		System.out.println(OcfClient.L + Arrays.toString(serverEndpoint.getAddr().getIpv4().getAddress()) + ":" + serverEndpoint.getAddr().getIpv4().getPort());
		isRequesting = true;
		Thread requestThread = new Thread(new Runnable() {
			@Override
			public void run() {
				switch (ocMethod) {
				case OC_GET:
					getRequest();
					break;
				case OC_POST:
					postRequest();
					break;
				case OC_PUT:
					putRequest();
					break;
				case OC_DELETE:
					deleteRequest();
					break;
				case OC_FETCH:
					break;
				default:
					break;
				}
			}
		});
		requestThread.start();
		while (isRequesting) {
			System.out.print("");
		}
		return resultJsonStr;
	}

	private class DiscoverThread implements Runnable {
		@Override
		public void run() {
			OCMain.doIPDiscovery(resourceType, new OCDiscoveryHandler() {
				@Override
				public OCDiscoveryFlags handler(String anchor, String uri, String[] types, int interfaceMask, OCEndpoint endpoint, int resourcePropertiesMask) {
					System.out.println(OcfClient.L + "doIPDiscovery handler");
					for (String type : types) {
						if (type.equals(resourceType)) {
							serverEndpoint = OCEndpointUtil.listCopy(endpoint);
							System.out.println(OcfClient.L + "Dicovered Endpoint: " + Arrays.toString(endpoint.getAddr().getIpv4().getAddress()) + ":"
									+ endpoint.getAddr().getIpv4().getPort());
							isRequesting = false;
							return OCDiscoveryFlags.OC_STOP_DISCOVERY;
						}
					}
					return OCDiscoveryFlags.OC_CONTINUE_DISCOVERY;
				}
			});
		}
	}

	public String doDiscoeryAndReqeust(String resourceUri, String uriQuery, String payloadJsonStr, String resourceType, OCMethod ocMethod) {
		this.resourceUri = resourceUri;
		this.uriQuery = uriQuery;
		this.payloadJsonStr = payloadJsonStr;
		this.resourceType = resourceType;

		isRequesting = true;
		Thread discoveryThread = new Thread(new DiscoverThread());
		discoveryThread.start();
		while (isRequesting) {
			System.out.print(".");
		}
		System.out.println(OcfClient.L + Arrays.toString(serverEndpoint.getAddr().getIpv4().getAddress()) + ":" + serverEndpoint.getAddr().getIpv4().getPort());
		isRequesting = true;
		Thread requestThread = new Thread(new Runnable() {
			@Override
			public void run() {
				switch (ocMethod) {
				case OC_GET:
					System.out.println("------------------------------In Get Request");
					getRequest();
					break;
				case OC_POST:
					postRequest();
					break;
				case OC_PUT:
					putRequest();
					break;
				case OC_DELETE:
					deleteRequest();
					break;
				case OC_FETCH:
					break;
				default:
					break;
				}
			}
		});
		requestThread.start();
		while (isRequesting) {
			System.out.print("");
		}
		
		return resultJsonStr;
	}

	private void getRequest() {
		OCMain.doGet(resourceUri, serverEndpoint, uriQuery, new OCResponseHandler() {
			@Override
			public void handler(OCClientResponse response) {
				System.out.println(OcfClient.L + "GET Response Handler:");
				System.out.println(OCRep.toJSON(response.getPayload(), true));
				resultJsonStr = OCRep.toJSON(response.getPayload(), false);// rep, pretty_print
				isRequesting = false;
			}
		}, OCQos.LOW_QOS);
	}

	private void postRequest() {
		boolean init_ret = OCMain.initPost(resourceUri, serverEndpoint, uriQuery, new OCResponseHandler() {
			@Override
			public void handler(OCClientResponse response) {
				System.out.println(OcfClient.L + "POST Response Handler:");
				System.out.println(OCRep.toJSON(response.getPayload(), true));
				resultJsonStr = OCRep.toJSON(response.getPayload(), false);
				isRequesting = false;
			}
		}, OCQos.LOW_QOS);
		if (init_ret) {
			doOCRepPayload(payloadJsonStr);
			if (OCMain.doPost()) {
				System.out.println("\tSent request");
			} else {
				System.out.println("\tCould not send request");
			}
		} else {
			System.out.println("\tCould not init request");
		}
	}

	private void putRequest() {
		boolean init_ret = OCMain.initPost(resourceUri, serverEndpoint, uriQuery, new OCResponseHandler() {
			@Override
			public void handler(OCClientResponse response) {
				System.out.println(OcfClient.L + "PUT Response Handler:");
				System.out.println(OCRep.toJSON(response.getPayload(), true));
				resultJsonStr = OCRep.toJSON(response.getPayload(), false);
				isRequesting = false;
			}
		}, OCQos.LOW_QOS);

		if (init_ret) {
			doOCRepPayload(payloadJsonStr);
			if (OCMain.doPut()) {
				System.out.println("\tSent request");
			} else {
				System.out.println("\tCould not send request");
			}
		} else {
			System.out.println("\tCould not init request");
		}
	}

	private void deleteRequest() {
		OCMain.doDelete(resourceUri, serverEndpoint, null, new OCResponseHandler() {
			@Override
			public void handler(OCClientResponse response) {
				System.out.println(OcfClient.L + "DELETE Response Handler:");
				System.out.println(OCRep.toJSON(response.getPayload(), true));
				resultJsonStr = OCRep.toJSON(response.getPayload(), false);
				isRequesting = false;
			}
		}, OCQos.LOW_QOS);
	}

	private void doOCRepPayload(String payload) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(payload);
			System.out.println(L);
			System.out.println(jsonNode.toPrettyString());
			CborEncoder root = OCRep.beginRootObject();
			parseJson2OCRep("", jsonNode, root);
			OCRep.endRootObject();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private void parseJson2OCRep(String key, JsonNode jsonNode, CborEncoder encoder) {
		if (jsonNode.isTextual()) {
			System.out.println("S" + key + " " + jsonNode.asText());
			OCRep.setTextString(encoder, key, jsonNode.asText());
		} else if (jsonNode.isIntegralNumber()) {
			System.out.println("I" + key + " " + jsonNode.asLong());
			OCRep.setLong(encoder, key, jsonNode.asLong());
		} else if (jsonNode.isFloatingPointNumber()) {
			System.out.println("F" + key + " " + jsonNode.asDouble());
			OCRep.setDouble(encoder, key, jsonNode.asDouble());
		} else if (jsonNode.isBoolean()) {
			System.out.println("B" + key + " " + jsonNode.asBoolean());
			OCRep.setBoolean(encoder, key, jsonNode.asBoolean());
		} else if (jsonNode.isObject()) {
			System.out.println("O" + key);
			Iterator<String> names = jsonNode.fieldNames();
			if (key.isEmpty()) {
				while (names.hasNext()) {
					String mKey = names.next();
					JsonNode objNode = jsonNode.get(mKey);
					parseJson2OCRep(mKey, objNode, encoder);
				}
			} else {
				while (names.hasNext()) {
					CborEncoder ocObj = OCRep.openObject(encoder, key);
					String mKey = names.next();
					JsonNode objNode = jsonNode.get(mKey);
					parseJson2OCRep(mKey, objNode, ocObj);
					OCRep.closeObject(encoder, ocObj);
				}
			}
		} else if (jsonNode.isArray()) {
			System.out.println("A" + key);
			CborEncoder ocArr = OCRep.openArray(encoder, key);
			for (JsonNode arrItem : jsonNode) {
				CborEncoder ocItem = OCRep.objectArrayBeginItem(ocArr);
				parseJson2OCRep("", arrItem, ocItem);
				OCRep.objectArrayEndItem(ocArr, ocItem);
			}
			OCRep.closeArray(encoder, ocArr);
		}
	}
}
