/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Usuarios;
import entidad.DetalleRenta;
import entidad.Rentas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class RentasJpaController implements Serializable {

    public RentasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rentas rentas) throws RollbackFailureException, Exception {
        if (rentas.getDetalleRentaList() == null) {
            rentas.setDetalleRentaList(new ArrayList<DetalleRenta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios idUsuario = rentas.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                rentas.setIdUsuario(idUsuario);
            }
            List<DetalleRenta> attachedDetalleRentaList = new ArrayList<DetalleRenta>();
            for (DetalleRenta detalleRentaListDetalleRentaToAttach : rentas.getDetalleRentaList()) {
                detalleRentaListDetalleRentaToAttach = em.getReference(detalleRentaListDetalleRentaToAttach.getClass(), detalleRentaListDetalleRentaToAttach.getIdDetalleRenta());
                attachedDetalleRentaList.add(detalleRentaListDetalleRentaToAttach);
            }
            rentas.setDetalleRentaList(attachedDetalleRentaList);
            em.persist(rentas);
            if (idUsuario != null) {
                idUsuario.getRentasList().add(rentas);
                idUsuario = em.merge(idUsuario);
            }
            for (DetalleRenta detalleRentaListDetalleRenta : rentas.getDetalleRentaList()) {
                Rentas oldIdRentaOfDetalleRentaListDetalleRenta = detalleRentaListDetalleRenta.getIdRenta();
                detalleRentaListDetalleRenta.setIdRenta(rentas);
                detalleRentaListDetalleRenta = em.merge(detalleRentaListDetalleRenta);
                if (oldIdRentaOfDetalleRentaListDetalleRenta != null) {
                    oldIdRentaOfDetalleRentaListDetalleRenta.getDetalleRentaList().remove(detalleRentaListDetalleRenta);
                    oldIdRentaOfDetalleRentaListDetalleRenta = em.merge(oldIdRentaOfDetalleRentaListDetalleRenta);
                }
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

    public void edit(Rentas rentas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rentas persistentRentas = em.find(Rentas.class, rentas.getIdRenta());
            Usuarios idUsuarioOld = persistentRentas.getIdUsuario();
            Usuarios idUsuarioNew = rentas.getIdUsuario();
            List<DetalleRenta> detalleRentaListOld = persistentRentas.getDetalleRentaList();
            List<DetalleRenta> detalleRentaListNew = rentas.getDetalleRentaList();
            List<String> illegalOrphanMessages = null;
            for (DetalleRenta detalleRentaListOldDetalleRenta : detalleRentaListOld) {
                if (!detalleRentaListNew.contains(detalleRentaListOldDetalleRenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleRenta " + detalleRentaListOldDetalleRenta + " since its idRenta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                rentas.setIdUsuario(idUsuarioNew);
            }
            List<DetalleRenta> attachedDetalleRentaListNew = new ArrayList<DetalleRenta>();
            for (DetalleRenta detalleRentaListNewDetalleRentaToAttach : detalleRentaListNew) {
                detalleRentaListNewDetalleRentaToAttach = em.getReference(detalleRentaListNewDetalleRentaToAttach.getClass(), detalleRentaListNewDetalleRentaToAttach.getIdDetalleRenta());
                attachedDetalleRentaListNew.add(detalleRentaListNewDetalleRentaToAttach);
            }
            detalleRentaListNew = attachedDetalleRentaListNew;
            rentas.setDetalleRentaList(detalleRentaListNew);
            rentas = em.merge(rentas);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getRentasList().remove(rentas);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getRentasList().add(rentas);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (DetalleRenta detalleRentaListNewDetalleRenta : detalleRentaListNew) {
                if (!detalleRentaListOld.contains(detalleRentaListNewDetalleRenta)) {
                    Rentas oldIdRentaOfDetalleRentaListNewDetalleRenta = detalleRentaListNewDetalleRenta.getIdRenta();
                    detalleRentaListNewDetalleRenta.setIdRenta(rentas);
                    detalleRentaListNewDetalleRenta = em.merge(detalleRentaListNewDetalleRenta);
                    if (oldIdRentaOfDetalleRentaListNewDetalleRenta != null && !oldIdRentaOfDetalleRentaListNewDetalleRenta.equals(rentas)) {
                        oldIdRentaOfDetalleRentaListNewDetalleRenta.getDetalleRentaList().remove(detalleRentaListNewDetalleRenta);
                        oldIdRentaOfDetalleRentaListNewDetalleRenta = em.merge(oldIdRentaOfDetalleRentaListNewDetalleRenta);
                    }
                }
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
                Integer id = rentas.getIdRenta();
                if (findRentas(id) == null) {
                    throw new NonexistentEntityException("The rentas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Rentas rentas;
            try {
                rentas = em.getReference(Rentas.class, id);
                rentas.getIdRenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rentas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleRenta> detalleRentaListOrphanCheck = rentas.getDetalleRentaList();
            for (DetalleRenta detalleRentaListOrphanCheckDetalleRenta : detalleRentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Rentas (" + rentas + ") cannot be destroyed since the DetalleRenta " + detalleRentaListOrphanCheckDetalleRenta + " in its detalleRentaList field has a non-nullable idRenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios idUsuario = rentas.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getRentasList().remove(rentas);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(rentas);
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

    public List<Rentas> findRentasEntities() {
        return findRentasEntities(true, -1, -1);
    }

    public List<Rentas> findRentasEntities(int maxResults, int firstResult) {
        return findRentasEntities(false, maxResults, firstResult);
    }

    private List<Rentas> findRentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rentas.class));
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

    public Rentas findRentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getRentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rentas> rt = cq.from(Rentas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
