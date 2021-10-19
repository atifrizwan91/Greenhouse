package com.mcl.app.test_server;

import org.iotivity.*;

public class Main {
	static private Thread mainThread;
	static private Thread shutdownHook = new Thread() {
		public void run() {
			System.out.println("Calling main_shutdown.");
			OCMain.mainShutdown();
			mainThread.interrupt();
		}
	};

	public static void main(String argv[]) {
		mainThread = Thread.currentThread();
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		OCBufferSettings.setMaxAppDataSize(1000000);
		OcfHandler ocfHandler = new OcfHandler();
		int init_ret = OCMain.mainInit(ocfHandler);
		if (init_ret < 0) {
			System.exit(init_ret);
		}

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.print("End Server---------------------------------");
		System.exit(0);
	}
}
