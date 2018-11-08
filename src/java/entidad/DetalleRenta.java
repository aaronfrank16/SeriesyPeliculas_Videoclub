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
@Table(name = "detalle_renta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleRenta.findAll", query = "SELECT d FROM DetalleRenta d")
    , @NamedQuery(name = "DetalleRenta.findByIdDetalleRenta", query = "SELECT d FROM DetalleRenta d WHERE d.idDetalleRenta = :idDetalleRenta")
    , @NamedQuery(name = "DetalleRenta.findBySubtotal", query = "SELECT d FROM DetalleRenta d WHERE d.subtotal = :subtotal")
    , @NamedQuery(name = "DetalleRenta.findByCantidad", query = "SELECT d FROM DetalleRenta d WHERE d.cantidad = :cantidad")})
public class DetalleRenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idDetalleRenta")
    private Integer idDetalleRenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subtotal")
    private double subtotal;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "idRenta", referencedColumnName = "idRenta")
    @ManyToOne(optional = false)
    private Rentas idRenta;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Productos idProducto;

    public DetalleRenta() {
    }

    public DetalleRenta(Integer idDetalleRenta) {
        this.idDetalleRenta = idDetalleRenta;
    }

    public DetalleRenta(Integer idDetalleRenta, double subtotal) {
        this.idDetalleRenta = idDetalleRenta;
        this.subtotal = subtotal;
    }

    public Integer getIdDetalleRenta() {
        return idDetalleRenta;
    }

    public void setIdDetalleRenta(Integer idDetalleRenta) {
        this.idDetalleRenta = idDetalleRenta;
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

    public Rentas getIdRenta() {
        return idRenta;
    }

    public void setIdRenta(Rentas idRenta) {
        this.idRenta = idRenta;
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
        hash += (idDetalleRenta != null ? idDetalleRenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleRenta)) {
            return false;
        }
        DetalleRenta other = (DetalleRenta) object;
        if ((this.idDetalleRenta == null && other.idDetalleRenta != null) || (this.idDetalleRenta != null && !this.idDetalleRenta.equals(other.idDetalleRenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.DetalleRenta[ idDetalleRenta=" + idDetalleRenta + " ]";
    }
    
}
