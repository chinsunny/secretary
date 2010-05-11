package secretar.objects;

import java.io.Serializable;
import java.util.Set;
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
import javax.persistence.Transient;

@Entity
@Table(name="teams")
@NamedQueries(value = {
        @NamedQuery(name = "Team.findByDelegationAndNumber", query = "from Team t where t.delegation = ? and t.number = ?")
})
public class Team implements Serializable{
    private Long id;
    private Integer number;
    private String name;
    private String chipNumber;
    private Group group;
    private Delegation delegation;
    private Set<TeamMember> teamMembers;


    private Set<DistancePart> distanceParts;

    @ManyToOne
    @JoinColumn(name="group_fk")
    public Group getGroup() {
        return group;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @OneToMany
    public Set<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    @OneToMany
    public Set<DistancePart> getDistanceParts() {
        return distanceParts;
    }

    public void setDistanceParts(Set<DistancePart> distanceParts) {
        this.distanceParts = distanceParts;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamMembers(Set<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
    }

    @ManyToOne
    @JoinColumn(name="delegation_fk")
    public Delegation getDelegation() {
        return delegation;
    }

    public void setDelegation(Delegation delegation) {
        this.delegation = delegation;
    }

    @Transient
    public String getFullNumber(){
        return getDelegation().getNumber() + "." + getNumber();
    }
}
