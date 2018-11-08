/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "productos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p")
    , @NamedQuery(name = "Productos.findByIdProducto", query = "SELECT p FROM Productos p WHERE p.idProducto = :idProducto")
    , @NamedQuery(name = "Productos.findByTipo", query = "SELECT p FROM Productos p WHERE p.tipo = :tipo")
    , @NamedQuery(name = "Productos.findByTitulo", query = "SELECT p FROM Productos p WHERE p.titulo = :titulo")
    , @NamedQuery(name = "Productos.findBySinopsis", query = "SELECT p FROM Productos p WHERE p.sinopsis = :sinopsis")
    , @NamedQuery(name = "Productos.findByA\u00f1o", query = "SELECT p FROM Productos p WHERE p.a\u00f1o = :a\u00f1o")
    , @NamedQuery(name = "Productos.findByRating", query = "SELECT p FROM Productos p WHERE p.rating = :rating")})
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idProducto")
    private Integer idProducto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    private int tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sinopsis")
    private String sinopsis;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "a\u00f1o")
    private String año;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rating")
    private double rating;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
    private List<Peliculas> peliculasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
    private List<Series> seriesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
    private List<Carritos> carritosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
    private List<DetalleCompra> detalleCompraList;
    @JoinColumn(name = "idCategoria", referencedColumnName = "idCategoria")
    @ManyToOne(optional = false)
    private Categorias idCategoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducto")
    private List<DetalleRenta> detalleRentaList;

    public Productos() {
    }

    public Productos(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Productos(Integer idProducto, int tipo, String titulo, String sinopsis, String año, double rating) {
        this.idProducto = idProducto;
        this.tipo = tipo;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.año = año;
        this.rating = rating;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @XmlTransient
    public List<Peliculas> getPeliculasList() {
        return peliculasList;
    }

    public void setPeliculasList(List<Peliculas> peliculasList) {
        this.peliculasList = peliculasList;
    }

    @XmlTransient
    public List<Series> getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<Series> seriesList) {
        this.seriesList = seriesList;
    }

    @XmlTransient
    public List<Carritos> getCarritosList() {
        return carritosList;
    }

    public void setCarritosList(List<Carritos> carritosList) {
        this.carritosList = carritosList;
    }

    @XmlTransient
    public List<DetalleCompra> getDetalleCompraList() {
        return detalleCompraList;
    }

    public void setDetalleCompraList(List<DetalleCompra> detalleCompraList) {
        this.detalleCompraList = detalleCompraList;
    }

    public Categorias getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categorias idCategoria) {
        this.idCategoria = idCategoria;
    }

    @XmlTransient
    public List<DetalleRenta> getDetalleRentaList() {
        return detalleRentaList;
    }

    public void setDetalleRentaList(List<DetalleRenta> detalleRentaList) {
        this.detalleRentaList = detalleRentaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Productos[ idProducto=" + idProducto + " ]";
    }
    
}
