/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.scripts.enter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import secretar.dao.DistancePartDao;
import secretar.objects.DistancePart;
import secretar.scripts.ScriptUtils;

/**
 *
 * @author sfilatov
 */
public class ChipExporter {
    private DistancePartDao distancePartDao;

    public void setDistancePartDao(DistancePartDao distancePartDao) {
        this.distancePartDao = distancePartDao;
    }

    
    
    public void exportChipInformation(String name) throws FileNotFoundException, IOException {
        List<DistancePart> distancePartList = distancePartDao.list();
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        ArrayList<String> header = new ArrayList<String>();
        header.add("Группа");
        header.add("ФИ");
        header.add("Колл");
        header.add("Квал(код)");
        header.add("Номер");
        header.add("ГР");
        header.add("SI");
        header.add("Comment");
        data.add(header);

        for (DistancePart distancePart : distancePartList) {
            ArrayList<String> row = new ArrayList<String>();
            row.add("группа");
            row.add(distancePart.getTeam().getName());
            row.add(distancePart.getTeam().getDelegation().getName());
            row.add("0");
            row.add(distancePart.getTeam().getFullNumber());
            row.add("0");
            row.add(distancePart.getTeam().getChipNumber());
            row.add("");
            data.add(row);
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        File file = new File(name);
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fileOut = new FileOutputStream(name);
        HSSFSheet sheet = wb.createSheet("SI data");
        ScriptUtils.printTableToSheet(sheet, data);
        wb.write(fileOut);
        fileOut.close();
    }

}
