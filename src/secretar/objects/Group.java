package secretar.objects;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="groups")
@NamedQueries(value = {
        @NamedQuery(name = "Group.findByNumber", query = "from Group g where g.number = ?")
})
public class Group implements Serializable {
    private Long id;
    private Integer number;
    private String name;
    private Distance distance;

    public Group() {
    }

    public Group(Integer number, String name) {
        this.number = number;
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

    public Integer getNumber() {
        return number;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @ManyToOne(cascade=javax.persistence.CascadeType.ALL)
    @JoinColumn(name="distance_fk")
    @Cascade(CascadeType.SAVE_UPDATE)
    @NotFound(action=NotFoundAction.IGNORE)
    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }


}
