/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rubens
 */
@Entity
@Table(name = "Agent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agent.findAll", query = "SELECT a FROM Agent a"),
    @NamedQuery(name = "Agent.findByIdIa", query = "SELECT a FROM Agent a WHERE a.idIa = :idIa"),
    @NamedQuery(name = "Agent.findByIteration", query = "SELECT a FROM Agent a WHERE a.iteration = :iteration"),
    @NamedQuery(name = "Agent.findByCode", query = "SELECT a FROM Agent a WHERE a.code = :code"),
    @NamedQuery(name = "Agent.findByType", query = "SELECT a FROM Agent a WHERE a.type = :type"),
    @NamedQuery(name = "Agent.findByLir", query = "SELECT a FROM Agent a WHERE a.lir = :lir"),
    @NamedQuery(name = "Agent.findByWor", query = "SELECT a FROM Agent a WHERE a.wor = :wor"),
    @NamedQuery(name = "Agent.findByRar", query = "SELECT a FROM Agent a WHERE a.rar = :rar"),
    @NamedQuery(name = "Agent.findByHer", query = "SELECT a FROM Agent a WHERE a.her = :her"),
    @NamedQuery(name = "Agent.findByNs", query = "SELECT a FROM Agent a WHERE a.ns = :ns"),
    @NamedQuery(name = "Agent.findByA3n", query = "SELECT a FROM Agent a WHERE a.a3n = :a3n"),
    @NamedQuery(name = "Agent.findByStt", query = "SELECT a FROM Agent a WHERE a.stt = :stt")})
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_ia")
    private Integer idIa;
    @Basic(optional = false)
    @Column(name = "iteration")
    private int iteration;
    @Column(name = "code")
    private String code;
    @Column(name = "type")
    private String type;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "lir")
    private Double lir = 0.0d;
    @Column(name = "wor")
    private Double wor= 0.0d;
    @Column(name = "rar")
    private Double rar= 0.0d;
    @Column(name = "her")
    private Double her= 0.0d;
    @Column(name = "ns")
    private Double ns= 0.0d;
    @Column(name = "a3n")
    private Double a3n= 0.0d;
    @Column(name = "stt")
    private Double stt= 0.0d;
    @JoinColumn(name = "id_test", referencedColumnName = "id_test")
    @ManyToOne
    private Test idTest;
    @Column(name = "mp")
    private Double mp= 0.0d;
    @Column(name = "me")
    private Double me= 0.0d;
    @Column(name = "le1")
    private Double le1= 0.0d;
    @Column(name = "le2")
    private Double le2= 0.0d;

    public Agent() {
    }

    public Agent(Integer idIa) {
        this.idIa = idIa;
    }

    public Agent(Integer idIa, int iteration) {
        this.idIa = idIa;
        this.iteration = iteration;
    }

    public Integer getIdIa() {
        return idIa;
    }

    public void setIdIa(Integer idIa) {
        this.idIa = idIa;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLir() {
        return lir;
    }

    public void setLir(Double lir) {
        this.lir = lir;
    }

    public Double getWor() {
        return wor;
    }

    public void setWor(Double wor) {
        this.wor = wor;
    }

    public Double getRar() {
        return rar;
    }

    public void setRar(Double rar) {
        this.rar = rar;
    }

    public Double getHer() {
        return her;
    }

    public void setHer(Double her) {
        this.her = her;
    }

    public Double getNs() {
        return ns;
    }

    public void setNs(Double ns) {
        this.ns = ns;
    }

    public Double getA3n() {
        return a3n;
    }

    public void setA3n(Double a3n) {
        this.a3n = a3n;
    }

    public Double getStt() {
        return stt;
    }

    public void setStt(Double stt) {
        this.stt = stt;
    }

    public Test getIdTest() {
        return idTest;
    }

    public void setIdTest(Test idTest) {
        this.idTest = idTest;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idIa != null ? idIa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agent)) {
            return false;
        }
        Agent other = (Agent) object;
        if ((this.idIa == null && other.idIa != null) || (this.idIa != null && !this.idIa.equals(other.idIa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ai.synthesis.DslLeague.ReportCharts.DBmodels.Agent[ idIa=" + idIa + " ]";
    }

    public Double getMp() {
        return mp;
    }

    public void setMp(Double mp) {
        this.mp = mp;
    }

    public Double getMe() {
        return me;
    }

    public void setMe(Double me) {
        this.me = me;
    }

    public Double getLe1() {
        return le1;
    }

    public void setLe1(Double le1) {
        this.le1 = le1;
    }

    public Double getLe2() {
        return le2;
    }

    public void setLe2(Double le2) {
        this.le2 = le2;
    }
    
}
