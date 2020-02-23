/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import data.DataAccess;
import java.util.ArrayList;
import java.util.List;
import models.Projects;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Chris
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceBeanTest {

    @Mock
    private static DataAccess da;

    @InjectMocks
    private ServiceBean sb;

    @Captor
    private ArgumentCaptor<Projects> ac;

    public ServiceBeanTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        // Set up data access mock
        da = mock(DataAccess.class);
    }

    @Test
    public void testGetAllProjectsReturnsList() {
        // Setup
        List<Projects> list = new ArrayList<Projects>();
        list.add(getTestProject());
        when(da.findAll()).thenReturn(list);

        // Act
        List<Projects> res = sb.getAllProjects();

        // Assert
        assertTrue(res.size() == 1);
        verify(da).findAll();
    }

    @Test
    public void testWhenGetByNameAndExistsReturnsProject() {
        // Setup
        Projects sample = getTestProject();
        List<Projects> sampleList = new ArrayList<Projects>();
        sampleList.add(sample);
        when(da.findByProjectName(sample.getProjectName())).thenReturn(sampleList);

        // Act
        Projects res = sb.getProjectByName(sample.getProjectName());

        // Assert
        assertTrue(res.getId().intValue() == sample.getId());
        assertTrue(res.getProjectName().equals(sample.getProjectName()));
        assertTrue(res.getDeveloperId().equals(sample.getDeveloperId()));
        assertTrue(res.getDescription().equals(sample.getDescription()));
        verify(da).findByProjectName(sample.getProjectName());
    }

    @Test
    public void testWhenGetByNameAndNotExistsReturnsNull() {
        // Setup
        when(da.findByProjectName(anyString())).thenReturn(new ArrayList<Projects>());

        // Act
        Projects res = sb.getProjectByName("test");

        // Assert
        assertTrue(res == null);
        verify(da).findByProjectName(anyString());
    }

    @Test
    public void testWhenCreateNewAndValidDbCalledToSave() {
        // setup
        Projects sample = getTestProject();
        sample.setId(0);

        when(da.findByProjectName(anyString())).thenReturn(new ArrayList<Projects>());
        when(da.findByDeveloperId(anyString())).thenReturn(new ArrayList<Projects>());

        // act
        sb.createNewProject(sample);

        // assert
        verify(da).findByProjectName(anyString());
        verify(da).findByDeveloperId(anyString());
        verify(da).save(sample);
    }

     @Test
    public void testWhenCreateNewAndProjectNameInUseExceptionThrown() {
        // setup
        Projects sample = getTestProject();
        sample.setId(0);
        List<Projects> list = new ArrayList<Projects>();
        list.add(sample);

        when(da.findByProjectName(anyString())).thenReturn(list);

        // act
        try {
            sb.createNewProject(sample);
        } catch (IllegalArgumentException e) {
            // Expected result
            // assert
            verify(da).findByProjectName(anyString());
            verify(da, never()).findByDeveloperId(anyString());
            verify(da, never()).save(sample);
            return;
        }

        // Shouldn't reach this point
        fail();
    }
    
    @Test
    public void testWhenCreateNewAndNullEntityExceptionThrown() {
        // setup
        
        // act
        try {
            sb.createNewProject(null);
        } catch (IllegalArgumentException e) {
            // Expected result
            // assert
            verify(da, never()).findByProjectName(anyString());
            verify(da, never()).findByDeveloperId(anyString());
            verify(da, never()).save((Projects) anyObject());
            return;
        }

        // Shouldn't reach this point
        fail();
    }

    @Test
    public void testWhenCreateNewAndDeveloperIdInUseExceptionThrown() {
        // setup
        Projects sample = getTestProject();
        sample.setId(0);
        List<Projects> list = new ArrayList<Projects>();
        list.add(sample);

        when(da.findByProjectName(anyString())).thenReturn(new ArrayList<Projects>());
        when(da.findByDeveloperId(anyString())).thenReturn(list);

        // act
        try {
            sb.createNewProject(sample);
        } catch (IllegalArgumentException e) {
            // Expected result
            // assert
            verify(da).findByProjectName(anyString());
            verify(da).findByDeveloperId(anyString());
            verify(da, never()).save(sample);
            return;
        }

        // Shouldn't reach this point
        fail();
    }

    @Test
    public void testWhenCreateNewAndProjectNameEmptyExceptionThrown() {
        // setup
        Projects test = new Projects(0, null, "test", "test");

        // act
        try {
            sb.createNewProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            verify(da, never()).findByProjectName(anyString());
            verify(da, never()).findByDeveloperId(anyString());
            verify(da, never()).save((Projects) anyObject());
            return;
        }

        // Shouldn't get here
        fail();
    }

    @Test
    public void testWhenCreateNewAndDevIdEmptyExceptionThrown() {
        // setup
        Projects test = new Projects(0, "test", null, "test");

        // act
        try {
            sb.createNewProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            verify(da, never()).findByProjectName(anyString());
            verify(da, never()).findByDeveloperId(anyString());
            verify(da, never()).save((Projects) anyObject());
            return;
        }

        // Shouldn't get here
        fail();
    }

    @Test
    public void testWhenCreateNewAndDescriptionEmptyExceptionThrown() {
        // setup
        Projects test = new Projects(0, "test", "test", null);

        // act
        try {
            sb.createNewProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            verify(da, never()).findByProjectName(anyString());
            verify(da, never()).findByDeveloperId(anyString());
            verify(da, never()).save((Projects) anyObject());
            return;
        }

        // Shouldn't get here
        fail();
    }

    @Test
    public void testWhenUpdateAndValidDBCalledtoSave() {
        // setup
        Projects test = getTestProject();
        List<Projects> list = new ArrayList<Projects>();
        list.add(test);

        when(da.findByProjectId(test.getId())).thenReturn(list);
        when(da.findByProjectName(test.getProjectName())).thenReturn(list);
        when(da.findByDeveloperId(test.getDeveloperId())).thenReturn(list);

        // act
        sb.updateProject(test);

        // assert
        verify(da).findByProjectId(test.getId());
        verify(da).findByProjectName(test.getProjectName());
        verify(da).findByDeveloperId(test.getDeveloperId());
        verify(da).update(test);
    }

    @Test
    public void testWhenUpdateAndNotFoundExceptionThrown() {
        // setup
        Projects test = getTestProject();
        when(da.findByProjectId(test.getId())).thenReturn(new ArrayList<Projects>());

        // act
        try {
            sb.updateProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            // Expected
            verify(da).findByProjectId(test.getId());
            verify(da, never()).findByProjectName(test.getProjectName());
            verify(da, never()).findByDeveloperId(test.getDeveloperId());
            verify(da, never()).update(test);
            return;
        }
        fail();

    }

    @Test
    public void testWhenUpdateAndNoProjectNameExceptionThrown() {
        // setup
        Projects test = getTestProject();
        test.setProjectName(null);

        // act
        try {
            sb.updateProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            // Expected
            verify(da, never()).findByProjectId(test.getId());
            verify(da, never()).findByProjectName(test.getProjectName());
            verify(da, never()).findByDeveloperId(test.getDeveloperId());
            verify(da, never()).update(test);
            return;
        }
        fail();
    }

    @Test
    public void testWhenUpdateAndNoDeveloperIdExceptionThrown() {
// setup
        Projects test = getTestProject();
        test.setDeveloperId(null);

        // act
        try {
            sb.updateProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            // Expected
            verify(da, never()).findByProjectId(test.getId());
            verify(da, never()).findByProjectName(test.getProjectName());
            verify(da, never()).findByDeveloperId(test.getDeveloperId());
            verify(da, never()).update(test);
            return;
        }
        fail();
    }

    @Test
    public void testWhenUpdateAndNoDescriptionExceptionThrown() {
        // setup
        Projects test = getTestProject();
        test.setDescription(null);

        // act
        try {
            sb.updateProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            // Expected
            verify(da, never()).findByProjectId(test.getId());
            verify(da, never()).findByProjectName(test.getProjectName());
            verify(da, never()).findByDeveloperId(test.getDeveloperId());
            verify(da, never()).update(test);
            return;
        }
        fail();
    }

    @Test
    public void testWhenUpdateAndProjectNameDuplicatedExceptionThrown() {
        // setup
        Projects test = getTestProject();
        Projects test2 = getTestProjectTwo();
        List<Projects> list = new ArrayList<Projects>();
        list.add(test);
        List<Projects> list2 = new ArrayList<Projects>();
        list2.add(test2);

        when(da.findByProjectId(test.getId())).thenReturn(list);
        when(da.findByProjectName(test.getProjectName())).thenReturn(list2);

        // act
        try {
            sb.updateProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            // Expected
            verify(da).findByProjectId(test.getId());
            verify(da).findByProjectName(test.getProjectName());
            verify(da, never()).findByDeveloperId(test.getDeveloperId());
            verify(da, never()).update(test);
            return;
        }
        fail();

    }

    @Test
    public void testWhenUpdateAndDeveloperIdDuplicatedExceptionThrown() {
         // setup
        Projects test = getTestProject();
        Projects test2 = getTestProjectTwo();
        List<Projects> list = new ArrayList<Projects>();
        list.add(test);
        List<Projects> list2 = new ArrayList<Projects>();
        list2.add(test2);

        when(da.findByProjectId(test.getId())).thenReturn(list);
        when(da.findByProjectName(test.getProjectName())).thenReturn(list);
        when(da.findByDeveloperId(test.getDeveloperId())).thenReturn(list2);
        
        // act
        try {
            sb.updateProject(test);
        } catch (IllegalArgumentException e) {
            // assert
            // Expected
            verify(da).findByProjectId(test.getId());
            verify(da).findByProjectName(test.getProjectName());
            verify(da).findByDeveloperId(test.getDeveloperId());
            verify(da, never()).update(test);
            return;
        }
        fail();

    }
    
    @Test
    public void testWhenDeleteAndValidDbCalled() {
        // setup
        Projects test = getTestProject();
        List<Projects> list = new ArrayList<Projects>();
        list.add(test);
        
        when(da.findByProjectName(test.getProjectName())).thenReturn(list);
        
        // act
        sb.deleteProject(test.getProjectName());
        
        // assert
        verify(da).findByProjectName(test.getProjectName());
        verify(da).remove(test);
    }
    
    @Test
    public void testWhenDeleteAndNotFoundExceptionThrown() {
        // setup
        Projects test = getTestProject();
        
        when(da.findByProjectName(test.getProjectName()))
                .thenReturn(new ArrayList<Projects>());
        
        // act
        
        try {
            sb.deleteProject(test.getProjectName());
        } catch (IllegalArgumentException e) {
            // Assert
            // Expected
            verify(da).findByProjectName(test.getProjectName());
            verify(da, never()).remove(test);
            return;
        }
        fail();
    }

    // Helpers
    private Projects getTestProject() {
        return new Projects(
                1,
                "test",
                "123",
                "Test desc"
        );
    }

    private Projects getTestProjectTwo() {
        return new Projects(
                2,
                "test2",
                "1234",
                "Test desc 2"
        );
    }
}
