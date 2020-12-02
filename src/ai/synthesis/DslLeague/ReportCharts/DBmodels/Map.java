/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rubens
 */
@Entity
@Table(name = "Map")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Map.findAll", query = "SELECT m FROM Map m"),
    @NamedQuery(name = "Map.findByIdMap", query = "SELECT m FROM Map m WHERE m.idMap = :idMap"),
    @NamedQuery(name = "Map.findByName", query = "SELECT m FROM Map m WHERE m.name = :name"),
    @NamedQuery(name = "Map.findByPath", query = "SELECT m FROM Map m WHERE m.path = :path")})
public class Map implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_map")
    private Integer idMap;
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "path")
    private String path;
    @OneToMany(mappedBy = "mapId")
    private List<Test> testList;

    public Map() {
    }

    public Map(Integer idMap) {
        this.idMap = idMap;
    }

    public Map(Integer idMap, String path) {
        this.idMap = idMap;
        this.path = path;
    }

    public Integer getIdMap() {
        return idMap;
    }

    public void setIdMap(Integer idMap) {
        this.idMap = idMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @XmlTransient
    public List<Test> getTestList() {
        return testList;
    }

    public void setTestList(List<Test> testList) {
        this.testList = testList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMap != null ? idMap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Map)) {
            return false;
        }
        Map other = (Map) object;
        if ((this.idMap == null && other.idMap != null) || (this.idMap != null && !this.idMap.equals(other.idMap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ai.synthesis.DslLeague.ReportCharts.DBmodels.Map[ idMap=" + idMap + " ]";
    }
    
}
