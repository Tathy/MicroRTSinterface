/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers;

import ai.synthesis.DslLeague.ReportCharts.DBmodels.Agent;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.NonexistentEntityException;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Test;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class AgentJpaController implements Serializable {
    private int id_cont;
            
    public AgentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
        this.id_cont = 1;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Agent agent) throws PreexistingEntityException, Exception {
        agent.setIdIa(id_cont);
        this.id_cont++;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Test idTest = agent.getIdTest();
            if (idTest != null) {
                idTest = em.getReference(idTest.getClass(), idTest.getIdTest());
                agent.setIdTest(idTest);
            }
            em.persist(agent);
            if (idTest != null) {
                idTest.getAgentList().add(agent);
                idTest = em.merge(idTest);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAgent(agent.getIdIa()) != null) {
                throw new PreexistingEntityException("Agent " + agent + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Agent agent) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agent persistentAgent = em.find(Agent.class, agent.getIdIa());
            Test idTestOld = persistentAgent.getIdTest();
            Test idTestNew = agent.getIdTest();
            if (idTestNew != null) {
                idTestNew = em.getReference(idTestNew.getClass(), idTestNew.getIdTest());
                agent.setIdTest(idTestNew);
            }
            agent = em.merge(agent);
            if (idTestOld != null && !idTestOld.equals(idTestNew)) {
                idTestOld.getAgentList().remove(agent);
                idTestOld = em.merge(idTestOld);
            }
            if (idTestNew != null && !idTestNew.equals(idTestOld)) {
                idTestNew.getAgentList().add(agent);
                idTestNew = em.merge(idTestNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = agent.getIdIa();
                if (findAgent(id) == null) {
                    throw new NonexistentEntityException("The agent with id " + id + " no longer exists.");
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
            Agent agent;
            try {
                agent = em.getReference(Agent.class, id);
                agent.getIdIa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agent with id " + id + " no longer exists.", enfe);
            }
            Test idTest = agent.getIdTest();
            if (idTest != null) {
                idTest.getAgentList().remove(agent);
                idTest = em.merge(idTest);
            }
            em.remove(agent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Agent> findAgentEntities() {
        return findAgentEntities(true, -1, -1);
    }

    public List<Agent> findAgentEntities(int maxResults, int firstResult) {
        return findAgentEntities(false, maxResults, firstResult);
    }

    private List<Agent> findAgentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agent.class));
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

    public Agent findAgent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agent.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Agent> rt = cq.from(Agent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
