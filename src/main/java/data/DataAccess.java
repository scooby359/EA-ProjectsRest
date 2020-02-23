/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import models.Projects;

/**
 *
 * @author Chris
 */
@Stateless
public class DataAccess {

    @PersistenceContext(unitName = "DevelopersRestPU")
    private EntityManager em;

    /**
     * Save entity to DB
     *
     * @param entity
     */
    public void save(Projects entity) {
        em.persist(entity);
    }

    /**
     * Get all items from DB
     *
     * @return
     */
    public List<Projects> findAll() {
        Query query = em.createNamedQuery("Projects.findAll");
        List<Projects> result = query.getResultList();
        return result;
    }

    /**
     * Find all items that match given project name
     *
     * @param name
     * @return
     */
    public List<Projects> findByProjectName(String name) {
        Query query = em.createNamedQuery("Projects.findByProjectName");
        query.setParameter("projectName", name);
        List<Projects> result = query.getResultList();
        return result;
    }

    /**
     * Find all items that match given developerId
     *
     * @param developerId
     * @return
     */
    public List<Projects> findByDeveloperId(String developerId) {
        Query query = em.createNamedQuery("Projects.findByDeveloperId");
        query.setParameter("developerId", developerId);
        List<Projects> result = query.getResultList();
        return result;
    }
    
    /**
     * Find all items that match given id
     *
     * @param id
     * @return
     */
    public List<Projects> findByProjectId(Integer id) {
        Query query = em.createNamedQuery("Projects.findById");
        query.setParameter("id", id);
        List<Projects> result = query.getResultList();
        return result;
    }

    /**
     * Update existing item
     *
     * @param entity
     */
    public void update(Projects entity) {
        em.merge(entity);
    }

    /**
     * Remove given given item from DB
     *
     * @param entity
     */
    public void remove(Projects entity) {
        em.remove(entity);
    }
    
}
