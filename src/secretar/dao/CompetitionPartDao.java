/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secretar.objects.CompetitionPart;

/**
 *
 * @author Степан
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface CompetitionPartDao extends GenericDao<CompetitionPart, Long>{

}
