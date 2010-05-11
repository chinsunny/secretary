package secretar.objects;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "distance_parts")
@NamedQueries(value = {
        @NamedQuery(name = "DistancePart.findByTeam", query = "from DistancePart dp where dp.team = ?"),
        @NamedQuery(name = "DistancePart.findByVisibleNumber", query = "from DistancePart dp where dp.visibleNumber = ?")
})
public class DistancePart implements Serializable {

    private Long id;
    private Distance distance;
    private Team team;
    private String number;
    private String visibleNumber;
    private Date startTime;
    private Date finishTime;
    private Date plusTime;
    private Date minusTime;
    private Integer startOrder;
    private Integer finishOrder;
    private Integer scores;
    private Set<LapPart> lapParts;

    private Integer failCount;
    private Integer completeFailCount;

    @ManyToOne
    @JoinColumn(name = "distance_fk")
    @NotFound(action=NotFoundAction.IGNORE)
    public Distance getDistance() {
        return distance;
    }

    @Temporal(javax.persistence.TemporalType.TIME)
    public Date getFinishTime() {
        return finishTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @OneToMany
    public Set<LapPart> getLapParts() {
        return lapParts;
    }

    public String getNumber() {
        return number;
    }

    @Temporal(javax.persistence.TemporalType.TIME)
    public Date getStartTime() {
        return startTime;
    }

    @Temporal(javax.persistence.TemporalType.TIME)
    public Date getMinusTime() {
        return minusTime;
    }

    @Temporal(javax.persistence.TemporalType.TIME)
    public Date getPlusTime() {
        return plusTime;
    }



    @ManyToOne(cascade=CascadeType.REFRESH)
    @JoinColumn(name = "team_fk")
    public Team getTeam() {
        return team;
    }

    public String getVisibleNumber() {
        return visibleNumber;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLapParts(Set<LapPart> lapParts) {
        this.lapParts = lapParts;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setVisibleNumber(String visibleNumber) {
        this.visibleNumber = visibleNumber;
    }

    public Integer getFinishOrder() {
        return finishOrder;
    }

    public void setFinishOrder(Integer finishOrder) {
        this.finishOrder = finishOrder;
    }

    public Integer getStartOrder() {
        return startOrder;
    }

    public void setStartOrder(Integer startOrder) {
        this.startOrder = startOrder;
    }

    public void setMinusTime(Date minusTime) {
        this.minusTime = minusTime;
    }

    public void setPlusTime(Date plusTime) {
        this.plusTime = plusTime;
    }

    @Transient
    public boolean isComplete(){
        return finishTime != null && minusTime != null && plusTime !=null;
    }

    @Transient
    public Date getResult(){
        return new Time(getFinishTime().getTime() - getMinusTime().getTime() + getPlusTime().getTime());
    }

    public Integer getScores() {
        return scores;
    }

    public void setScores(Integer scores) {
        this.scores = scores;
    }

    @Transient
    public Integer getCompleteFailCount() {
        return completeFailCount;
    }

    @Transient
    public Integer getFailCount() {
        return failCount;
    }

    public void setCompleteFailCount(Integer completeFailCount) {
        this.completeFailCount = completeFailCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

}
