package secretar.logic;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secretar.dao.CompetitionDao;
import secretar.dao.CompetitionPartDao;
import secretar.dao.DelegationDao;
import secretar.dao.DistanceDao;
import secretar.dao.DistancePartDao;
import secretar.dao.GroupDao;
import secretar.dao.LapDao;
import secretar.dao.LapPartDao;
import secretar.dao.TeamDao;
import secretar.dao.TeamMemberDao;
import secretar.objects.Distance;
import secretar.objects.DistancePart;
import secretar.objects.Group;
import secretar.objects.Lap;
import secretar.objects.LapPart;
import secretar.objects.Team;
import secretar.objects.TeamMember;

@Transactional(propagation = Propagation.REQUIRED)
public class CompetitionLogic {

    private CompetitionDao competitionDao;
    private DistanceDao distanceDao;
    private LapDao lapDao;

    private CompetitionPartDao competitionPartDao;
    private DistancePartDao distancePartDao;
    private LapPartDao lapPartDao;

    private DelegationDao delegationDao;
    private TeamMemberDao teamMemberDao;
    private TeamDao teamDao;

    private GroupDao groupDao;

    public void generateStartList() {
        lapPartDao.deleteAll();
        distancePartDao.deleteAll();

        List<Team> teamList = teamDao.list();
        for (Team team : teamList) {
            Distance distance = team.getGroup().getDistance();

            DistancePart distancePart = new DistancePart();
            distancePart.setDistance(distance);
            distancePart.setTeam(team);
            distancePart.setVisibleNumber(team.getFullNumber());
            distancePart = distancePartDao.add(distancePart);

            List<Lap> lapSet = lapDao.findByDistance(distance);
            for (Lap lap : lapSet){
                LapPart lapPart = new LapPart();
                lapPart.setDistancePart(distancePart);
                lapPart.setLap(lap);
                lapPart.setLapState(LapPart.INIT_STATE);
                lapPartDao.create(lapPart);
            }
        }
    }

    public String getTeamMembersString(Team team){
        List<TeamMember> teamMemberList = teamMemberDao.findByTeam(team);
//        Set<TeamMember> teamMemberList = teamDao.read(team.getId()).getTeamMembers();
        StringBuffer buf = new StringBuffer();
        for (TeamMember teamMember : teamMemberList) {
            buf.append(teamMember.getName()).append(" (").append(teamMember.getRank()).append(") ");
        }
        return buf.toString();
    }

    public Integer getTeamFailCount(DistancePart distancePart){
        List<LapPart> lapPartList = lapPartDao.findByDistancePart(distancePart);
        Integer failCount = new Integer(0);
        for (LapPart lapPart : lapPartList) {
            if (lapPart.getLapState().equals(LapPart.FAIL_STATE)) {
                failCount++;
            }
        }
        return failCount;
    }

    public Integer getTeamCompleteFailCount(DistancePart distancePart) {
        List<LapPart> lapPartList = lapPartDao.findByDistancePart(distancePart);
        Integer failCount = new Integer(0);
        for (LapPart lapPart : lapPartList) {
            if (lapPart.getLapState().equals(LapPart.COMPLETE_FAIL_STATE)) {
                failCount++;
            }
        }
        return failCount;

    }

    public DistancePart findDistancePartByFullTeamNumber(String fullNumber) {
        DistancePart distancePart = distancePartDao.findByVisibleNumber(fullNumber);
        Set<LapPart> lapParts = distancePart.getLapParts();
        return distancePart;
    }

    public List<LapPart> findLapPartsByDistancePart(DistancePart distancePart){
        List<LapPart> lapParts = lapPartDao.findByDistancePart(distancePart);
        Collections.sort(lapParts, new Comparator(){

            public int compare(Object arg0, Object arg1) {
                LapPart one = (LapPart)arg0;
                LapPart two = (LapPart)arg1;
                return one.getLap().getNumber() - two.getLap().getNumber();
            }

        });
        return  lapParts;

    }


    public void setDelegationDao(DelegationDao delegationDao) {
        this.delegationDao = delegationDao;
    }

    public void setDistanceDao(DistanceDao distanceDao) {
        this.distanceDao = distanceDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void setLapDao(LapDao lapDao) {
        this.lapDao = lapDao;
    }

    public void setTeamDao(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    public void setTeamMemberDao(TeamMemberDao teamMemberDao) {
        this.teamMemberDao = teamMemberDao;
    }

    public void setCompetitionDao(CompetitionDao competitionDao) {
        this.competitionDao = competitionDao;
    }

    public void setCompetitionPartDao(CompetitionPartDao competitionPartDao) {
        this.competitionPartDao = competitionPartDao;
    }

    public void setDistancePartDao(DistancePartDao distancePartDao) {
        this.distancePartDao = distancePartDao;
    }

    public void setLapPartDao(LapPartDao lapPartDao) {
        this.lapPartDao = lapPartDao;
    }

    public List<DistancePart> getCalculatedDistanceParts() {
        List<DistancePart> distanceParts = distancePartDao.list();

        Map<Integer, List<DistancePart>> map = new HashMap<Integer, List<DistancePart>>();
        for (DistancePart distancePart : distanceParts) {
            Group group = distancePart.getTeam().getGroup();
            if (!map.containsKey(group.getNumber()))
                map.put(group.getNumber(), new LinkedList<DistancePart>());
            List<DistancePart> list = map.get(group.getNumber());
            if (distancePart.isComplete() && getTeamCompleteFailCount(distancePart) == 0) {
                list.add(distancePart);
            } else {
                distancePart.setFinishOrder(null);
                distancePart.setScores(null);
            }
        }

        PlaceComparator placeComparator = new PlaceComparator();
        for (Integer groupNumber : map.keySet()) {
            List<DistancePart> list = map.get(groupNumber);
            Collections.sort(list, placeComparator);
            for (int i = 0; i < list.size(); i++){
                DistancePart distancePart = list.get(i);
                distancePart.setFinishOrder(i+1);
                distancePart.setScores(getScore(groupNumber, i+1));
                distancePartDao.update(distancePart);
            }
        }
        return distanceParts;
    }

    public List<Lap> findLapsByGroupNumber(Integer groupNumber) {
        Group group = groupDao.findByNumber(groupNumber);
        List<Lap> laps = lapDao.findByDistance(group.getDistance());
        Collections.sort(laps, new Comparator(){

            public int compare(Object arg0, Object arg1) {
                Lap one = (Lap)arg0;
                Lap two = (Lap)arg1;
                return one.getNumber() - two.getNumber();
            }

        });
        return  laps;
    }

    private class PlaceComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            DistancePart distancePart0 = (DistancePart)arg0;
            DistancePart distancePart1 = (DistancePart)arg1;

            int fail = getTeamFailCount(distancePart0) - getTeamFailCount(distancePart1);

            if (fail > 0 || fail < 0) {
                return fail;
            }

            long result = distancePart0.getResult().getTime() - distancePart1.getResult().getTime();

            if (result > 0 || result < 0) {
                return (int)(result);
            }

            return 0;
        }

    }

    private Integer getScore(Integer groupNumber, Integer place) {
        int order = place - 1;
        if (order >= 21)
            order = 20;
        int row = 0;
        if (groupNumber == 4 || groupNumber == 5) {
            row = 1;
        }

        return SCORES[row][order];
    }

    private static int[][] SCORES = {
        {400, 370, 340, 310, 280, 260, 240, 220, 200, 180, 165, 150, 135, 120, 105, 90, 75, 60, 45, 30, 15},
        {500, 465, 430, 395, 360, 335, 310, 285, 260, 240, 220, 200, 180, 160, 140, 120, 100, 80, 60, 40, 20}
    };


}
