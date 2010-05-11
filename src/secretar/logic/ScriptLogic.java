/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.logic;

import java.io.IOException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secretar.scripts.enter.CompetitionParser;
import secretar.scripts.enter.MandatParser;
import secretar.scripts.enter.ChipExporter;
import secretar.scripts.results.ResultsByGroup;

@Transactional(propagation = Propagation.REQUIRED)
public class ScriptLogic {

    private MandatParser mandatParser;
    private CompetitionParser competitionParser;
    private ChipExporter chipExporter;
    private ResultsByGroup resultsByGroup;

    public void setMandatParser(MandatParser mandatParser) {
        this.mandatParser = mandatParser;
    }

    public void setCompetitionParser(CompetitionParser competitionParser) {
        this.competitionParser = competitionParser;
    }

    public void setChipExporter(ChipExporter chipExporter) {
        this.chipExporter = chipExporter;
    }

    public void setResultsByGroup(ResultsByGroup resultsByGroup) {
        this.resultsByGroup = resultsByGroup;
    }

    public void loadMandat(String fileName) throws IOException{
        mandatParser.loadMandat(fileName);
    }

    public void loadDistances(String fileName) throws Exception{
        competitionParser.loadCompetition();
    }

    public void exportChipData(String fileName)  throws IOException{
        chipExporter.exportChipInformation(fileName);
    }

    public void exportResultsByGroup(String fileName) throws IOException {
        resultsByGroup.calculate(fileName);
    }
}
