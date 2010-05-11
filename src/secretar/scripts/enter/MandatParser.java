package secretar.scripts.enter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import secretar.objects.Delegation;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.log4j.Logger;
import secretar.dao.CompetitionDao;
import secretar.dao.DelegationDao;
import secretar.dao.DistanceDao;
import secretar.dao.GroupDao;
import secretar.dao.TeamDao;
import secretar.dao.TeamMemberDao;
import secretar.objects.Competition;
import secretar.objects.Distance;
import secretar.objects.TeamMember;
import secretar.objects.Group;
import secretar.objects.Team;
import secretar.scripts.ScriptUtils;
import static secretar.utils.StringUtils.*;
import static secretar.constants.Rank.*;
import static secretar.constants.Gender.*;

public class MandatParser {
    private static Logger logger = Logger.getLogger(MandatParser.class);

    private DelegationDao delegationDao;
    private TeamDao teamDao;
    private TeamMemberDao teamMemberDao;
    private GroupDao groupDao;
    private CompetitionDao competitionDao;
    private DistanceDao distanceDao;

    public void setDelegationDao(DelegationDao delegationDao) {
        this.delegationDao = delegationDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void setCompetitionDao(CompetitionDao competitionDao) {
        this.competitionDao = competitionDao;
    }

    public void setDistanceDao(DistanceDao distanceDao) {
        this.distanceDao = distanceDao;
    }

    public void setTeamDao(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    public void setTeamMemberDao(TeamMemberDao teamMemberDao) {
        this.teamMemberDao = teamMemberDao;
    }

    

    public void loadMandat(String bidFile) throws IOException {
        teamMemberDao.deleteAll();
        teamDao.deleteAll();
        delegationDao.deleteAll();

        InputStream myxls = new FileInputStream(bidFile);
        HSSFWorkbook wb = new HSSFWorkbook(myxls);
        HSSFSheet sheet = wb.getSheetAt(0);

        ArrayList<ArrayList<String>> data = ScriptUtils.convertSheetDataToStringTable(sheet);

        HashMap<Integer, Delegation> delegationHash = new HashMap<Integer, Delegation>();
        HashMap<Integer, Map<Integer, Team>> teamHash = new HashMap<Integer, Map<Integer, Team>>();
        for(int i = 1; i < data.size(); i++) {
            ArrayList<String> row = data.get(i);

            if (row.size() < 12) {
                throw new IOException("Нехватает колонок в ряде " + i);
            }
            String number = row.get(0);
            String delegationName = row.get(1);
            String org = row.get(2);
            String region = row.get(3);
            String leader = row.get(4);

            String memberName = row.get(5);
            String memberBirth = row.get(6);
            String memberRank = row.get(7);
            String memberGender = row.get(8);

            String memberGroup = row.get(9);
            String teamNumberString = row.get(10);
            String chipNumber = row.get(11);

            Integer delegationNumber;
            if (isNullOrEmpty(number)) {
                throw new IOException("Пустой номер делегации в ряде " + i);
            } else {
                try {
                    delegationNumber = Integer.valueOf(number);
                } catch(NumberFormatException e) {
                    throw new IOException("Не числовой номер делегации в ряде " + i + ". " + e.getMessage());
                }
            }
            
            if (isNullOrEmpty(delegationName)){
                throw new IOException("Пустое название делегации в ряде " + i);
            }

            
            if (isNullOrEmpty(org)){
                throw new IOException("Пустое название учреждения в ряде " + i);
            }

            if (isNullOrEmpty(region)){
                throw new IOException("Пустое название муниципального образования в ряде " + i);
            }

            if (isNullOrEmpty(leader)){
                throw new IOException("Пустое ФИО представителя в ряде " + i);
            }

            if (isNullOrEmpty(memberName)){
                throw new IOException("Пустое ФИО участника в ряде " + i);
            }

            Integer memberBirthYear;
            if (isNullOrEmpty(memberBirth)){
                throw new IOException("Пустой год рождения участника в ряде " + i);
            } else {
                try {
                   memberBirthYear = Integer.valueOf(memberBirth);
                } catch(NumberFormatException e) {
                    throw new IOException("Не числовой год рождения в ряде " + i + ". " + e.getMessage());
                }
            }

            if (isNullOrEmpty(memberRank)){
                throw new IOException("Пустое значение в графе разряд в ряде " + i);
            } else {
                memberRank = memberRank.trim().toUpperCase();
                if (!(memberRank.equals(BR) |
                    memberRank.equals(FIRST_JUNIOR) |
                    memberRank.equals(SECOND_JUNIOR) |
                    memberRank.equals(THIRD_JUNIOR) |
                    memberRank.equals(FIRST) |
                    memberRank.equals(SECOND) |
                    memberRank.equals(THIRD) |
                    memberRank.equals(KMS) |
                    memberRank.equals(MS)
                    )){
                    throw new IOException("Неправильное значение разряда в ряде " + i + " = " + memberRank);
                }
            }

            if (isNullOrEmpty(memberGender)){
                throw new IOException("Не указан пол в ряде " + i);
            } else {
                memberGender = memberGender.trim().toUpperCase();
                if (!(memberGender.equals(MAN) |
                      memberGender.equals(WOMAN)  )) {
                      throw new IOException("Неправильное значение поля в ряде " + i + " = " + memberGender);
                }
            }

            Integer groupNumber;
            Group group;
            if (isNullOrEmpty(memberGroup)){
                throw new IOException("Не указана группа в ряде " + i);
            } else {
                try {
                   groupNumber = Integer.valueOf(memberGroup);
                } catch(NumberFormatException e) {
                    throw new IOException("Не числовое значение группы в ряде " + i + ". " + e.getMessage());
                }
                group = groupDao.findByNumber(groupNumber);
                if (group == null) {
                    throw new IOException("Не существующая группа в ряде " + i + " = " + groupNumber);
                }
            }

            Integer teamNumber;
            if (isNullOrEmpty(teamNumberString)){
                throw new IOException("Не указан номер команды в ряде " + i);
            } else {
                try {
                   teamNumber = Integer.valueOf(teamNumberString);
                } catch(NumberFormatException e) {
                    throw new IOException("Не числовое значение номера команды в ряде " + i + ". " + e.getMessage());
                }
            }

            if (isNullOrEmpty(chipNumber)){
                throw new IOException("Не указан номер чипа в ряде " + i);
            } else {
//                if (!Pattern.matches("\\d[5,7]", chipNumber)){
//                    throw new IOException("Неправильный номер чипа в ряде " + i + ". Ожидается число от 10000 до 9999999");
//                }
            }


            Delegation delegation = null;
            if (delegationHash.containsKey(delegationNumber)) {
                delegation = delegationHash.get(delegationNumber);
            } else {
                delegation = new Delegation();
                delegation.setNumber(delegationNumber);
                delegation.setName(delegationName);
                delegation.setOrganization(org);
                delegation.setRegion(region);
                delegation.setLead(leader);
                delegationHash.put(delegationNumber, delegation);
                delegation = delegationDao.add(delegation);
            }

            
            if (!teamHash.containsKey(delegation.getNumber())) {
                teamHash.put(delegation.getNumber(), new HashMap<Integer, Team>());
            }

            Map<Integer, Team> teams = teamHash.get(delegation.getNumber());
            Team team = null;
            if (teams.containsKey(teamNumber)) {
                team = teams.get(teamNumber);
            } else {
                team = new Team();
                team.setName(delegationName + " " + teamNumber);
                team.setGroup(group);
                team.setNumber(teamNumber);
                team.setChipNumber(chipNumber);
                team.setDelegation(delegation);
                teams.put(teamNumber, team);
                team = teamDao.add(team);
            }

            TeamMember teamMember = new TeamMember();
            teamMember.setName(memberName);
            teamMember.setBirthYear(memberBirthYear);
            teamMember.setRank(memberRank);
            teamMember.setGender(memberGender);
            teamMember.setGroup(group);
            teamMember.setTeam(team);
            teamMember.setDelegation(delegation);
            teamMemberDao.add(teamMember);

        }


    }

}
