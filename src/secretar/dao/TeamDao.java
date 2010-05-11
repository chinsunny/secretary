/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secretar.objects.Delegation;
import secretar.objects.Team;

/**
 *
 * @author Степан
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface TeamDao extends GenericDao<Team, Long>{

    public Team findByDelegationAndNumber(Delegation delegation, Integer teamNumber);

}
