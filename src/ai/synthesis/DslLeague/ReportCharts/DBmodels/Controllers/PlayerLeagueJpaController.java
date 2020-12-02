/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers;

import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.NonexistentEntityException;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.Controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.LeagueInfo;
import ai.synthesis.DslLeague.ReportCharts.DBmodels.PlayerLeague;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class PlayerLeagueJpaController implements Serializable {
    private int id_cont;
    
    public PlayerLeagueJpaController(EntityManagerFactory emf) {
        this.emf = emf;
        this.id_cont = 1;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlayerLeague playerLeague) throws PreexistingEntityException, Exception {
        playerLeague.setIdPlayer(id_cont);
        this.id_cont++;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LeagueInfo idLeague = playerLeague.getIdLeague();
            if (idLeague != null) {
                idLeague = em.getReference(idLeague.getClass(), idLeague.getIdLeague());
                playerLeague.setIdLeague(idLeague);
            }
            em.persist(playerLeague);
            if (idLeague != null) {
                idLeague.getPlayerLeagueList().add(playerLeague);
                idLeague = em.merge(idLeague);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPlayerLeague(playerLeague.getIdPlayer()) != null) {
                throw new PreexistingEntityException("PlayerLeague " + playerLeague + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlayerLeague playerLeague) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PlayerLeague persistentPlayerLeague = em.find(PlayerLeague.class, playerLeague.getIdPlayer());
            LeagueInfo idLeagueOld = persistentPlayerLeague.getIdLeague();
            LeagueInfo idLeagueNew = playerLeague.getIdLeague();
            if (idLeagueNew != null) {
                idLeagueNew = em.getReference(idLeagueNew.getClass(), idLeagueNew.getIdLeague());
                playerLeague.setIdLeague(idLeagueNew);
            }
            playerLeague = em.merge(playerLeague);
            if (idLeagueOld != null && !idLeagueOld.equals(idLeagueNew)) {
                idLeagueOld.getPlayerLeagueList().remove(playerLeague);
                idLeagueOld = em.merge(idLeagueOld);
            }
            if (idLeagueNew != null && !idLeagueNew.equals(idLeagueOld)) {
                idLeagueNew.getPlayerLeagueList().add(playerLeague);
                idLeagueNew = em.merge(idLeagueNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = playerLeague.getIdPlayer();
                if (findPlayerLeague(id) == null) {
                    throw new NonexistentEntityException("The playerLeague with id " + id + " no longer exists.");
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
            PlayerLeague playerLeague;
            try {
                playerLeague = em.getReference(PlayerLeague.class, id);
                playerLeague.getIdPlayer();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The playerLeague with id " + id + " no longer exists.", enfe);
            }
            LeagueInfo idLeague = playerLeague.getIdLeague();
            if (idLeague != null) {
                idLeague.getPlayerLeagueList().remove(playerLeague);
                idLeague = em.merge(idLeague);
            }
            em.remove(playerLeague);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlayerLeague> findPlayerLeagueEntities() {
        return findPlayerLeagueEntities(true, -1, -1);
    }

    public List<PlayerLeague> findPlayerLeagueEntities(int maxResults, int firstResult) {
        return findPlayerLeagueEntities(false, maxResults, firstResult);
    }

    private List<PlayerLeague> findPlayerLeagueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlayerLeague.class));
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

    public PlayerLeague findPlayerLeague(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlayerLeague.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlayerLeagueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlayerLeague> rt = cq.from(PlayerLeague.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
