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

@Entity
@Table(name = "lap_parts")
@NamedQueries(value = {
      @NamedQuery(name = "LapPart.findByDistancePart", query = "from LapPart lp where lp.distancePart = ?")
})
public class LapPart implements Serializable {
    public static final int INIT_STATE = 0;
    public static final int OK_STATE = 1;
    public static final int FAIL_STATE = 2;
    public static final int COMPLETE_FAIL_STATE = 3;

    private Long id;
    private Integer lapState;
    private Lap lap;
    private DistancePart distancePart;

    @ManyToOne
    @JoinColumn(name = "distance_part_fk")
    public DistancePart getDistancePart() {
        return distancePart;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "lap_fk")
    public Lap getLap() {
        return lap;
    }

    public void setDistancePart(DistancePart distancePart) {
        this.distancePart = distancePart;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLap(Lap lap) {
        this.lap = lap;
    }

    public Integer getLapState() {
        return lapState;
    }

    public void setLapState(Integer lapState) {
        this.lapState = lapState;
    }
    
}
