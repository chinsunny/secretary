package secretar.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secretar.objects.Competition;

@Transactional(propagation = Propagation.REQUIRED)
public interface CompetitionDao extends GenericDao<Competition, Long> {

}
