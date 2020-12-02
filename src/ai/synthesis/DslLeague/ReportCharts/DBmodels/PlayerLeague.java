/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.DslLeague.ReportCharts.DBmodels;

import java.io.Serializable;
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
@Table(name = "player_league")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlayerLeague.findAll", query = "SELECT p FROM PlayerLeague p"),
    @NamedQuery(name = "PlayerLeague.findByIdPlayer", query = "SELECT p FROM PlayerLeague p WHERE p.idPlayer = :idPlayer"),
    @NamedQuery(name = "PlayerLeague.findByType", query = "SELECT p FROM PlayerLeague p WHERE p.type = :type"),
    @NamedQuery(name = "PlayerLeague.findByCode", query = "SELECT p FROM PlayerLeague p WHERE p.code = :code"),
    @NamedQuery(name = "PlayerLeague.findByFullLog", query = "SELECT p FROM PlayerLeague p WHERE p.fullLog = :fullLog")})
public class PlayerLeague implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_player")
    private Integer idPlayer;
    @Column(name = "type")
    private String type;
    @Column(name = "code")
    private String code;
    @Column(name = "full_log")
    private String fullLog;
    @JoinColumn(name = "id_league", referencedColumnName = "id_league")
    @ManyToOne
    private LeagueInfo idLeague;

    public PlayerLeague() {
    }

    public PlayerLeague(Integer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public Integer getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Integer idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullLog() {
        return fullLog;
    }

    public void setFullLog(String fullLog) {
        this.fullLog = fullLog;
    }

    public LeagueInfo getIdLeague() {
        return idLeague;
    }

    public void setIdLeague(LeagueInfo idLeague) {
        this.idLeague = idLeague;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlayer != null ? idPlayer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlayerLeague)) {
            return false;
        }
        PlayerLeague other = (PlayerLeague) object;
        if ((this.idPlayer == null && other.idPlayer != null) || (this.idPlayer != null && !this.idPlayer.equals(other.idPlayer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ai.synthesis.DslLeague.ReportCharts.DBmodels.PlayerLeague[ idPlayer=" + idPlayer + " ]";
    }
    
}
