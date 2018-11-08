/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import entidad.Peliculas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Productos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class PeliculasJpaController implements Serializable {

    public PeliculasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Peliculas peliculas) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Productos idProducto = peliculas.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                peliculas.setIdProducto(idProducto);
            }
            em.persist(peliculas);
            if (idProducto != null) {
                idProducto.getPeliculasList().add(peliculas);
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

    public void edit(Peliculas peliculas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Peliculas persistentPeliculas = em.find(Peliculas.class, peliculas.getIdPelicula());
            Productos idProductoOld = persistentPeliculas.getIdProducto();
            Productos idProductoNew = peliculas.getIdProducto();
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                peliculas.setIdProducto(idProductoNew);
            }
            peliculas = em.merge(peliculas);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getPeliculasList().remove(peliculas);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getPeliculasList().add(peliculas);
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
                Integer id = peliculas.getIdPelicula();
                if (findPeliculas(id) == null) {
                    throw new NonexistentEntityException("The peliculas with id " + id + " no longer exists.");
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
            Peliculas peliculas;
            try {
                peliculas = em.getReference(Peliculas.class, id);
                peliculas.getIdPelicula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peliculas with id " + id + " no longer exists.", enfe);
            }
            Productos idProducto = peliculas.getIdProducto();
            if (idProducto != null) {
                idProducto.getPeliculasList().remove(peliculas);
                idProducto = em.merge(idProducto);
            }
            em.remove(peliculas);
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

    public List<Peliculas> findPeliculasEntities() {
        return findPeliculasEntities(true, -1, -1);
    }

    public List<Peliculas> findPeliculasEntities(int maxResults, int firstResult) {
        return findPeliculasEntities(false, maxResults, firstResult);
    }

    private List<Peliculas> findPeliculasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Peliculas.class));
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

    public Peliculas findPeliculas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Peliculas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeliculasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Peliculas> rt = cq.from(Peliculas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
