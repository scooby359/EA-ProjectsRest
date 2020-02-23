/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import business.ServiceBean;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import models.Projects;

/**
 *
 * @author Chris
 */
@Stateless
@Path("projects")
@RolesAllowed({"AdminRole"}) // Restricts all endpoints to AdminRole by default
public class ProjectsFacadeREST  {
    
    @EJB
    private ServiceBean sb;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Projects entity) {
        try {
            this.sb.createNewProject(entity);
            return Response.status(Response.Status.CREATED).build();
        } catch (EJBException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("{name}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response edit(@PathParam("name") String name, Projects entity) {
        try {
            this.sb.updateProject(entity);
            return Response.status(Response.Status.CREATED).build();
        } catch (EJBException e) {
           // EJB nests originating exception - need to retrieve
           Throwable cause = (Throwable) e.getCause().getCause();
           
           // Check cause of error to return appropriate status
           if (cause.getMessage().equals(sb.NOT_FOUND)) {
               return Response.status(Response.Status.NOT_FOUND).build();
           } else {
               return Response.status(Response.Status.BAD_REQUEST).build();
           }
        }
    }

    @DELETE
    @Path("{name}")
    public Response remove(@PathParam("name") String name) {
        try {
            this.sb.deleteProject(name);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (EJBException e) {
            // EJB nests originating exception - need to retrive
            Throwable cause = (Throwable) e.getCause().getCause();
            
            // Check cause of error to return correct status
            if (cause.getMessage().equals(sb.NOT_FOUND)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    @GET
    @Path("{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("name") String name) {
        Projects project = sb.getProjectByName(name);
        
        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(project).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Projects> findAll() {
        return sb.getAllProjects();
    }
    
}
