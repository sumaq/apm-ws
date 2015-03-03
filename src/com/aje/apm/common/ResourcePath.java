package com.aje.apm.common;

import java.io.File;
import java.io.IOException;

public class ResourcePath {

	private Object targetClass;

	public ResourcePath(Object targetClass) {
		this.targetClass = targetClass;
	}

	public String getWEBINFpath() {
		OperatingSystem os = new OperatingSystem();
		String result = null;
		try {
			String WEB_INF = "WEB-INF";
			String fullPath = new File(targetClass.getClass()
					.getProtectionDomain().getCodeSource().getLocation()
					.getFile()).getCanonicalPath();
			int posWebInf = fullPath.indexOf(WEB_INF);
			if (posWebInf > 0) {
				result = fullPath.substring(0, posWebInf + WEB_INF.length());
			} else {
				int posBuildFolder = fullPath.indexOf("build");
				if (posBuildFolder > 0) {
					result = fullPath.substring(0, posBuildFolder)
							+ "WebContent" + os.getOSPathDelimiter()
							+ "WEB-INF";
				} else {
					result = fullPath;
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading WEB-INF folder: "
					+ e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

}
