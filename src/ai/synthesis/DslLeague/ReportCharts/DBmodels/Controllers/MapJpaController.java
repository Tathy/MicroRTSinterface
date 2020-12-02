/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers;

import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.NonexistentEntityException;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.PreexistingEntityException;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Map;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Test;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class MapJpaController implements Serializable {

    public MapJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Map map) throws PreexistingEntityException, Exception {
        if (map.getTestList() == null) {
            map.setTestList(new ArrayList<Test>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Test> attachedTestList = new ArrayList<Test>();
            for (Test testListTestToAttach : map.getTestList()) {
                testListTestToAttach = em.getReference(testListTestToAttach.getClass(), testListTestToAttach.getIdTest());
                attachedTestList.add(testListTestToAttach);
            }
            map.setTestList(attachedTestList);
            em.persist(map);
            for (Test testListTest : map.getTestList()) {
                Map oldMapIdOfTestListTest = testListTest.getMapId();
                testListTest.setMapId(map);
                testListTest = em.merge(testListTest);
                if (oldMapIdOfTestListTest != null) {
                    oldMapIdOfTestListTest.getTestList().remove(testListTest);
                    oldMapIdOfTestListTest = em.merge(oldMapIdOfTestListTest);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMap(map.getIdMap()) != null) {
                throw new PreexistingEntityException("Map " + map + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Map map) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Map persistentMap = em.find(Map.class, map.getIdMap());
            List<Test> testListOld = persistentMap.getTestList();
            List<Test> testListNew = map.getTestList();
            List<Test> attachedTestListNew = new ArrayList<Test>();
            for (Test testListNewTestToAttach : testListNew) {
                testListNewTestToAttach = em.getReference(testListNewTestToAttach.getClass(), testListNewTestToAttach.getIdTest());
                attachedTestListNew.add(testListNewTestToAttach);
            }
            testListNew = attachedTestListNew;
            map.setTestList(testListNew);
            map = em.merge(map);
            for (Test testListOldTest : testListOld) {
                if (!testListNew.contains(testListOldTest)) {
                    testListOldTest.setMapId(null);
                    testListOldTest = em.merge(testListOldTest);
                }
            }
            for (Test testListNewTest : testListNew) {
                if (!testListOld.contains(testListNewTest)) {
                    Map oldMapIdOfTestListNewTest = testListNewTest.getMapId();
                    testListNewTest.setMapId(map);
                    testListNewTest = em.merge(testListNewTest);
                    if (oldMapIdOfTestListNewTest != null && !oldMapIdOfTestListNewTest.equals(map)) {
                        oldMapIdOfTestListNewTest.getTestList().remove(testListNewTest);
                        oldMapIdOfTestListNewTest = em.merge(oldMapIdOfTestListNewTest);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = map.getIdMap();
                if (findMap(id) == null) {
                    throw new NonexistentEntityException("The map with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Map map;
            try {
                map = em.getReference(Map.class, id);
                map.getIdMap();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The map with id " + id + " no longer exists.", enfe);
            }
            List<Test> testList = map.getTestList();
            for (Test testListTest : testList) {
                testListTest.setMapId(null);
                testListTest = em.merge(testListTest);
            }
            em.remove(map);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Map> findMapEntities() {
        return findMapEntities(true, -1, -1);
    }

    public List<Map> findMapEntities(int maxResults, int firstResult) {
        return findMapEntities(false, maxResults, firstResult);
    }

    private List<Map> findMapEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Map.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Map findMap(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Map.class, id);
        } finally {
            em.close();
        }
    }

    public int getMapCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Map> rt = cq.from(Map.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
