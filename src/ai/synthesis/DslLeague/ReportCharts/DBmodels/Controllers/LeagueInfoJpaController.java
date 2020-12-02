/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers;

import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.NonexistentEntityException;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.LeagueInfo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Test;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.PlayerLeague;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class LeagueInfoJpaController implements Serializable {
    private int id_cont;
    
    public LeagueInfoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
        this.id_cont = 1;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LeagueInfo leagueInfo) {
        leagueInfo.setIdLeague(id_cont);
        this.id_cont++;
        
        if (leagueInfo.getPlayerLeagueList() == null) {
            leagueInfo.setPlayerLeagueList(new ArrayList<PlayerLeague>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Test idTest = leagueInfo.getIdTest();
            if (idTest != null) {
                idTest = em.getReference(idTest.getClass(), idTest.getIdTest());
                leagueInfo.setIdTest(idTest);
            }
            List<PlayerLeague> attachedPlayerLeagueList = new ArrayList<PlayerLeague>();
            for (PlayerLeague playerLeagueListPlayerLeagueToAttach : leagueInfo.getPlayerLeagueList()) {
                playerLeagueListPlayerLeagueToAttach = em.getReference(playerLeagueListPlayerLeagueToAttach.getClass(), playerLeagueListPlayerLeagueToAttach.getIdPlayer());
                attachedPlayerLeagueList.add(playerLeagueListPlayerLeagueToAttach);
            }
            leagueInfo.setPlayerLeagueList(attachedPlayerLeagueList);
            em.persist(leagueInfo);
            if (idTest != null) {
                idTest.getLeagueInfoList().add(leagueInfo);
                idTest = em.merge(idTest);
            }
            for (PlayerLeague playerLeagueListPlayerLeague : leagueInfo.getPlayerLeagueList()) {
                LeagueInfo oldIdLeagueOfPlayerLeagueListPlayerLeague = playerLeagueListPlayerLeague.getIdLeague();
                playerLeagueListPlayerLeague.setIdLeague(leagueInfo);
                playerLeagueListPlayerLeague = em.merge(playerLeagueListPlayerLeague);
                if (oldIdLeagueOfPlayerLeagueListPlayerLeague != null) {
                    oldIdLeagueOfPlayerLeagueListPlayerLeague.getPlayerLeagueList().remove(playerLeagueListPlayerLeague);
                    oldIdLeagueOfPlayerLeagueListPlayerLeague = em.merge(oldIdLeagueOfPlayerLeagueListPlayerLeague);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LeagueInfo leagueInfo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LeagueInfo persistentLeagueInfo = em.find(LeagueInfo.class, leagueInfo.getIdLeague());
            Test idTestOld = persistentLeagueInfo.getIdTest();
            Test idTestNew = leagueInfo.getIdTest();
            List<PlayerLeague> playerLeagueListOld = persistentLeagueInfo.getPlayerLeagueList();
            List<PlayerLeague> playerLeagueListNew = leagueInfo.getPlayerLeagueList();
            if (idTestNew != null) {
                idTestNew = em.getReference(idTestNew.getClass(), idTestNew.getIdTest());
                leagueInfo.setIdTest(idTestNew);
            }
            List<PlayerLeague> attachedPlayerLeagueListNew = new ArrayList<PlayerLeague>();
            for (PlayerLeague playerLeagueListNewPlayerLeagueToAttach : playerLeagueListNew) {
                playerLeagueListNewPlayerLeagueToAttach = em.getReference(playerLeagueListNewPlayerLeagueToAttach.getClass(), playerLeagueListNewPlayerLeagueToAttach.getIdPlayer());
                attachedPlayerLeagueListNew.add(playerLeagueListNewPlayerLeagueToAttach);
            }
            playerLeagueListNew = attachedPlayerLeagueListNew;
            leagueInfo.setPlayerLeagueList(playerLeagueListNew);
            leagueInfo = em.merge(leagueInfo);
            if (idTestOld != null && !idTestOld.equals(idTestNew)) {
                idTestOld.getLeagueInfoList().remove(leagueInfo);
                idTestOld = em.merge(idTestOld);
            }
            if (idTestNew != null && !idTestNew.equals(idTestOld)) {
                idTestNew.getLeagueInfoList().add(leagueInfo);
                idTestNew = em.merge(idTestNew);
            }
            for (PlayerLeague playerLeagueListOldPlayerLeague : playerLeagueListOld) {
                if (!playerLeagueListNew.contains(playerLeagueListOldPlayerLeague)) {
                    playerLeagueListOldPlayerLeague.setIdLeague(null);
                    playerLeagueListOldPlayerLeague = em.merge(playerLeagueListOldPlayerLeague);
                }
            }
            for (PlayerLeague playerLeagueListNewPlayerLeague : playerLeagueListNew) {
                if (!playerLeagueListOld.contains(playerLeagueListNewPlayerLeague)) {
                    LeagueInfo oldIdLeagueOfPlayerLeagueListNewPlayerLeague = playerLeagueListNewPlayerLeague.getIdLeague();
                    playerLeagueListNewPlayerLeague.setIdLeague(leagueInfo);
                    playerLeagueListNewPlayerLeague = em.merge(playerLeagueListNewPlayerLeague);
                    if (oldIdLeagueOfPlayerLeagueListNewPlayerLeague != null && !oldIdLeagueOfPlayerLeagueListNewPlayerLeague.equals(leagueInfo)) {
                        oldIdLeagueOfPlayerLeagueListNewPlayerLeague.getPlayerLeagueList().remove(playerLeagueListNewPlayerLeague);
                        oldIdLeagueOfPlayerLeagueListNewPlayerLeague = em.merge(oldIdLeagueOfPlayerLeagueListNewPlayerLeague);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = leagueInfo.getIdLeague();
                if (findLeagueInfo(id) == null) {
                    throw new NonexistentEntityException("The leagueInfo with id " + id + " no longer exists.");
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
            LeagueInfo leagueInfo;
            try {
                leagueInfo = em.getReference(LeagueInfo.class, id);
                leagueInfo.getIdLeague();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The leagueInfo with id " + id + " no longer exists.", enfe);
            }
            Test idTest = leagueInfo.getIdTest();
            if (idTest != null) {
                idTest.getLeagueInfoList().remove(leagueInfo);
                idTest = em.merge(idTest);
            }
            List<PlayerLeague> playerLeagueList = leagueInfo.getPlayerLeagueList();
            for (PlayerLeague playerLeagueListPlayerLeague : playerLeagueList) {
                playerLeagueListPlayerLeague.setIdLeague(null);
                playerLeagueListPlayerLeague = em.merge(playerLeagueListPlayerLeague);
            }
            em.remove(leagueInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LeagueInfo> findLeagueInfoEntities() {
        return findLeagueInfoEntities(true, -1, -1);
    }

    public List<LeagueInfo> findLeagueInfoEntities(int maxResults, int firstResult) {
        return findLeagueInfoEntities(false, maxResults, firstResult);
    }

    private List<LeagueInfo> findLeagueInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LeagueInfo.class));
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

    public LeagueInfo findLeagueInfo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LeagueInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getLeagueInfoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LeagueInfo> rt = cq.from(LeagueInfo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
