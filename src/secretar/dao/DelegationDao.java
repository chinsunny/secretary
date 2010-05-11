/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secretar.objects.Delegation;

@Transactional(propagation = Propagation.REQUIRED)
public interface DelegationDao extends GenericDao<Delegation, Long>{

    public Delegation findByNumber(Integer delegationNumber);

}
