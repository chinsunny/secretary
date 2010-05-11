package secretar.objects;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name="distances")
public class Distance implements Serializable{

    private Long id;

    private String name;

    private Competition competition;

    private Set<Lap> laps;

    private Set<Group> groups;

    private Set<DistancePart> distanceParts;

    public Distance() {
    }

    public Distance(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    @ManyToOne
    @JoinColumn(name="competition_fk")
    public Competition getCompetition() {
        return competition;
    }

    @OneToMany
    public Set<Lap> getLaps() {
        return laps;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    @OneToMany
    public Set<Group> getGroups() {
        return groups;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLaps(Set<Lap> laps) {
        this.laps = laps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @OneToMany
    public Set<DistancePart> getDistanceParts() {
        return distanceParts;
    }

    public void setDistanceParts(Set<DistancePart> distanceParts) {
        this.distanceParts = distanceParts;
    }



}
