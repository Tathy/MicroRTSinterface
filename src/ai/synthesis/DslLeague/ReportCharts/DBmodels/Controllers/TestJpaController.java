/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Map;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Agent;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.NonexistentEntityException;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.LeagueInfo;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class TestJpaController implements Serializable {

    public TestJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Test test) throws PreexistingEntityException, Exception {
        if (test.getAgentList() == null) {
            test.setAgentList(new ArrayList<Agent>());
        }
        if (test.getLeagueInfoList() == null) {
            test.setLeagueInfoList(new ArrayList<LeagueInfo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Map mapId = test.getMapId();
            if (mapId != null) {
                mapId = em.getReference(mapId.getClass(), mapId.getIdMap());
                test.setMapId(mapId);
            }
            List<Agent> attachedAgentList = new ArrayList<Agent>();
            for (Agent agentListAgentToAttach : test.getAgentList()) {
                agentListAgentToAttach = em.getReference(agentListAgentToAttach.getClass(), agentListAgentToAttach.getIdIa());
                attachedAgentList.add(agentListAgentToAttach);
            }
            test.setAgentList(attachedAgentList);
            List<LeagueInfo> attachedLeagueInfoList = new ArrayList<LeagueInfo>();
            for (LeagueInfo leagueInfoListLeagueInfoToAttach : test.getLeagueInfoList()) {
                leagueInfoListLeagueInfoToAttach = em.getReference(leagueInfoListLeagueInfoToAttach.getClass(), leagueInfoListLeagueInfoToAttach.getIdLeague());
                attachedLeagueInfoList.add(leagueInfoListLeagueInfoToAttach);
            }
            test.setLeagueInfoList(attachedLeagueInfoList);
            em.persist(test);
            if (mapId != null) {
                mapId.getTestList().add(test);
                mapId = em.merge(mapId);
            }
            for (Agent agentListAgent : test.getAgentList()) {
                Test oldIdTestOfAgentListAgent = agentListAgent.getIdTest();
                agentListAgent.setIdTest(test);
                agentListAgent = em.merge(agentListAgent);
                if (oldIdTestOfAgentListAgent != null) {
                    oldIdTestOfAgentListAgent.getAgentList().remove(agentListAgent);
                    oldIdTestOfAgentListAgent = em.merge(oldIdTestOfAgentListAgent);
                }
            }
            for (LeagueInfo leagueInfoListLeagueInfo : test.getLeagueInfoList()) {
                Test oldIdTestOfLeagueInfoListLeagueInfo = leagueInfoListLeagueInfo.getIdTest();
                leagueInfoListLeagueInfo.setIdTest(test);
                leagueInfoListLeagueInfo = em.merge(leagueInfoListLeagueInfo);
                if (oldIdTestOfLeagueInfoListLeagueInfo != null) {
                    oldIdTestOfLeagueInfoListLeagueInfo.getLeagueInfoList().remove(leagueInfoListLeagueInfo);
                    oldIdTestOfLeagueInfoListLeagueInfo = em.merge(oldIdTestOfLeagueInfoListLeagueInfo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTest(test.getIdTest()) != null) {
                throw new PreexistingEntityException("Test " + test + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Test test) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Test persistentTest = em.find(Test.class, test.getIdTest());
            Map mapIdOld = persistentTest.getMapId();
            Map mapIdNew = test.getMapId();
            List<Agent> agentListOld = persistentTest.getAgentList();
            List<Agent> agentListNew = test.getAgentList();
            List<LeagueInfo> leagueInfoListOld = persistentTest.getLeagueInfoList();
            List<LeagueInfo> leagueInfoListNew = test.getLeagueInfoList();
            if (mapIdNew != null) {
                mapIdNew = em.getReference(mapIdNew.getClass(), mapIdNew.getIdMap());
                test.setMapId(mapIdNew);
            }
            List<Agent> attachedAgentListNew = new ArrayList<Agent>();
            for (Agent agentListNewAgentToAttach : agentListNew) {
                agentListNewAgentToAttach = em.getReference(agentListNewAgentToAttach.getClass(), agentListNewAgentToAttach.getIdIa());
                attachedAgentListNew.add(agentListNewAgentToAttach);
            }
            agentListNew = attachedAgentListNew;
            test.setAgentList(agentListNew);
            List<LeagueInfo> attachedLeagueInfoListNew = new ArrayList<LeagueInfo>();
            for (LeagueInfo leagueInfoListNewLeagueInfoToAttach : leagueInfoListNew) {
                leagueInfoListNewLeagueInfoToAttach = em.getReference(leagueInfoListNewLeagueInfoToAttach.getClass(), leagueInfoListNewLeagueInfoToAttach.getIdLeague());
                attachedLeagueInfoListNew.add(leagueInfoListNewLeagueInfoToAttach);
            }
            leagueInfoListNew = attachedLeagueInfoListNew;
            test.setLeagueInfoList(leagueInfoListNew);
            test = em.merge(test);
            if (mapIdOld != null && !mapIdOld.equals(mapIdNew)) {
                mapIdOld.getTestList().remove(test);
                mapIdOld = em.merge(mapIdOld);
            }
            if (mapIdNew != null && !mapIdNew.equals(mapIdOld)) {
                mapIdNew.getTestList().add(test);
                mapIdNew = em.merge(mapIdNew);
            }
            for (Agent agentListOldAgent : agentListOld) {
                if (!agentListNew.contains(agentListOldAgent)) {
                    agentListOldAgent.setIdTest(null);
                    agentListOldAgent = em.merge(agentListOldAgent);
                }
            }
            for (Agent agentListNewAgent : agentListNew) {
                if (!agentListOld.contains(agentListNewAgent)) {
                    Test oldIdTestOfAgentListNewAgent = agentListNewAgent.getIdTest();
                    agentListNewAgent.setIdTest(test);
                    agentListNewAgent = em.merge(agentListNewAgent);
                    if (oldIdTestOfAgentListNewAgent != null && !oldIdTestOfAgentListNewAgent.equals(test)) {
                        oldIdTestOfAgentListNewAgent.getAgentList().remove(agentListNewAgent);
                        oldIdTestOfAgentListNewAgent = em.merge(oldIdTestOfAgentListNewAgent);
                    }
                }
            }
            for (LeagueInfo leagueInfoListOldLeagueInfo : leagueInfoListOld) {
                if (!leagueInfoListNew.contains(leagueInfoListOldLeagueInfo)) {
                    leagueInfoListOldLeagueInfo.setIdTest(null);
                    leagueInfoListOldLeagueInfo = em.merge(leagueInfoListOldLeagueInfo);
                }
            }
            for (LeagueInfo leagueInfoListNewLeagueInfo : leagueInfoListNew) {
                if (!leagueInfoListOld.contains(leagueInfoListNewLeagueInfo)) {
                    Test oldIdTestOfLeagueInfoListNewLeagueInfo = leagueInfoListNewLeagueInfo.getIdTest();
                    leagueInfoListNewLeagueInfo.setIdTest(test);
                    leagueInfoListNewLeagueInfo = em.merge(leagueInfoListNewLeagueInfo);
                    if (oldIdTestOfLeagueInfoListNewLeagueInfo != null && !oldIdTestOfLeagueInfoListNewLeagueInfo.equals(test)) {
                        oldIdTestOfLeagueInfoListNewLeagueInfo.getLeagueInfoList().remove(leagueInfoListNewLeagueInfo);
                        oldIdTestOfLeagueInfoListNewLeagueInfo = em.merge(oldIdTestOfLeagueInfoListNewLeagueInfo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = test.getIdTest();
                if (findTest(id) == null) {
                    throw new NonexistentEntityException("The test with id " + id + " no longer exists.");
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
            Test test;
            try {
                test = em.getReference(Test.class, id);
                test.getIdTest();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The test with id " + id + " no longer exists.", enfe);
            }
            Map mapId = test.getMapId();
            if (mapId != null) {
                mapId.getTestList().remove(test);
                mapId = em.merge(mapId);
            }
            List<Agent> agentList = test.getAgentList();
            for (Agent agentListAgent : agentList) {
                agentListAgent.setIdTest(null);
                agentListAgent = em.merge(agentListAgent);
            }
            List<LeagueInfo> leagueInfoList = test.getLeagueInfoList();
            for (LeagueInfo leagueInfoListLeagueInfo : leagueInfoList) {
                leagueInfoListLeagueInfo.setIdTest(null);
                leagueInfoListLeagueInfo = em.merge(leagueInfoListLeagueInfo);
            }
            em.remove(test);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Test> findTestEntities() {
        return findTestEntities(true, -1, -1);
    }

    public List<Test> findTestEntities(int maxResults, int firstResult) {
        return findTestEntities(false, maxResults, firstResult);
    }

    private List<Test> findTestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Test.class));
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

    public Test findTest(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Test.class, id);
        } finally {
            em.close();
        }
    }

    public int getTestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Test> rt = cq.from(Test.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
