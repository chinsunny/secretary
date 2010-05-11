package secretar.objects;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="competition_parts")
//@NamedQueries(value = {
//        @NamedQuery(name = "DataMapping.findByFieldMapping", query = "from DataMapping dm where dm.fieldMapping = ?")
//})
public class CompetitionPart implements Serializable {
	private Long id;

	private Delegation delegation;
	
	private Competition competition;
	
	private String number;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="competiotion_fk")
    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    @ManyToOne
    @JoinColumn(name="delegation_fk")
    public Delegation getDelegation() {
        return delegation;
    }

    public void setDelegation(Delegation delegation) {
        this.delegation = delegation;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }




}
