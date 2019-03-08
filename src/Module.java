import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Module {
    String moduleSection=null;

    public Module(String moduleSection)
    {
        this.moduleSection=moduleSection;
    }

    public void getBoards(Router router) {


        //split the section into mod/sub-mod
        String firstSectionRegex = "(Mod.+)(Ports.+)(Card.*Type.+)(Model.+)(Serial.+)";
        String secondSectionRegex = "(Mod.+)(Sub.Module.+)(Model.+)(Serial.+)(Hw.+)(Status.+)";
        String splitter = "\\n\\n"; //an empty line

        String[] moduleSubSections = splitModuleSubmodule(firstSectionRegex, secondSectionRegex, splitter);

        //parse section 1: mother boards
        if (moduleSubSections[0] != null) {

            String motherboardRegex = "^\\s*\\d+\\s+\\d+\\s+.+";

            Scanner scanner1 = new Scanner(moduleSubSections[0]);
            String firstLine = scanner1.nextLine();
            Pattern motherboardPattern = Pattern.compile(firstSectionRegex, Pattern.CASE_INSENSITIVE);
            Matcher motherboardMatcher = motherboardPattern.matcher(firstLine);
            //match the first line and get the groups
            if (motherboardMatcher.find()) {

                while (scanner1.hasNextLine()) {
                    String line = scanner1.nextLine();
                    //mother board lines
                    if (line.matches(motherboardRegex)) {
                        Board board = router.createBoard();
                        board.setIndexOnSlot(-1);
                        board.setCabinetIndex(1);
                        board.setShelfIndex(1);
                        board.setSlotIndex(Integer.valueOf(line.substring(motherboardMatcher.start(1), motherboardMatcher.end(1)).trim()));
                        //get a sub string from the line indicated by start and end of matcher groups
                        board.setBoardDescription(line.substring(motherboardMatcher.start(3), motherboardMatcher.end(3)).trim());
                        board.setBoardType(line.substring(motherboardMatcher.start(4), motherboardMatcher.end(4)).trim());
                        board.setSerialNumber(line.substring(motherboardMatcher.start(5)).trim());
                        System.out.println("s");
                    }

                }
            }
        }


        //parse section 2: daughter boards
        if (moduleSubSections[1] != null) {

            String daughterboardRegex = "^\\s*\\d\\/\\d+\\s+.+?\\s+.+";

            Scanner scanner2 = new Scanner(moduleSubSections[1]);
            String firstLine = scanner2.nextLine();
            Pattern daughterboardPattern = Pattern.compile(secondSectionRegex, Pattern.CASE_INSENSITIVE);
            Matcher daughterboardMatcher = daughterboardPattern.matcher(firstLine);

            //match the first line and get the groups
            if (daughterboardMatcher.find()) {

                while (scanner2.hasNextLine()) {
                    String line = scanner2.nextLine();
                    //mother board lines
                    if (line.matches(daughterboardRegex)) {

                        //things to be done in this case
                    }

                }
            }
        }
    }


    //helper method to split module into module/sub-module sections
    private String[] splitModuleSubmodule(String firstSectionRegex,String secondSectionRegex,String splitter)
    {
        String[]moduleSubSections=new String[2];
        Pattern firstSectionPattern = Pattern.compile(firstSectionRegex,Pattern.CASE_INSENSITIVE);
        Pattern secondSectionPattern = Pattern.compile(secondSectionRegex,Pattern.CASE_INSENSITIVE);

        if(moduleSection!=null)
        {
            //split based on the splitter
            Pattern p = Pattern.compile(splitter);
            String[] temp= p.split(moduleSection);

            for (String section:temp) {
                //get the first subsection
                Matcher firstSectionMatcher = firstSectionPattern.matcher(section);
                if(firstSectionMatcher.find())
                {
                    moduleSubSections[0]=section;
                }
                //get the second subsection
                Matcher secondSectionMatcher = secondSectionPattern.matcher(section);
                if (secondSectionMatcher.find())
                {
                    moduleSubSections[1]=section;
                }
            }
        }

        return moduleSubSections;
    }

}
