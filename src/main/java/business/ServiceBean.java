/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import data.DataAccess;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import models.Projects;

/**
 *
 * @author Chris
 */
@Stateless
public class ServiceBean {

    public final static String NOT_FOUND = "NOT FOUND";
    
    @EJB
    DataAccess da;
    
    /**
     * Create a new project
     * @param entity 
     */
    public void createNewProject(Projects entity) {
        
        // Check new entity valid, or throw error
        String errors = validateNewProject(entity);
        if (errors != null) {
            throw new IllegalArgumentException(errors);
        }
        
        // Check project name is unique
        List<Projects> projNames = da.findByProjectName(entity.getProjectName());
        if (!projNames.isEmpty()) {
            throw new IllegalArgumentException("Project name already in use");
        }
        
        // Check lead developer is unique
        List<Projects> projDevs = da.findByDeveloperId(entity.getDeveloperId());
        if (!projDevs.isEmpty()) {
            throw new IllegalArgumentException("Developer already assigned to project");
        }
        
        // Add to db
        da.save(entity);
    }
    
    public void updateProject(Projects entity) {
        // Check new entity valid, or throw error
        String errors = validateNewProject(entity);
        if (errors != null) {
            throw new IllegalArgumentException(errors);
        }
        
        // Check project exists
        List<Projects> project = da.findByProjectId(entity.getId());
        if (project.isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND);
        }
        
        // Make sure correct entity DB ID set
        entity.setId(project.get(0).getId());
        
        // Merge with DB
        da.update(entity);
    }
    
    public Projects getProjectByName(String name) {
        List<Projects> projects = da.findByProjectName(name);
        if (projects.isEmpty()) {
            return null;
        } else {
            return projects.get(0);
        }
    }
    
    public List<Projects> getAllProjects() {
        return da.findAll();
    }
    
    public void deleteProject(String name) {
        // Check project exists
        List<Projects> projects = da.findByProjectName(name);
        if (projects.isEmpty()) {
            throw new IllegalArgumentException(NOT_FOUND);
        }
        
        da.remove(projects.get(0));
    }
    
    private String validateNewProject(Projects entity) {

        Boolean isError = false;
        String errorMsg = "Required fields missing:";
        
        if (entity == null) {
            errorMsg = errorMsg + " name, developerId, description";
            throw new IllegalArgumentException(errorMsg);
        }
        
        if (entity.getProjectName() == null || entity.getProjectName().isEmpty()) {
            errorMsg = errorMsg + " name,";
            isError = true;
        }
        if (entity.getDeveloperId() == null || entity.getDeveloperId().isEmpty()) {
            errorMsg = errorMsg + " developerId,";
            isError = true;
        }
        if (entity.getDescription() == null || entity.getDescription().isEmpty()) {
            errorMsg = errorMsg + " description";
            isError = true;
        }
        if (!isError) {
            errorMsg = null;
        }
        return errorMsg;
    }
}
