package secretar.objects;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "competitions")
//@NamedQueries(value = {
//        @NamedQuery(name = "DataMapping.findByFieldMapping", query = "from DataMapping dm where dm.fieldMapping = ?")
//})
public class Competition implements Serializable {

    private Long id;
    private String name;
    private Set<Distance> distances;

    public Competition() {
    }

    public Competition(String name) {
        this.name = name;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(cascade=CascadeType.REFRESH)
    public Set<Distance> getDistances() {
        return distances;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistances(Set<Distance> distances) {
        this.distances = distances;
    }

    
}
