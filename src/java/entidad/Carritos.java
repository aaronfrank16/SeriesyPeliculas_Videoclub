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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "carritos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carritos.findAll", query = "SELECT c FROM Carritos c")
    , @NamedQuery(name = "Carritos.findByIdCarrito", query = "SELECT c FROM Carritos c WHERE c.idCarrito = :idCarrito")
    , @NamedQuery(name = "Carritos.findByTipoProducto", query = "SELECT c FROM Carritos c WHERE c.tipoProducto = :tipoProducto")
    , @NamedQuery(name = "Carritos.findByTipo", query = "SELECT c FROM Carritos c WHERE c.tipo = :tipo")
    , @NamedQuery(name = "Carritos.findBySubtotal", query = "SELECT c FROM Carritos c WHERE c.subtotal = :subtotal")
    , @NamedQuery(name = "Carritos.findByCantidad", query = "SELECT c FROM Carritos c WHERE c.cantidad = :cantidad")})
public class Carritos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCarrito")
    private Integer idCarrito;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_producto")
    private int tipoProducto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    private int tipo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subtotal")
    private double subtotal;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Productos idProducto;

    public Carritos() {
    }

    public Carritos(Integer idCarrito) {
        this.idCarrito = idCarrito;
    }

    public Carritos(Integer idCarrito, int tipoProducto, int tipo, double subtotal) {
        this.idCarrito = idCarrito;
        this.tipoProducto = tipoProducto;
        this.tipo = tipo;
        this.subtotal = subtotal;
    }

    public Integer getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Integer idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(int tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
        hash += (idCarrito != null ? idCarrito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carritos)) {
            return false;
        }
        Carritos other = (Carritos) object;
        if ((this.idCarrito == null && other.idCarrito != null) || (this.idCarrito != null && !this.idCarrito.equals(other.idCarrito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Carritos[ idCarrito=" + idCarrito + " ]";
    }
    
}
