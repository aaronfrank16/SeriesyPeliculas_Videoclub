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
import entidad.Rentas;
import java.util.ArrayList;
import java.util.List;
import entidad.Compras;
import entidad.Usuarios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws RollbackFailureException, Exception {
        if (usuarios.getRentasList() == null) {
            usuarios.setRentasList(new ArrayList<Rentas>());
        }
        if (usuarios.getComprasList() == null) {
            usuarios.setComprasList(new ArrayList<Compras>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            utx = em.getTransaction();
            utx.begin();
            List<Rentas> attachedRentasList = new ArrayList<Rentas>();
            for (Rentas rentasListRentasToAttach : usuarios.getRentasList()) {
                rentasListRentasToAttach = em.getReference(rentasListRentasToAttach.getClass(), rentasListRentasToAttach.getIdRenta());
                attachedRentasList.add(rentasListRentasToAttach);
            }
            usuarios.setRentasList(attachedRentasList);
            List<Compras> attachedComprasList = new ArrayList<Compras>();
            for (Compras comprasListComprasToAttach : usuarios.getComprasList()) {
                comprasListComprasToAttach = em.getReference(comprasListComprasToAttach.getClass(), comprasListComprasToAttach.getIdCompra());
                attachedComprasList.add(comprasListComprasToAttach);
            }
            usuarios.setComprasList(attachedComprasList);
            em.persist(usuarios);
            for (Rentas rentasListRentas : usuarios.getRentasList()) {
                Usuarios oldIdUsuarioOfRentasListRentas = rentasListRentas.getIdUsuario();
                rentasListRentas.setIdUsuario(usuarios);
                rentasListRentas = em.merge(rentasListRentas);
                if (oldIdUsuarioOfRentasListRentas != null) {
                    oldIdUsuarioOfRentasListRentas.getRentasList().remove(rentasListRentas);
                    oldIdUsuarioOfRentasListRentas = em.merge(oldIdUsuarioOfRentasListRentas);
                }
            }
            for (Compras comprasListCompras : usuarios.getComprasList()) {
                Usuarios oldIdUsuarioOfComprasListCompras = comprasListCompras.getIdUsuario();
                comprasListCompras.setIdUsuario(usuarios);
                comprasListCompras = em.merge(comprasListCompras);
                if (oldIdUsuarioOfComprasListCompras != null) {
                    oldIdUsuarioOfComprasListCompras.getComprasList().remove(comprasListCompras);
                    oldIdUsuarioOfComprasListCompras = em.merge(oldIdUsuarioOfComprasListCompras);
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

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuario());
            List<Rentas> rentasListOld = persistentUsuarios.getRentasList();
            List<Rentas> rentasListNew = usuarios.getRentasList();
            List<Compras> comprasListOld = persistentUsuarios.getComprasList();
            List<Compras> comprasListNew = usuarios.getComprasList();
            List<String> illegalOrphanMessages = null;
            for (Rentas rentasListOldRentas : rentasListOld) {
                if (!rentasListNew.contains(rentasListOldRentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Rentas " + rentasListOldRentas + " since its idUsuario field is not nullable.");
                }
            }
            for (Compras comprasListOldCompras : comprasListOld) {
                if (!comprasListNew.contains(comprasListOldCompras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compras " + comprasListOldCompras + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Rentas> attachedRentasListNew = new ArrayList<Rentas>();
            for (Rentas rentasListNewRentasToAttach : rentasListNew) {
                rentasListNewRentasToAttach = em.getReference(rentasListNewRentasToAttach.getClass(), rentasListNewRentasToAttach.getIdRenta());
                attachedRentasListNew.add(rentasListNewRentasToAttach);
            }
            rentasListNew = attachedRentasListNew;
            usuarios.setRentasList(rentasListNew);
            List<Compras> attachedComprasListNew = new ArrayList<Compras>();
            for (Compras comprasListNewComprasToAttach : comprasListNew) {
                comprasListNewComprasToAttach = em.getReference(comprasListNewComprasToAttach.getClass(), comprasListNewComprasToAttach.getIdCompra());
                attachedComprasListNew.add(comprasListNewComprasToAttach);
            }
            comprasListNew = attachedComprasListNew;
            usuarios.setComprasList(comprasListNew);
            usuarios = em.merge(usuarios);
            for (Rentas rentasListNewRentas : rentasListNew) {
                if (!rentasListOld.contains(rentasListNewRentas)) {
                    Usuarios oldIdUsuarioOfRentasListNewRentas = rentasListNewRentas.getIdUsuario();
                    rentasListNewRentas.setIdUsuario(usuarios);
                    rentasListNewRentas = em.merge(rentasListNewRentas);
                    if (oldIdUsuarioOfRentasListNewRentas != null && !oldIdUsuarioOfRentasListNewRentas.equals(usuarios)) {
                        oldIdUsuarioOfRentasListNewRentas.getRentasList().remove(rentasListNewRentas);
                        oldIdUsuarioOfRentasListNewRentas = em.merge(oldIdUsuarioOfRentasListNewRentas);
                    }
                }
            }
            for (Compras comprasListNewCompras : comprasListNew) {
                if (!comprasListOld.contains(comprasListNewCompras)) {
                    Usuarios oldIdUsuarioOfComprasListNewCompras = comprasListNewCompras.getIdUsuario();
                    comprasListNewCompras.setIdUsuario(usuarios);
                    comprasListNewCompras = em.merge(comprasListNewCompras);
                    if (oldIdUsuarioOfComprasListNewCompras != null && !oldIdUsuarioOfComprasListNewCompras.equals(usuarios)) {
                        oldIdUsuarioOfComprasListNewCompras.getComprasList().remove(comprasListNewCompras);
                        oldIdUsuarioOfComprasListNewCompras = em.merge(oldIdUsuarioOfComprasListNewCompras);
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
                Integer id = usuarios.getIdUsuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Rentas> rentasListOrphanCheck = usuarios.getRentasList();
            for (Rentas rentasListOrphanCheckRentas : rentasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Rentas " + rentasListOrphanCheckRentas + " in its rentasList field has a non-nullable idUsuario field.");
            }
            List<Compras> comprasListOrphanCheck = usuarios.getComprasList();
            for (Compras comprasListOrphanCheckCompras : comprasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Compras " + comprasListOrphanCheckCompras + " in its comprasList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarios);
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

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Usuarios findByCorreo(String login){
        Usuarios user = null;
        List<Usuarios> users;
        EntityManager em = getEntityManager();
        System.out.println("Buscando usuario por email jpacontroller");
        Query consulta = em.createNamedQuery("Usuario.findByCorreo");
        consulta.setParameter("correo", login);
        users=consulta.getResultList();
        if (!users.isEmpty()) {
            System.out.println("Lo encontro");
            System.out.println("users.get[0]: "+users.get(0));
            user = users.get(0);
        }
        return user;
    }
    
}
