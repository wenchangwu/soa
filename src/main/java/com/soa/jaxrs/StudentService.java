package com.soa.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path(value="/student/{id}")
@Produces("application/xml")
public interface StudentService {

	@GET
	@Path(value="info")
	public Student getStudent(@PathParam("id") long id,@QueryParam("name") String name);
	
	@GET
	@Path(value="/info2")
	public Student getStudent(@QueryParam("name") String name);
}
