import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuration {
    String configurationSection = null;

    public Configuration(String configurationSection) {
        this.configurationSection = configurationSection;
    }

    public String getRouterName() {
        String routerName = null;
        if (configurationSection != null) {
        //first priority
        routerName = routerNameSearch("hostname (.+)");
        //second priority
            if (routerName==null)
            {
                routerName = routerNameSearch("switchname (.+)");
            }
            //third priority
            if (routerName==null)
            {
                routerName = routerNameSearch("Device name (.+)");
            }
        }
        return routerName;
    }

    private String routerNameSearch(String namePattern)
    {
        String routerName = null;
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(configurationSection);
        while (matcher.find()) {
            routerName = matcher.group(1);
            return routerName;
        }
        return routerName;
    }
}
