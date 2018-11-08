/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.DetalleRenta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Rentas;
import entidad.Productos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class DetalleRentaJpaController implements Serializable {

    public DetalleRentaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleRenta detalleRenta) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rentas idRenta = detalleRenta.getIdRenta();
            if (idRenta != null) {
                idRenta = em.getReference(idRenta.getClass(), idRenta.getIdRenta());
                detalleRenta.setIdRenta(idRenta);
            }
            Productos idProducto = detalleRenta.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                detalleRenta.setIdProducto(idProducto);
            }
            em.persist(detalleRenta);
            if (idRenta != null) {
                idRenta.getDetalleRentaList().add(detalleRenta);
                idRenta = em.merge(idRenta);
            }
            if (idProducto != null) {
                idProducto.getDetalleRentaList().add(detalleRenta);
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

    public void edit(DetalleRenta detalleRenta) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetalleRenta persistentDetalleRenta = em.find(DetalleRenta.class, detalleRenta.getIdDetalleRenta());
            Rentas idRentaOld = persistentDetalleRenta.getIdRenta();
            Rentas idRentaNew = detalleRenta.getIdRenta();
            Productos idProductoOld = persistentDetalleRenta.getIdProducto();
            Productos idProductoNew = detalleRenta.getIdProducto();
            if (idRentaNew != null) {
                idRentaNew = em.getReference(idRentaNew.getClass(), idRentaNew.getIdRenta());
                detalleRenta.setIdRenta(idRentaNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                detalleRenta.setIdProducto(idProductoNew);
            }
            detalleRenta = em.merge(detalleRenta);
            if (idRentaOld != null && !idRentaOld.equals(idRentaNew)) {
                idRentaOld.getDetalleRentaList().remove(detalleRenta);
                idRentaOld = em.merge(idRentaOld);
            }
            if (idRentaNew != null && !idRentaNew.equals(idRentaOld)) {
                idRentaNew.getDetalleRentaList().add(detalleRenta);
                idRentaNew = em.merge(idRentaNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getDetalleRentaList().remove(detalleRenta);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getDetalleRentaList().add(detalleRenta);
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
                Integer id = detalleRenta.getIdDetalleRenta();
                if (findDetalleRenta(id) == null) {
                    throw new NonexistentEntityException("The detalleRenta with id " + id + " no longer exists.");
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
            DetalleRenta detalleRenta;
            try {
                detalleRenta = em.getReference(DetalleRenta.class, id);
                detalleRenta.getIdDetalleRenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleRenta with id " + id + " no longer exists.", enfe);
            }
            Rentas idRenta = detalleRenta.getIdRenta();
            if (idRenta != null) {
                idRenta.getDetalleRentaList().remove(detalleRenta);
                idRenta = em.merge(idRenta);
            }
            Productos idProducto = detalleRenta.getIdProducto();
            if (idProducto != null) {
                idProducto.getDetalleRentaList().remove(detalleRenta);
                idProducto = em.merge(idProducto);
            }
            em.remove(detalleRenta);
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

    public List<DetalleRenta> findDetalleRentaEntities() {
        return findDetalleRentaEntities(true, -1, -1);
    }

    public List<DetalleRenta> findDetalleRentaEntities(int maxResults, int firstResult) {
        return findDetalleRentaEntities(false, maxResults, firstResult);
    }

    private List<DetalleRenta> findDetalleRentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleRenta.class));
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

    public DetalleRenta findDetalleRenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleRenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleRentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleRenta> rt = cq.from(DetalleRenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
