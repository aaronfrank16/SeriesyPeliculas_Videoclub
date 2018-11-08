
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.Carritos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Productos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

public class CarritosJpaController implements Serializable {

    public CarritosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carritos carritos) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Productos idProducto = carritos.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                carritos.setIdProducto(idProducto);
            }
            em.persist(carritos);
            if (idProducto != null) {
                idProducto.getCarritosList().add(carritos);
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

    public void edit(Carritos carritos) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Carritos persistentCarritos = em.find(Carritos.class, carritos.getIdCarrito());
            Productos idProductoOld = persistentCarritos.getIdProducto();
            Productos idProductoNew = carritos.getIdProducto();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                carritos.setIdProducto(idProductoNew);
            }
            carritos = em.merge(carritos);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getCarritosList().remove(carritos);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getCarritosList().add(carritos);
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
                Integer id = carritos.getIdCarrito();
                if (findCarritos(id) == null) {
                    throw new NonexistentEntityException("The carritos with id " + id + " no longer exists.");
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
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            Carritos carritos;
            try {
                carritos = em.getReference(Carritos.class, id);
                carritos.getIdCarrito();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carritos with id " + id + " no longer exists.", enfe);
            }
            Productos idProducto = carritos.getIdProducto();
            if (idProducto != null) {
                idProducto.getCarritosList().remove(carritos);
                idProducto = em.merge(idProducto);
            }
            em.remove(carritos);
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

    public List<Carritos> findCarritosEntities() {
        return findCarritosEntities(true, -1, -1);
    }

    public List<Carritos> findCarritosEntities(int maxResults, int firstResult) {
        return findCarritosEntities(false, maxResults, firstResult);
    }

    private List<Carritos> findCarritosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carritos.class));
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

    public Carritos findCarritos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carritos.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarritosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carritos> rt = cq.from(Carritos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
