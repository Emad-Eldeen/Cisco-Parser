import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        String dumpPath ="C:\\Users\\emad_\\IdeaProjects\\Cisco\\Dumps";
        ArrayList<Router> routers= new ArrayList<Router>();

        File dumpFiles = new File(dumpPath);
        File[] dumpsList = dumpFiles.listFiles();

        for (File file: dumpsList)
        {
            try {
                MainParser parser = new MainParser(file.toPath());
                routers.add(parser.parse());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Router router : routers)
        {
            System.out.println("File name: "+router.getFileName() +" Router name:"+router.getRouterName()+" "+router.getRouterType()+ " boards number: "+router.getRouterBoards().size());
        }
    }

}
