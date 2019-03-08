import java.awt.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version {
    String versionSection=null;

    public Version(String versionSection)
    {
        this.versionSection=versionSection;
    }

    public String getRouterType()
    {
        String routerType=null;
        //pattern for getting router type
        if (versionSection!=null){
            //check the section line by line
            Scanner scanner = new Scanner(versionSection);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                //case 1. ASR 9010
                if (line.contains("ASR 9010"))
                {
                    routerType="ASR 9010";
                    return routerType;
                }

                else if(line.contains("ASR9K"))
                {
                    return routerType="ASR9K";
                }

                //
                else if(line.contains("Cisco CISCO")||line.contains("cisco WS-")||line.contains("cisco ME-"))
                {

                    String typeString=null;
                    if(line.contains("Cisco CISCO"))
                    {
                        typeString="Cisco CISCO(.+?) ";
                    }
                    if(line.contains("cisco WS-"))
                    {
                        typeString="cisco WS-(.+?) ";
                    }
                    if(line.contains("cisco ME-"))
                    {
                        typeString="cisco ME-(.+?) ";
                    }


                    Pattern typePattern = Pattern.compile(typeString);
                    Matcher typeMatcher = typePattern.matcher(line);
                    while (typeMatcher.find())
                    {
                        routerType= typeMatching(typeMatcher.group(1));
                        return routerType;
                    }
                }
            }
        }
        return routerType;
    }

    private String typeMatching (String dumpType)
    {
        switch (dumpType)
        {
            case "7609-S": return "Cisco 7609 Router";
            case "7613": return "Cisco 7613 Router";
            case "3400E-24TS-M": return "Cisco ME 3400E Switch";
            case "C4948": return "Cisco Catalyst 4948";
        }
        return null;
    }
}
