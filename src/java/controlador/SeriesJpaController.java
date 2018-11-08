/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Productos;
import entidad.Series;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class SeriesJpaController implements Serializable {

    public SeriesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Series series) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Productos idProducto = series.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                series.setIdProducto(idProducto);
            }
            em.persist(series);
            if (idProducto != null) {
                idProducto.getSeriesList().add(series);
                idProducto = em.merge(idProducto);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Series series) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Series persistentSeries = em.find(Series.class, series.getIdSerie());
            Productos idProductoOld = persistentSeries.getIdProducto();
            Productos idProductoNew = series.getIdProducto();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                series.setIdProducto(idProductoNew);
            }
            series = em.merge(series);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getSeriesList().remove(series);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getSeriesList().add(series);
                idProductoNew = em.merge(idProductoNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = series.getIdSerie();
                if (findSeries(id) == null) {
                    throw new NonexistentEntityException("The series with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Series series;
            try {
                series = em.getReference(Series.class, id);
                series.getIdSerie();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The series with id " + id + " no longer exists.", enfe);
            }
            Productos idProducto = series.getIdProducto();
            if (idProducto != null) {
                idProducto.getSeriesList().remove(series);
                idProducto = em.merge(idProducto);
            }
            em.remove(series);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Series> findSeriesEntities() {
        return findSeriesEntities(true, -1, -1);
    }

    public List<Series> findSeriesEntities(int maxResults, int firstResult) {
        return findSeriesEntities(false, maxResults, firstResult);
    }

    private List<Series> findSeriesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Series.class));
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

    public Series findSeries(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Series.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeriesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Series> rt = cq.from(Series.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
