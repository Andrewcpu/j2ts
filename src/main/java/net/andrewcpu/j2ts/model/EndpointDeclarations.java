package net.andrewcpu.j2ts.model;

import java.util.List;

public class EndpointDeclarations {
	public List<AuthenticatedRoute> authenticatedRoutes;

	public List<AuthenticatedRoute> getAuthenticatedRoutes() {
		return authenticatedRoutes;
	}

	public void setAuthenticatedRoutes(List<AuthenticatedRoute> authenticatedRoutes) {
		this.authenticatedRoutes = authenticatedRoutes;
	}
}
