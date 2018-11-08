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
import entidad.Compras;
import entidad.DetalleCompra;
import entidad.Productos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class DetalleCompraJpaController implements Serializable {

    public DetalleCompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleCompra detalleCompra) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Compras idCompra = detalleCompra.getIdCompra();
            if (idCompra != null) {
                idCompra = em.getReference(idCompra.getClass(), idCompra.getIdCompra());
                detalleCompra.setIdCompra(idCompra);
            }
            Productos idProducto = detalleCompra.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                detalleCompra.setIdProducto(idProducto);
            }
            em.persist(detalleCompra);
            if (idCompra != null) {
                idCompra.getDetalleCompraList().add(detalleCompra);
                idCompra = em.merge(idCompra);
            }
            if (idProducto != null) {
                idProducto.getDetalleCompraList().add(detalleCompra);
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

    public void edit(DetalleCompra detalleCompra) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetalleCompra persistentDetalleCompra = em.find(DetalleCompra.class, detalleCompra.getIdDetalleCompra());
            Compras idCompraOld = persistentDetalleCompra.getIdCompra();
            Compras idCompraNew = detalleCompra.getIdCompra();
            Productos idProductoOld = persistentDetalleCompra.getIdProducto();
            Productos idProductoNew = detalleCompra.getIdProducto();
            if (idCompraNew != null) {
                idCompraNew = em.getReference(idCompraNew.getClass(), idCompraNew.getIdCompra());
                detalleCompra.setIdCompra(idCompraNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                detalleCompra.setIdProducto(idProductoNew);
            }
            detalleCompra = em.merge(detalleCompra);
            if (idCompraOld != null && !idCompraOld.equals(idCompraNew)) {
                idCompraOld.getDetalleCompraList().remove(detalleCompra);
                idCompraOld = em.merge(idCompraOld);
            }
            if (idCompraNew != null && !idCompraNew.equals(idCompraOld)) {
                idCompraNew.getDetalleCompraList().add(detalleCompra);
                idCompraNew = em.merge(idCompraNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getDetalleCompraList().remove(detalleCompra);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getDetalleCompraList().add(detalleCompra);
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
                Integer id = detalleCompra.getIdDetalleCompra();
                if (findDetalleCompra(id) == null) {
                    throw new NonexistentEntityException("The detalleCompra with id " + id + " no longer exists.");
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
            DetalleCompra detalleCompra;
            try {
                detalleCompra = em.getReference(DetalleCompra.class, id);
                detalleCompra.getIdDetalleCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleCompra with id " + id + " no longer exists.", enfe);
            }
            Compras idCompra = detalleCompra.getIdCompra();
            if (idCompra != null) {
                idCompra.getDetalleCompraList().remove(detalleCompra);
                idCompra = em.merge(idCompra);
            }
            Productos idProducto = detalleCompra.getIdProducto();
            if (idProducto != null) {
                idProducto.getDetalleCompraList().remove(detalleCompra);
                idProducto = em.merge(idProducto);
            }
            em.remove(detalleCompra);
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

    public List<DetalleCompra> findDetalleCompraEntities() {
        return findDetalleCompraEntities(true, -1, -1);
    }

    public List<DetalleCompra> findDetalleCompraEntities(int maxResults, int firstResult) {
        return findDetalleCompraEntities(false, maxResults, firstResult);
    }

    private List<DetalleCompra> findDetalleCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleCompra.class));
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

    public DetalleCompra findDetalleCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleCompra.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleCompra> rt = cq.from(DetalleCompra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
