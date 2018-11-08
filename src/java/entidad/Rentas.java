/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "rentas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rentas.findAll", query = "SELECT r FROM Rentas r")
    , @NamedQuery(name = "Rentas.findByIdRenta", query = "SELECT r FROM Rentas r WHERE r.idRenta = :idRenta")
    , @NamedQuery(name = "Rentas.findByFechaRenta", query = "SELECT r FROM Rentas r WHERE r.fechaRenta = :fechaRenta")
    , @NamedQuery(name = "Rentas.findByFechaDevolucion", query = "SELECT r FROM Rentas r WHERE r.fechaDevolucion = :fechaDevolucion")
    , @NamedQuery(name = "Rentas.findByFechaEntrega", query = "SELECT r FROM Rentas r WHERE r.fechaEntrega = :fechaEntrega")
    , @NamedQuery(name = "Rentas.findByTotalRenta", query = "SELECT r FROM Rentas r WHERE r.totalRenta = :totalRenta")})
public class Rentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idRenta")
    private Integer idRenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_renta")
    @Temporal(TemporalType.DATE)
    private Date fechaRenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_devolucion")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_renta")
    @Temporal(TemporalType.DATE)
    private Date totalRenta;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuarios idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRenta")
    private List<DetalleRenta> detalleRentaList;

    public Rentas() {
    }

    public Rentas(Integer idRenta) {
        this.idRenta = idRenta;
    }

    public Rentas(Integer idRenta, Date fechaRenta, Date fechaDevolucion, Date fechaEntrega, Date totalRenta) {
        this.idRenta = idRenta;
        this.fechaRenta = fechaRenta;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaEntrega = fechaEntrega;
        this.totalRenta = totalRenta;
    }

    public Integer getIdRenta() {
        return idRenta;
    }

    public void setIdRenta(Integer idRenta) {
        this.idRenta = idRenta;
    }

    public Date getFechaRenta() {
        return fechaRenta;
    }

    public void setFechaRenta(Date fechaRenta) {
        this.fechaRenta = fechaRenta;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Date getTotalRenta() {
        return totalRenta;
    }

    public void setTotalRenta(Date totalRenta) {
        this.totalRenta = totalRenta;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
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
        hash += (idRenta != null ? idRenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rentas)) {
            return false;
        }
        Rentas other = (Rentas) object;
        if ((this.idRenta == null && other.idRenta != null) || (this.idRenta != null && !this.idRenta.equals(other.idRenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Rentas[ idRenta=" + idRenta + " ]";
    }
    
}
