/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rubens
 */
@Entity
@Table(name = "league_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LeagueInfo.findAll", query = "SELECT l FROM LeagueInfo l"),
    @NamedQuery(name = "LeagueInfo.findByIdLeague", query = "SELECT l FROM LeagueInfo l WHERE l.idLeague = :idLeague"),
    @NamedQuery(name = "LeagueInfo.findByIteration", query = "SELECT l FROM LeagueInfo l WHERE l.iteration = :iteration")})
public class LeagueInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id    
    @Column(name = "id_league")
    private Integer idLeague;
    @Column(name = "iteration")
    private Integer iteration;
    @JoinColumn(name = "id_test", referencedColumnName = "id_test")
    @ManyToOne
    private Test idTest;
    @OneToMany(mappedBy = "idLeague")
    private List<PlayerLeague> playerLeagueList;

    public LeagueInfo() {
        this.playerLeagueList = new ArrayList<>();
    }

    public LeagueInfo(Integer idLeague) {
        this();
        this.idLeague = idLeague;        
    }

    public Integer getIdLeague() {
        return idLeague;
    }

    public void setIdLeague(Integer idLeague) {
        this.idLeague = idLeague;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }

    public Test getIdTest() {
        return idTest;
    }

    public void setIdTest(Test idTest) {
        this.idTest = idTest;
    }

    @XmlTransient
    public List<PlayerLeague> getPlayerLeagueList() {
        return playerLeagueList;
    }

    public void setPlayerLeagueList(List<PlayerLeague> playerLeagueList) {
        this.playerLeagueList = playerLeagueList;
    }
    
    public void add_PlayerLeague(PlayerLeague player){
        this.playerLeagueList.add(player);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLeague != null ? idLeague.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LeagueInfo)) {
            return false;
        }
        LeagueInfo other = (LeagueInfo) object;
        if ((this.idLeague == null && other.idLeague != null) || (this.idLeague != null && !this.idLeague.equals(other.idLeague))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ai.synthesis.DslLeague.ReportCharts.DBmodels.LeagueInfo[ idLeague=" + idLeague + " ]";
    }
    
}
