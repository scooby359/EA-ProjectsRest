/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Chris
 */
public class NewProject {
    
    String name;
    String developerUserId;
    String description;
    
    public NewProject() {
    }

    public NewProject(String name, String developerUserId, String description) {
        this.name = name;
        this.developerUserId = developerUserId;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloperUserId() {
        return developerUserId;
    }

    public void setDeveloperUserId(String developerUserId) {
        this.developerUserId = developerUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NewProject{" + "name=" + name + ", developerUserId=" + developerUserId + ", description=" + description + '}';
    }
    
}
