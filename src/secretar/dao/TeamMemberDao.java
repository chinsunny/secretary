/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.dao;

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secretar.objects.Team;
import secretar.objects.TeamMember;

/**
 *
 * @author Степан
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface TeamMemberDao extends GenericDao<TeamMember, Long> {

    public List<TeamMember> findByTeam(Team team);
}
