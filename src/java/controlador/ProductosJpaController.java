/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.PreexistingEntityException;
import controlador.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Categorias;
import entidad.Peliculas;
import java.util.ArrayList;
import java.util.List;
import entidad.Series;
import entidad.Carritos;
import entidad.DetalleCompra;
import entidad.DetalleRenta;
import entidad.Productos;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author aaron
 */
public class ProductosJpaController implements Serializable {

    public ProductosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productos productos) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (productos.getPeliculasList() == null) {
            productos.setPeliculasList(new ArrayList<Peliculas>());
        }
        if (productos.getSeriesList() == null) {
            productos.setSeriesList(new ArrayList<Series>());
        }
        if (productos.getCarritosList() == null) {
            productos.setCarritosList(new ArrayList<Carritos>());
        }
        if (productos.getDetalleCompraList() == null) {
            productos.setDetalleCompraList(new ArrayList<DetalleCompra>());
        }
        if (productos.getDetalleRentaList() == null) {
            productos.setDetalleRentaList(new ArrayList<DetalleRenta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categorias idCategoria = productos.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                productos.setIdCategoria(idCategoria);
            }
            List<Peliculas> attachedPeliculasList = new ArrayList<Peliculas>();
            for (Peliculas peliculasListPeliculasToAttach : productos.getPeliculasList()) {
                peliculasListPeliculasToAttach = em.getReference(peliculasListPeliculasToAttach.getClass(), peliculasListPeliculasToAttach.getIdPelicula());
                attachedPeliculasList.add(peliculasListPeliculasToAttach);
            }
            productos.setPeliculasList(attachedPeliculasList);
            List<Series> attachedSeriesList = new ArrayList<Series>();
            for (Series seriesListSeriesToAttach : productos.getSeriesList()) {
                seriesListSeriesToAttach = em.getReference(seriesListSeriesToAttach.getClass(), seriesListSeriesToAttach.getIdSerie());
                attachedSeriesList.add(seriesListSeriesToAttach);
            }
            productos.setSeriesList(attachedSeriesList);
            List<Carritos> attachedCarritosList = new ArrayList<Carritos>();
            for (Carritos carritosListCarritosToAttach : productos.getCarritosList()) {
                carritosListCarritosToAttach = em.getReference(carritosListCarritosToAttach.getClass(), carritosListCarritosToAttach.getIdCarrito());
                attachedCarritosList.add(carritosListCarritosToAttach);
            }
            productos.setCarritosList(attachedCarritosList);
            List<DetalleCompra> attachedDetalleCompraList = new ArrayList<DetalleCompra>();
            for (DetalleCompra detalleCompraListDetalleCompraToAttach : productos.getDetalleCompraList()) {
                detalleCompraListDetalleCompraToAttach = em.getReference(detalleCompraListDetalleCompraToAttach.getClass(), detalleCompraListDetalleCompraToAttach.getIdDetalleCompra());
                attachedDetalleCompraList.add(detalleCompraListDetalleCompraToAttach);
            }
            productos.setDetalleCompraList(attachedDetalleCompraList);
            List<DetalleRenta> attachedDetalleRentaList = new ArrayList<DetalleRenta>();
            for (DetalleRenta detalleRentaListDetalleRentaToAttach : productos.getDetalleRentaList()) {
                detalleRentaListDetalleRentaToAttach = em.getReference(detalleRentaListDetalleRentaToAttach.getClass(), detalleRentaListDetalleRentaToAttach.getIdDetalleRenta());
                attachedDetalleRentaList.add(detalleRentaListDetalleRentaToAttach);
            }
            productos.setDetalleRentaList(attachedDetalleRentaList);
            em.persist(productos);
            if (idCategoria != null) {
                idCategoria.getProductosList().add(productos);
                idCategoria = em.merge(idCategoria);
            }
            for (Peliculas peliculasListPeliculas : productos.getPeliculasList()) {
                Productos oldIdProductoOfPeliculasListPeliculas = peliculasListPeliculas.getIdProducto();
                peliculasListPeliculas.setIdProducto(productos);
                peliculasListPeliculas = em.merge(peliculasListPeliculas);
                if (oldIdProductoOfPeliculasListPeliculas != null) {
                    oldIdProductoOfPeliculasListPeliculas.getPeliculasList().remove(peliculasListPeliculas);
                    oldIdProductoOfPeliculasListPeliculas = em.merge(oldIdProductoOfPeliculasListPeliculas);
                }
            }
            for (Series seriesListSeries : productos.getSeriesList()) {
                Productos oldIdProductoOfSeriesListSeries = seriesListSeries.getIdProducto();
                seriesListSeries.setIdProducto(productos);
                seriesListSeries = em.merge(seriesListSeries);
                if (oldIdProductoOfSeriesListSeries != null) {
                    oldIdProductoOfSeriesListSeries.getSeriesList().remove(seriesListSeries);
                    oldIdProductoOfSeriesListSeries = em.merge(oldIdProductoOfSeriesListSeries);
                }
            }
            for (Carritos carritosListCarritos : productos.getCarritosList()) {
                Productos oldIdProductoOfCarritosListCarritos = carritosListCarritos.getIdProducto();
                carritosListCarritos.setIdProducto(productos);
                carritosListCarritos = em.merge(carritosListCarritos);
                if (oldIdProductoOfCarritosListCarritos != null) {
                    oldIdProductoOfCarritosListCarritos.getCarritosList().remove(carritosListCarritos);
                    oldIdProductoOfCarritosListCarritos = em.merge(oldIdProductoOfCarritosListCarritos);
                }
            }
            for (DetalleCompra detalleCompraListDetalleCompra : productos.getDetalleCompraList()) {
                Productos oldIdProductoOfDetalleCompraListDetalleCompra = detalleCompraListDetalleCompra.getIdProducto();
                detalleCompraListDetalleCompra.setIdProducto(productos);
                detalleCompraListDetalleCompra = em.merge(detalleCompraListDetalleCompra);
                if (oldIdProductoOfDetalleCompraListDetalleCompra != null) {
                    oldIdProductoOfDetalleCompraListDetalleCompra.getDetalleCompraList().remove(detalleCompraListDetalleCompra);
                    oldIdProductoOfDetalleCompraListDetalleCompra = em.merge(oldIdProductoOfDetalleCompraListDetalleCompra);
                }
            }
            for (DetalleRenta detalleRentaListDetalleRenta : productos.getDetalleRentaList()) {
                Productos oldIdProductoOfDetalleRentaListDetalleRenta = detalleRentaListDetalleRenta.getIdProducto();
                detalleRentaListDetalleRenta.setIdProducto(productos);
                detalleRentaListDetalleRenta = em.merge(detalleRentaListDetalleRenta);
                if (oldIdProductoOfDetalleRentaListDetalleRenta != null) {
                    oldIdProductoOfDetalleRentaListDetalleRenta.getDetalleRentaList().remove(detalleRentaListDetalleRenta);
                    oldIdProductoOfDetalleRentaListDetalleRenta = em.merge(oldIdProductoOfDetalleRentaListDetalleRenta);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProductos(productos.getIdProducto()) != null) {
                throw new PreexistingEntityException("Productos " + productos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productos productos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Productos persistentProductos = em.find(Productos.class, productos.getIdProducto());
            Categorias idCategoriaOld = persistentProductos.getIdCategoria();
            Categorias idCategoriaNew = productos.getIdCategoria();
            List<Peliculas> peliculasListOld = persistentProductos.getPeliculasList();
            List<Peliculas> peliculasListNew = productos.getPeliculasList();
            List<Series> seriesListOld = persistentProductos.getSeriesList();
            List<Series> seriesListNew = productos.getSeriesList();
            List<Carritos> carritosListOld = persistentProductos.getCarritosList();
            List<Carritos> carritosListNew = productos.getCarritosList();
            List<DetalleCompra> detalleCompraListOld = persistentProductos.getDetalleCompraList();
            List<DetalleCompra> detalleCompraListNew = productos.getDetalleCompraList();
            List<DetalleRenta> detalleRentaListOld = persistentProductos.getDetalleRentaList();
            List<DetalleRenta> detalleRentaListNew = productos.getDetalleRentaList();
            List<String> illegalOrphanMessages = null;
            for (Peliculas peliculasListOldPeliculas : peliculasListOld) {
                if (!peliculasListNew.contains(peliculasListOldPeliculas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Peliculas " + peliculasListOldPeliculas + " since its idProducto field is not nullable.");
                }
            }
            for (Series seriesListOldSeries : seriesListOld) {
                if (!seriesListNew.contains(seriesListOldSeries)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Series " + seriesListOldSeries + " since its idProducto field is not nullable.");
                }
            }
            for (Carritos carritosListOldCarritos : carritosListOld) {
                if (!carritosListNew.contains(carritosListOldCarritos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carritos " + carritosListOldCarritos + " since its idProducto field is not nullable.");
                }
            }
            for (DetalleCompra detalleCompraListOldDetalleCompra : detalleCompraListOld) {
                if (!detalleCompraListNew.contains(detalleCompraListOldDetalleCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleCompra " + detalleCompraListOldDetalleCompra + " since its idProducto field is not nullable.");
                }
            }
            for (DetalleRenta detalleRentaListOldDetalleRenta : detalleRentaListOld) {
                if (!detalleRentaListNew.contains(detalleRentaListOldDetalleRenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleRenta " + detalleRentaListOldDetalleRenta + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                productos.setIdCategoria(idCategoriaNew);
            }
            List<Peliculas> attachedPeliculasListNew = new ArrayList<Peliculas>();
            for (Peliculas peliculasListNewPeliculasToAttach : peliculasListNew) {
                peliculasListNewPeliculasToAttach = em.getReference(peliculasListNewPeliculasToAttach.getClass(), peliculasListNewPeliculasToAttach.getIdPelicula());
                attachedPeliculasListNew.add(peliculasListNewPeliculasToAttach);
            }
            peliculasListNew = attachedPeliculasListNew;
            productos.setPeliculasList(peliculasListNew);
            List<Series> attachedSeriesListNew = new ArrayList<Series>();
            for (Series seriesListNewSeriesToAttach : seriesListNew) {
                seriesListNewSeriesToAttach = em.getReference(seriesListNewSeriesToAttach.getClass(), seriesListNewSeriesToAttach.getIdSerie());
                attachedSeriesListNew.add(seriesListNewSeriesToAttach);
            }
            seriesListNew = attachedSeriesListNew;
            productos.setSeriesList(seriesListNew);
            List<Carritos> attachedCarritosListNew = new ArrayList<Carritos>();
            for (Carritos carritosListNewCarritosToAttach : carritosListNew) {
                carritosListNewCarritosToAttach = em.getReference(carritosListNewCarritosToAttach.getClass(), carritosListNewCarritosToAttach.getIdCarrito());
                attachedCarritosListNew.add(carritosListNewCarritosToAttach);
            }
            carritosListNew = attachedCarritosListNew;
            productos.setCarritosList(carritosListNew);
            List<DetalleCompra> attachedDetalleCompraListNew = new ArrayList<DetalleCompra>();
            for (DetalleCompra detalleCompraListNewDetalleCompraToAttach : detalleCompraListNew) {
                detalleCompraListNewDetalleCompraToAttach = em.getReference(detalleCompraListNewDetalleCompraToAttach.getClass(), detalleCompraListNewDetalleCompraToAttach.getIdDetalleCompra());
                attachedDetalleCompraListNew.add(detalleCompraListNewDetalleCompraToAttach);
            }
            detalleCompraListNew = attachedDetalleCompraListNew;
            productos.setDetalleCompraList(detalleCompraListNew);
            List<DetalleRenta> attachedDetalleRentaListNew = new ArrayList<DetalleRenta>();
            for (DetalleRenta detalleRentaListNewDetalleRentaToAttach : detalleRentaListNew) {
                detalleRentaListNewDetalleRentaToAttach = em.getReference(detalleRentaListNewDetalleRentaToAttach.getClass(), detalleRentaListNewDetalleRentaToAttach.getIdDetalleRenta());
                attachedDetalleRentaListNew.add(detalleRentaListNewDetalleRentaToAttach);
            }
            detalleRentaListNew = attachedDetalleRentaListNew;
            productos.setDetalleRentaList(detalleRentaListNew);
            productos = em.merge(productos);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getProductosList().remove(productos);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getProductosList().add(productos);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Peliculas peliculasListNewPeliculas : peliculasListNew) {
                if (!peliculasListOld.contains(peliculasListNewPeliculas)) {
                    Productos oldIdProductoOfPeliculasListNewPeliculas = peliculasListNewPeliculas.getIdProducto();
                    peliculasListNewPeliculas.setIdProducto(productos);
                    peliculasListNewPeliculas = em.merge(peliculasListNewPeliculas);
                    if (oldIdProductoOfPeliculasListNewPeliculas != null && !oldIdProductoOfPeliculasListNewPeliculas.equals(productos)) {
                        oldIdProductoOfPeliculasListNewPeliculas.getPeliculasList().remove(peliculasListNewPeliculas);
                        oldIdProductoOfPeliculasListNewPeliculas = em.merge(oldIdProductoOfPeliculasListNewPeliculas);
                    }
                }
            }
            for (Series seriesListNewSeries : seriesListNew) {
                if (!seriesListOld.contains(seriesListNewSeries)) {
                    Productos oldIdProductoOfSeriesListNewSeries = seriesListNewSeries.getIdProducto();
                    seriesListNewSeries.setIdProducto(productos);
                    seriesListNewSeries = em.merge(seriesListNewSeries);
                    if (oldIdProductoOfSeriesListNewSeries != null && !oldIdProductoOfSeriesListNewSeries.equals(productos)) {
                        oldIdProductoOfSeriesListNewSeries.getSeriesList().remove(seriesListNewSeries);
                        oldIdProductoOfSeriesListNewSeries = em.merge(oldIdProductoOfSeriesListNewSeries);
                    }
                }
            }
            for (Carritos carritosListNewCarritos : carritosListNew) {
                if (!carritosListOld.contains(carritosListNewCarritos)) {
                    Productos oldIdProductoOfCarritosListNewCarritos = carritosListNewCarritos.getIdProducto();
                    carritosListNewCarritos.setIdProducto(productos);
                    carritosListNewCarritos = em.merge(carritosListNewCarritos);
                    if (oldIdProductoOfCarritosListNewCarritos != null && !oldIdProductoOfCarritosListNewCarritos.equals(productos)) {
                        oldIdProductoOfCarritosListNewCarritos.getCarritosList().remove(carritosListNewCarritos);
                        oldIdProductoOfCarritosListNewCarritos = em.merge(oldIdProductoOfCarritosListNewCarritos);
                    }
                }
            }
            for (DetalleCompra detalleCompraListNewDetalleCompra : detalleCompraListNew) {
                if (!detalleCompraListOld.contains(detalleCompraListNewDetalleCompra)) {
                    Productos oldIdProductoOfDetalleCompraListNewDetalleCompra = detalleCompraListNewDetalleCompra.getIdProducto();
                    detalleCompraListNewDetalleCompra.setIdProducto(productos);
                    detalleCompraListNewDetalleCompra = em.merge(detalleCompraListNewDetalleCompra);
                    if (oldIdProductoOfDetalleCompraListNewDetalleCompra != null && !oldIdProductoOfDetalleCompraListNewDetalleCompra.equals(productos)) {
                        oldIdProductoOfDetalleCompraListNewDetalleCompra.getDetalleCompraList().remove(detalleCompraListNewDetalleCompra);
                        oldIdProductoOfDetalleCompraListNewDetalleCompra = em.merge(oldIdProductoOfDetalleCompraListNewDetalleCompra);
                    }
                }
            }
            for (DetalleRenta detalleRentaListNewDetalleRenta : detalleRentaListNew) {
                if (!detalleRentaListOld.contains(detalleRentaListNewDetalleRenta)) {
                    Productos oldIdProductoOfDetalleRentaListNewDetalleRenta = detalleRentaListNewDetalleRenta.getIdProducto();
                    detalleRentaListNewDetalleRenta.setIdProducto(productos);
                    detalleRentaListNewDetalleRenta = em.merge(detalleRentaListNewDetalleRenta);
                    if (oldIdProductoOfDetalleRentaListNewDetalleRenta != null && !oldIdProductoOfDetalleRentaListNewDetalleRenta.equals(productos)) {
                        oldIdProductoOfDetalleRentaListNewDetalleRenta.getDetalleRentaList().remove(detalleRentaListNewDetalleRenta);
                        oldIdProductoOfDetalleRentaListNewDetalleRenta = em.merge(oldIdProductoOfDetalleRentaListNewDetalleRenta);
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
                Integer id = productos.getIdProducto();
                if (findProductos(id) == null) {
                    throw new NonexistentEntityException("The productos with id " + id + " no longer exists.");
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
            Productos productos;
            try {
                productos = em.getReference(Productos.class, id);
                productos.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Peliculas> peliculasListOrphanCheck = productos.getPeliculasList();
            for (Peliculas peliculasListOrphanCheckPeliculas : peliculasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the Peliculas " + peliculasListOrphanCheckPeliculas + " in its peliculasList field has a non-nullable idProducto field.");
            }
            List<Series> seriesListOrphanCheck = productos.getSeriesList();
            for (Series seriesListOrphanCheckSeries : seriesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the Series " + seriesListOrphanCheckSeries + " in its seriesList field has a non-nullable idProducto field.");
            }
            List<Carritos> carritosListOrphanCheck = productos.getCarritosList();
            for (Carritos carritosListOrphanCheckCarritos : carritosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the Carritos " + carritosListOrphanCheckCarritos + " in its carritosList field has a non-nullable idProducto field.");
            }
            List<DetalleCompra> detalleCompraListOrphanCheck = productos.getDetalleCompraList();
            for (DetalleCompra detalleCompraListOrphanCheckDetalleCompra : detalleCompraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the DetalleCompra " + detalleCompraListOrphanCheckDetalleCompra + " in its detalleCompraList field has a non-nullable idProducto field.");
            }
            List<DetalleRenta> detalleRentaListOrphanCheck = productos.getDetalleRentaList();
            for (DetalleRenta detalleRentaListOrphanCheckDetalleRenta : detalleRentaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the DetalleRenta " + detalleRentaListOrphanCheckDetalleRenta + " in its detalleRentaList field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categorias idCategoria = productos.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getProductosList().remove(productos);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(productos);
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

    public List<Productos> findProductosEntities() {
        return findProductosEntities(true, -1, -1);
    }

    public List<Productos> findProductosEntities(int maxResults, int firstResult) {
        return findProductosEntities(false, maxResults, firstResult);
    }

    private List<Productos> findProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productos.class));
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

    public Productos findProductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productos> rt = cq.from(Productos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
