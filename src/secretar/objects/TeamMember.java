package secretar.objects;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "team_members")
@NamedQueries(value = {
        @NamedQuery(name = "TeamMember.findByTeam", query = "from TeamMember tm where tm.team = ?")
})
public class TeamMember implements Serializable{
    private Long id;

    private String name;
    private Integer birthYear;
    private String rank;
    private String gender;
    private Group group;

    private Team team;
    private Delegation delegation;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    @ManyToOne
    @JoinColumn(name="group_fk")
    public Group getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    @ManyToOne
    @JoinColumn(name="team_fk")
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @ManyToOne
    @JoinColumn(name="delegation_fk")
    public Delegation getDelegation() {
        return delegation;
    }

    public void setDelegation(Delegation delegation) {
        this.delegation = delegation;
    }



}
