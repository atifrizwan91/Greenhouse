package  com.mcl.app.test_server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.iotivity.*;

public class OcfHandler implements OCMainInitHandler {
	public static String L = "-------------------------------";
	public static String PAYLOAD_KEY = "payload";
	int device;

	@Override
	public int initialize() {
		System.out.println("inside OcfHandler.initialize()");
		OCMain.initPlatform("MCL");
		device |= OCMain.addDevice("/oic/d", "oic.d.iot", "MCL-IoT000", "ocf.0.0.0", "ocf.res.0.0.0");
		return device;
	}

	@Override
	public void registerResources() {
		registerLightGetPost();
		registerCalcPost();
		registerFileGet();
	}

	private void registerLightGetPost() {
		OCResource resource = OCMain.newResource("", "/light", (short) 1, device); // name, uri, num_resource_types, device
		OCMain.resourceBindResourceType(resource, "oic.r.light");
		OCMain.resourceBindResourceInterface(resource, OCInterfaceMask.RW);
		OCMain.resourceSetDefaultInterface(resource, OCInterfaceMask.RW);
		OCMain.resourceSetDiscoverable(resource, true);
		OCMain.resourceSetRequestHandler(resource, OCMethod.OC_GET, new OCRequestHandler() {
			@Override
			public void handler(OCRequest request, int interfaces) {
				System.out.println(interfaces);
				CborEncoder root = OCRep.beginRootObject();
				switch (interfaces) {
				case OCInterfaceMask.BASELINE: {
					OCMain.processBaselineInterface(request.getResource());
				}
				case OCInterfaceMask.RW: {
					OCRep.setTextString(root, PAYLOAD_KEY, "registerLightGet");
					break;
				}
				default:
					break;
				}
				OCRep.endRootObject();
				OCMain.sendResponse(request, OCStatus.OC_STATUS_OK);
			}
		});
		OCMain.resourceSetRequestHandler(resource, OCMethod.OC_POST, new OCRequestHandler() {
			@Override
			public void handler(OCRequest request, int interfaces) {
				CborEncoder root = OCRep.beginRootObject();
				switch (interfaces) {
				case OCInterfaceMask.BASELINE: {
					OCMain.processBaselineInterface(request.getResource());
				}
				case OCInterfaceMask.RW: {
					OCRep.setTextString(root, PAYLOAD_KEY, "registerLightPost");
					break;
				}
				default:
					break;
				}
				OCRep.endRootObject();
				OCMain.sendResponse(request, OCStatus.OC_STATUS_OK);
			}
		});
		OCMain.addResource(resource);
	}

	private void registerCalcPost() {
		OCResource resource = OCMain.newResource("", "/calc", (short) 1, device); // name, uri, num_resource_types, device
		OCMain.resourceBindResourceType(resource, "oic.r.calc");
		OCMain.resourceBindResourceInterface(resource, OCInterfaceMask.RW);
		OCMain.resourceSetDefaultInterface(resource, OCInterfaceMask.RW);
		OCMain.resourceSetDiscoverable(resource, true);
		OCMain.resourceSetRequestHandler(resource, OCMethod.OC_POST, new OCRequestHandler() {
			@Override
			public void handler(OCRequest request, int interfaces) {
				System.out.println(L + "/calc POST");
				switch (interfaces) {
				case OCInterfaceMask.BASELINE: {
					OCMain.processBaselineInterface(request.getResource());
				}
				case OCInterfaceMask.RW: {
					System.out.println(L + OCRep.toJSON(request.getRequestPayload(), false));
					CborEncoder root = OCRep.beginRootObject();
					OCRep.setTextString(root, PAYLOAD_KEY,
							"registerCalcPost" + "_query_" + request.getQuery() + "_payload_" + OCRep.toJSON(request.getRequestPayload(), false));
					OCRep.endRootObject();
					System.out.println(L + OCRep.getCborErrno());
					OCRepresentation ocRepresentation = OCRep.getOCRepresentaionFromRootObject();
					System.out.println(L + OCRep.toJSON(ocRepresentation, false));
					break;
				}
				default:
					break;
				}
				OCMain.sendResponse(request, OCStatus.OC_STATUS_OK);
			}
		});
		OCMain.addResource(resource);
	}

	private void registerFileGet() {
		OCResource resource = OCMain.newResource("", "/file", (short) 1, device); // name, uri, num_resource_types, device
		OCMain.resourceBindResourceType(resource, "oic.r.file");
		OCMain.resourceBindResourceInterface(resource, OCInterfaceMask.RW);
		OCMain.resourceSetDefaultInterface(resource, OCInterfaceMask.RW);
		OCMain.resourceSetDiscoverable(resource, true);
		OCMain.resourceSetRequestHandler(resource, OCMethod.OC_POST, new OCRequestHandler() {
			@Override
			public void handler(OCRequest request, int interfaces) {
				String query = request.getQuery();
				String fileName = query.trim().split("file=")[1].split("&")[0];
				File file = new File("file/" + fileName);
				byte[] fileContent = null;
				try {
					fileContent = Files.readAllBytes(file.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				String fileStr = Base64.getEncoder().encodeToString(fileContent);

				CborEncoder root = OCRep.beginRootObject();
				OCRep.setTextString(root, PAYLOAD_KEY, fileStr);
				OCRep.endRootObject();
				OCMain.sendResponse(request, OCStatus.OC_STATUS_OK);

//				System.out.println("hello");
//				File file = new File("tfmodel/model01/saved_model.pb");
//				try {
//					byte[] fileContent = Files.readAllBytes(file.toPath());
//					System.out.println(fileContent.length);
//
//					String str = Base64.getEncoder().encodeToString(fileContent);
//					System.out.println(str.length());
//
//					byte[] decode = Base64.getDecoder().decode(str);
//					System.out.println(decode.length);
//					
//					FileOutputStream out = new FileOutputStream("saved_model.pb");
//					out.write(decode);
//					out.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}

			}
		});
		OCMain.addResource(resource);
	}

	@Override
	public void requestEntry() {
		System.out.println("inside OcfHandler.requestEntry()");
	}
}
