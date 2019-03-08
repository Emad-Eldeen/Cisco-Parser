import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class MainParser {
    //creating new router
    Router router;
    Path path;
    String commandStartKeyRegex ="#";
    ArrayList<String> commandsList;
    HashMap<String,String> commandsSections;

    //constructor and set the commands
    public MainParser (Path path) throws IOException {
        this.path=path;
        this.router= new Router();
        this.commandsList=new ArrayList<String>();
        this.commandsSections=new HashMap<String, String>();
        commandsList.add("show version");
        commandsList.add("show inventory");
        commandsList.add("show lldp neighbors");
        commandsList.add("show lldp neighbors detail");
        commandsList.add("show cdp neighbors");
        commandsList.add("show arp");
        commandsList.add("show route");
        commandsList.add("show ip route vrf *");
        commandsList.add("show access-lists");
        commandsList.add("show configuration");
        commandsList.add("show configuration running");
        commandsList.add("show module all");
    }


    public Router parse() {

//        BufferedReader reader = null;
//        try {
//            reader = Files.newBufferedReader(path);
//        StringBuffer fullDump= new StringBuffer();
//
//        //iterate line-by-line over the file and store it into a string buffer
//        while (true)
//        {
//            String dumpLine = reader.readLine();
//            if (dumpLine == null) {
//                break;
//            }
//            fullDump.append("\\r\\n"+dumpLine);
//        }


        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(path);
            String fullDump = new String(encoded,"UTF-8" );
            //set router file name
            router.setFileName(path.getFileName().toString());

            //get the command sections
            getCommands(fullDump);
            parseCommands(commandsSections);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return router;
    }

    //a method to convert the dump to a set of commands
    private void getCommands(String fullDump)
    {
        Pattern p = Pattern.compile(commandStartKeyRegex);
        String[] commandSectionArray = p.split(fullDump);


        for (String commadsection: commandSectionArray)
        {
            for(String command : commandsList )
            {
               if(commadsection.matches("^"+command+"(?s).+"))
               {
                   commandsSections.put(command,commadsection);
               }

            }
        }
    }

    private void parseCommands (HashMap<String,String> commandsSections)
    {
        Inventory inventory = new Inventory(commandsSections.get("show inventory"));
        Version version = new Version(commandsSections.get("show version"));
        Configuration configuration = new Configuration(commandsSections.get("show configuration"));
        Module module = new Module(commandsSections.get("show module all"));

        //router type
        router.setRouterType(parseRouterType(inventory,version));

        //router Name
        router.setRouterName(parseRouterName(configuration));

        //router boards
        parseBoards(inventory, module,router);

    }

    private String parseRouterType(Inventory inventory, Version version)
    {
        String routerType=null;
        //first priority
        routerType= inventory.getRouterType();
        //second priority
        if (routerType==null)
        {
            routerType=version.getRouterType();
        }
        return routerType;
    }

    private String parseRouterName(Configuration configuration)
    {
        String routerName=null;
        routerName= configuration.getRouterName();
        return routerName;
    }

    private void parseBoards(Inventory inventory,Module module , Router router)
    {
        //try to parse the boards from inventory command
        inventory.getBoards(router);

        //try to parse the boards from module command
        if(router.getRouterBoards().size()==0)
        {
            module.getBoards(router);
        }
    }

}
