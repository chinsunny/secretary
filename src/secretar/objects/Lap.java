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
import org.hibernate.annotations.Cascade;

@Entity
@Table(name="laps")
@NamedQueries(value = {
        @NamedQuery(name = "Lap.findByDistance", query = "from Lap l where l.distance = ?")
})
public class Lap implements Serializable{

    private Long id;
    private Integer number;
    private String name;
    private Distance distance;

    public Lap() {
    }

    public Lap(Integer number, String name, Distance distance) {
        this.number = number;
        this.name = name;
        this.distance = distance;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="distance_fk")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    
}
