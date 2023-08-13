package net.andrewcpu.j2ts.model;

public class AuthenticatedRoute {
	public String packageName;
	public String headerKey;


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setHeaderKey(String headerKey) {
		this.headerKey = headerKey;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getHeaderKey() {
		return headerKey;
	}
}
