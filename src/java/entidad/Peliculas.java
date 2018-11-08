/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "peliculas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Peliculas.findAll", query = "SELECT p FROM Peliculas p")
    , @NamedQuery(name = "Peliculas.findByIdPelicula", query = "SELECT p FROM Peliculas p WHERE p.idPelicula = :idPelicula")
    , @NamedQuery(name = "Peliculas.findByDirector", query = "SELECT p FROM Peliculas p WHERE p.director = :director")
    , @NamedQuery(name = "Peliculas.findByDuracion", query = "SELECT p FROM Peliculas p WHERE p.duracion = :duracion")
    , @NamedQuery(name = "Peliculas.findByClasificacion", query = "SELECT p FROM Peliculas p WHERE p.clasificacion = :clasificacion")
    , @NamedQuery(name = "Peliculas.findByCantidadAlmacen", query = "SELECT p FROM Peliculas p WHERE p.cantidadAlmacen = :cantidadAlmacen")
    , @NamedQuery(name = "Peliculas.findByCantidadRenta", query = "SELECT p FROM Peliculas p WHERE p.cantidadRenta = :cantidadRenta")
    , @NamedQuery(name = "Peliculas.findByPrecioCompra", query = "SELECT p FROM Peliculas p WHERE p.precioCompra = :precioCompra")
    , @NamedQuery(name = "Peliculas.findByPrecioRenta", query = "SELECT p FROM Peliculas p WHERE p.precioRenta = :precioRenta")})
public class Peliculas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPelicula")
    private Integer idPelicula;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "director")
    private String director;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "duracion")
    private String duracion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "clasificacion")
    private String clasificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_almacen")
    private int cantidadAlmacen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_renta")
    private int cantidadRenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_compra")
    private double precioCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_renta")
    private double precioRenta;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Productos idProducto;

    public Peliculas() {
    }

    public Peliculas(Integer idPelicula) {
        this.idPelicula = idPelicula;
    }

    public Peliculas(Integer idPelicula, String director, String duracion, String clasificacion, int cantidadAlmacen, int cantidadRenta, double precioCompra, double precioRenta) {
        this.idPelicula = idPelicula;
        this.director = director;
        this.duracion = duracion;
        this.clasificacion = clasificacion;
        this.cantidadAlmacen = cantidadAlmacen;
        this.cantidadRenta = cantidadRenta;
        this.precioCompra = precioCompra;
        this.precioRenta = precioRenta;
    }

    public Integer getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Integer idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getCantidadAlmacen() {
        return cantidadAlmacen;
    }

    public void setCantidadAlmacen(int cantidadAlmacen) {
        this.cantidadAlmacen = cantidadAlmacen;
    }

    public int getCantidadRenta() {
        return cantidadRenta;
    }

    public void setCantidadRenta(int cantidadRenta) {
        this.cantidadRenta = cantidadRenta;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public double getPrecioRenta() {
        return precioRenta;
    }

    public void setPrecioRenta(double precioRenta) {
        this.precioRenta = precioRenta;
    }

    public Productos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Productos idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPelicula != null ? idPelicula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Peliculas)) {
            return false;
        }
        Peliculas other = (Peliculas) object;
        if ((this.idPelicula == null && other.idPelicula != null) || (this.idPelicula != null && !this.idPelicula.equals(other.idPelicula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Peliculas[ idPelicula=" + idPelicula + " ]";
    }
    
}
