package secretar.scripts.results;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import secretar.dao.DistancePartDao;
import secretar.dao.LapPartDao;
import secretar.logic.CompetitionLogic;
import secretar.objects.DistancePart;
import secretar.objects.Group;
import secretar.objects.Lap;
import secretar.objects.LapPart;
import secretar.scripts.ScriptUtils;

public class ResultsByGroup {

    private DistancePartDao distancePartDao;
    private LapPartDao lapPartDao;
    private CompetitionLogic competitionLogic;

    public void setDistancePartDao(DistancePartDao distancePartDao) {
        this.distancePartDao = distancePartDao;
    }

    public void setLapPartDao(LapPartDao lapPartDao) {
        this.lapPartDao = lapPartDao;
    }

    public void setCompetitionLogic(CompetitionLogic competitionLogic) {
        this.competitionLogic = competitionLogic;
    }

    public void calculate(String fileName) throws IOException {

        List<DistancePart> distanceParts = distancePartDao.list();

        Map<Integer, List<DistancePart>> map = new HashMap<Integer, List<DistancePart>>();
        for (DistancePart distancePart : distanceParts) {
            distancePart.setFailCount(competitionLogic.getTeamFailCount(distancePart));
            distancePart.setCompleteFailCount(competitionLogic.getTeamCompleteFailCount(distancePart));
            Group group = distancePart.getTeam().getGroup();
            if (!map.containsKey(group.getNumber())) {
                map.put(group.getNumber(), new LinkedList<DistancePart>());
            }
            List<DistancePart> list = map.get(group.getNumber());
            if (distancePart.isComplete()) {
                list.add(distancePart);
            }
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fileOut = new FileOutputStream(fileName);

        PlaceComparator placeComparator = new PlaceComparator();
        for (Integer groupNumber : map.keySet()) {
            List<DistancePart> list = map.get(groupNumber);
            Collections.sort(list, placeComparator);

            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            ArrayList<String> header = new ArrayList<String>();
            header.add("пп");
            header.add("Номер команды");
            header.add("Команда");
            header.add("Образовательное учреждение");
            header.add("Муниципальное образование");
            header.add("Состав");
            header.add("Ранг");

            List<Lap> laps = competitionLogic.findLapsByGroupNumber(groupNumber);
            for (Lap lap : laps) {
                header.add(lap.getNumber() + " " + lap.getName());
                
            }
            header.add("Сумма отсечек");
            header.add("Время на дистанции");
            header.add("Штраф за отметку");
            header.add("Результат");
            header.add("Количество снятий");
            header.add("Место");
            header.add("Результат");
            header.add("Очки в зачет");
            header.add("Процент от времени победителя");
            data.add(header);

            for (int i = 0 ; i < list.size(); i++) {
                DistancePart distancePart = list.get(i);
                ArrayList<String> row = new ArrayList<String>();
                row.add(Integer.toString(i+1));
                row.add(distancePart.getTeam().getFullNumber());
                row.add(distancePart.getTeam().getName());
                row.add(distancePart.getTeam().getDelegation().getOrganization());
                row.add(distancePart.getTeam().getDelegation().getRegion());
                row.add(competitionLogic.getTeamMembersString(distancePart.getTeam()));
                row.add("нк "); //TODO

                List<LapPart> lapParts = competitionLogic.findLapPartsByDistancePart(distancePart);

                for(LapPart lapPart : lapParts) {
                    if (lapPart.getLapState() == LapPart.FAIL_STATE) {
                        row.add("сн");
                    } else {
                        row.add("");
                    }
                }

                if (distancePart.getCompleteFailCount() > 0) {
                    row.add("");
                    row.add("");
                    row.add("");
                    row.add("");
                } else {
                    row.add(distancePart.getMinusTime().toString());
                    row.add(distancePart.getFinishTime().toString());
                    row.add(distancePart.getPlusTime().toString());
                    row.add(distancePart.getResult().toString());
                }
                row.add(distancePart.getFailCount().toString());
                if (distancePart.getCompleteFailCount() > 0) {
                    row.add("cнятие с дист");
                } else {
                    row.add(distancePart.getFinishOrder().toString());
                }


                row.add(distancePart.getScores() != null ? distancePart.getScores().toString() : "0");
                row.add("нв");
                data.add(row);
            }


            HSSFSheet sheet = wb.createSheet("Группа " + groupNumber.toString());
            ScriptUtils.printTableToSheet(sheet, data);
        }
        wb.write(fileOut);
        fileOut.close();

    }

    private class PlaceComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            DistancePart distancePart0 = (DistancePart) arg0;
            DistancePart distancePart1 = (DistancePart) arg1;

            if (distancePart0.getCompleteFailCount() > 0) {
                if (distancePart1.getCompleteFailCount() > 0) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                if (distancePart1.getCompleteFailCount() > 0) {
                    return -1;
                }
            }

            int fail = distancePart0.getFailCount() - distancePart1.getFailCount();

            if (fail > 0 || fail < 0) {
                return fail;
            }

            long result = distancePart0.getResult().getTime() - distancePart1.getResult().getTime();

            if (result > 0 || result < 0) {
                return (int) (result);
            }

            return 0;
        }
    }
}
