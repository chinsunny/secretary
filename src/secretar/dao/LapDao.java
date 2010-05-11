/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.dao;

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secretar.objects.Distance;
import secretar.objects.Lap;

/**
 *
 * @author Степан
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface LapDao extends GenericDao<Lap, Long>{

    public List<Lap> findByDistance(Distance distance);

}
