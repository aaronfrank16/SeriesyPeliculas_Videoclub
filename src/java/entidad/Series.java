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
@Table(name = "series")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Series.findAll", query = "SELECT s FROM Series s")
    , @NamedQuery(name = "Series.findByIdSerie", query = "SELECT s FROM Series s WHERE s.idSerie = :idSerie")
    , @NamedQuery(name = "Series.findByNumeroTemporadas", query = "SELECT s FROM Series s WHERE s.numeroTemporadas = :numeroTemporadas")
    , @NamedQuery(name = "Series.findByPrecioCompra", query = "SELECT s FROM Series s WHERE s.precioCompra = :precioCompra")
    , @NamedQuery(name = "Series.findByEstudio", query = "SELECT s FROM Series s WHERE s.estudio = :estudio")})
public class Series implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSerie")
    private Integer idSerie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_temporadas")
    private int numeroTemporadas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_compra")
    private double precioCompra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "estudio")
    private String estudio;
    @JoinColumn(name = "idProducto", referencedColumnName = "idProducto")
    @ManyToOne(optional = false)
    private Productos idProducto;

    public Series() {
    }

    public Series(Integer idSerie) {
        this.idSerie = idSerie;
    }

    public Series(Integer idSerie, int numeroTemporadas, double precioCompra, String estudio) {
        this.idSerie = idSerie;
        this.numeroTemporadas = numeroTemporadas;
        this.precioCompra = precioCompra;
        this.estudio = estudio;
    }

    public Integer getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(Integer idSerie) {
        this.idSerie = idSerie;
    }

    public int getNumeroTemporadas() {
        return numeroTemporadas;
    }

    public void setNumeroTemporadas(int numeroTemporadas) {
        this.numeroTemporadas = numeroTemporadas;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
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
        hash += (idSerie != null ? idSerie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Series)) {
            return false;
        }
        Series other = (Series) object;
        if ((this.idSerie == null && other.idSerie != null) || (this.idSerie != null && !this.idSerie.equals(other.idSerie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Series[ idSerie=" + idSerie + " ]";
    }
    
}
