/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.scripts.enter;

import secretar.dao.CompetitionDao;
import secretar.dao.DistanceDao;
import secretar.dao.GroupDao;
import secretar.dao.LapDao;
import secretar.objects.Competition;
import secretar.objects.Distance;
import secretar.objects.Group;
import secretar.objects.Lap;

/**
 *
 * @author Степан
 */
public class CompetitionParser {

    private GroupDao groupDao;
    private CompetitionDao competitionDao;
    private DistanceDao distanceDao;
    private LapDao lapDao;

    public void setCompetitionDao(CompetitionDao competitionDao) {
        this.competitionDao = competitionDao;
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

    public void loadCompetition(){
        lapDao.deleteAll();
        groupDao.deleteAll();
        distanceDao.deleteAll();
        competitionDao.deleteAll();
        
        Group group1 = groupDao.add(new Group(1, "10-13, 1 класс"));
        Group group2 = groupDao.add(new Group(2, "14-15, 1 класс"));
        Group group3 = groupDao.add(new Group(3, "16-21, 1 класс"));
        Group group4 = groupDao.add(new Group(4, "14-15, 3 класс"));
        Group group5 = groupDao.add(new Group(5, "16-старше, 3 класс"));

        Competition competition = competitionDao.add(new Competition("Матч городов 2009"));
        Distance distance1 = new Distance("1 класс");
        distance1.setCompetition(competition);
        distance1 = distanceDao.add(distance1);
        group1.setDistance(distance1);
        groupDao.update(group1);
        group2.setDistance(distance1);
        groupDao.update(group2);
        group3.setDistance(distance1);
        groupDao.update(group3);


        Distance distance2 = new Distance("3 класс");
        distance2.setCompetition(competition);
        distance2 = distanceDao.add(distance2);
        group4.setDistance(distance2);
        groupDao.update(group4);
        group5.setDistance(distance2);
        groupDao.update(group5);

        lapDao.create(new Lap(0, "Ориентирование", distance1));
        lapDao.create(new Lap(1, "Спуск по склону по перилам", distance1));
        lapDao.create(new Lap(2, "Переправа по веревке с перилами", distance1));
        lapDao.create(new Lap(3, "Переправа по бревну по перилам", distance1));
        lapDao.create(new Lap(4, "Подъем по склону по перилам", distance1));
        lapDao.create(new Lap(5, "Переправа по качающемуся бревну по перилам", distance1));

        lapDao.create(new Lap(0, "Ориентирование", distance2));
        lapDao.create(new Lap(1, "Переправа по бревну по перилам", distance2));
        lapDao.create(new Lap(2, "Переправа по горизонтальным перилам", distance2));
        lapDao.create(new Lap(3, "Переправа по горизонтальным перилам 2", distance2));
        lapDao.create(new Lap(4, "Спуск по склону по перилам", distance2));
        lapDao.create(new Lap(5, "ПКВ", distance2));
        lapDao.create(new Lap(6, "Переправа по наклонным вверх горизонтальным перилам", distance2));
        lapDao.create(new Lap(7, "Переправа по веревке с перилами", distance2));
        lapDao.create(new Lap(8, "Спуск по перилам", distance2));

    }


}
