/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import business.ServiceBean;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import models.Projects;

/**
 *
 * @author Chris
 */
@SessionScoped
@ManagedBean(name = "frontEnd")
public class FrontEnd {

    Projects selected = null;
    List<Projects> projects = null;
    Projects newProject = null;

    @EJB
    ServiceBean sb;

    public List<Projects> getItems() {
        System.out.println("getProjects");
        projects = sb.getAllProjects();
        return projects;
    }

    public Projects getSelected() {
        if (selected == null) {
            return new Projects();
        } else {
            return selected;
        }
    }

    public String prepareView(Projects project) {
        selected = project;
        return "View";
    }

    public String destroy(Projects project) {
        try {
            sb.deleteProject(project.getProjectName());
            projects = sb.getAllProjects();
            logMessage("ProjectsDeleted");
            return "List";
        } catch (Exception e) {
            logError(e);
            return null;
        }
    }

    public String update() {
        try {
            System.out.println("Updating: " + newProject.getId());
            sb.updateProject(newProject);
            selected = sb.getProjectByName(newProject.getProjectName());
            return "View";
        } catch (Exception e) {
            logError(e);
            return null;
        }
    }

    public String create() {
        try {
            System.out.println(newProject);
            sb.createNewProject(newProject);
            selected = sb.getProjectByName(newProject.getProjectName());
            logMessage("ProjectsCreated");
            return "View";
        } catch (Exception e) {
            logError(e);
            return null;
        }
    }

    public String prepareCreate() {
        newProject = new Projects();
        return "Create";
    }

    public String prepareEdit(Projects project) {
        if (project != null) {
            selected = project;
        }
        newProject = new Projects(selected.getId(), selected.getProjectName(), selected.getDeveloperId(), selected.getDescription());
        return "Edit";
    }

    public String prepareList() {
        projects = sb.getAllProjects();
        return "List";
    }
    
    public Projects getNewProject() {
        return newProject;
    }

    public void logError(Exception e) {
        addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
    }

    public void logMessage(String message) {
        addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString(message));
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

}
