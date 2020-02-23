/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soap;

import business.ServiceBean;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import models.NotFoundException;
import models.Projects;

/**
 *
 * @author Chris
 */
@WebService(serviceName = "ProjectsSoap")

@RolesAllowed("AdminRole") // - Isn't working - unsure why
@Stateless()
public class ProjectsSoap {

    @EJB
    ServiceBean sb;

    @WebMethod(operationName = "findAll")
    public List<Projects> findall() {
        return sb.getAllProjects();
    }

    @WebMethod(operationName = "findByName")
    public Projects findByName(@WebParam(name = "name") String name) 
            throws NotFoundException {
        Projects res = sb.getProjectByName(name);
        if (res == null) {
            System.out.println("Throwing not found exception");
            throw new NotFoundException();
        }
        return res;
    }

    @WebMethod(operationName = "create")
    public Projects create(
            @WebParam(name = "name") String name, 
            @WebParam(name = "developerId") String developerId, 
            @WebParam(name = "description") String description
    ) throws Exception {
        Projects newProject = new Projects();
        newProject.setProjectName(name);
        newProject.setDeveloperId(developerId);
        newProject.setDescription(description);
        System.out.println(newProject.toString());
        try {
            sb.createNewProject(newProject);
        } catch (EJBException e) {
            Throwable cause = (Throwable) e.getCause().getCause();
            throw new Exception(cause.getMessage());
        }

        return sb.getProjectByName(name);
    }

    @WebMethod(operationName = "update")
    public Projects update(
            @WebParam(name = "id") int id, 
            @WebParam(name = "name") String name, 
            @WebParam(name = "developerId") String developerId, 
            @WebParam(name = "description") String description
    ) throws Exception {
        Projects newProject = new Projects(id, name, developerId, description);

        try {
            sb.updateProject(newProject);
            Projects res = sb.getProjectByName(name);
            return res;
        } catch (EJBException e) {
            Throwable cause = (Throwable) e.getCause().getCause();
            throw new Exception(cause.getMessage());
        }
    }

    @WebMethod(operationName = "delete")
    public void delete(@WebParam(name = "name") String name) 
            throws NotFoundException, Exception {
        try {
            sb.deleteProject(name);
        } catch (EJBException e) {
            Throwable cause = (Throwable) e.getCause().getCause();

            if (cause.getMessage().equals(ServiceBean.NOT_FOUND)) {
                throw new NotFoundException();
            } else {
                throw new Exception(cause.getMessage());
            }
        }
    }
}
