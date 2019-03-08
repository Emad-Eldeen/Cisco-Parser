import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inventory {
    String inventorySection = null;

    public Inventory(String inventorySection) {
        this.inventorySection = inventorySection;
    }

    public String getRouterType() {
        String routerType = null;
        //pattern for getting router type
        String pattern1 = "PID: (.+?)\\s";
        if (inventorySection != null) {
            //check the section line by line
            Scanner scanner = new Scanner(inventorySection);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //this pattern preceeds the line contains router type
                if (line.matches("NAME: \"Chassis\".+")) {
                    //get next line
                    String line2 = scanner.nextLine();
                    Pattern pattern = Pattern.compile(pattern1);
                    Matcher matcher = pattern.matcher(line2);
                    while (matcher.find()) {
                        routerType = matcher.group(1);
                    }
                    return routerType;

                }
                if (line.contains("ASR 9010")) {
                    routerType = "ASR 9010";
                    return routerType;
                }
            }
        }
        return routerType;
    }

    public void getBoards(Router router) {
        //for router type ASR-9xx
        if (router.getRouterType() != null) {
            if (router.getRouterType().matches("ASR-9.+")||router.getRouterType().equalsIgnoreCase("ASR9K")) {
                String pattern1 = "NAME: \"(.+?)\".+DESCR: \"(.+?)\"";
                String pattern2 = "PID: (.+?) .+SN: (.+) *";

                String NAME=null;
                String DESC=null;
                String PID=null;
                String SN=null;

                Scanner scanner = new Scanner(inventorySection);

                while (scanner.hasNextLine()) {
                    //check line by line
                    String line = scanner.nextLine();

                    if (line.matches(pattern1)) {
                        Pattern pattern = Pattern.compile(pattern1);
                        Matcher matcher = pattern.matcher(line);

                        //to get the second line (PID and SN)
                        if(scanner.hasNextLine())
                        {
                            String line2 = scanner.nextLine();
                            Pattern secondLinePattern = Pattern.compile(pattern2);
                            Matcher secondLineMatcher = secondLinePattern.matcher(line2);
                            while (secondLineMatcher.find())
                            {
                                PID=secondLineMatcher.group(1);
                                SN=secondLineMatcher.group(2);
                            }
                        }

                        while (matcher.find()) {
                            NAME = matcher.group(1);
                            DESC = matcher.group(2);


                            //case 1. NAME ends with subslot x/y
                            String name1 = ".+subslot (\\d+)\\/(\\d+)";
                            if (NAME.matches(name1)) {
                                Board board = router.createBoard();

                                Pattern name1Patern = Pattern.compile(name1);
                                Matcher name1Matcher = name1Patern.matcher(NAME);
                                while (name1Matcher.find()) {
                                    board.setSlotIndex(Integer.valueOf(name1Matcher.group(1)));
                                    board.setIndexOnSlot(Integer.valueOf(name1Matcher.group(2)));
                                }

                                board.setBoardDescription(DESC);
                                board.setBoardType(PID);
                                board.setSerialNumber(SN);
                                board.setCabinetIndex(1);
                                board.setShelfIndex(1);
                            }


                            //case 2. Name: "module R0", "Power Supply Module 0" "Power Supply Module 1" or "Fan Tray"

                            else if (NAME.matches("module R0") || NAME.matches("Power Supply Module 0") || NAME.matches("Power Supply Module 1") || NAME.matches("Fan Tray")) {
                                Board board = router.createBoard();

                                //set slot index based on board type
                                switch (NAME) {
                                    case "module R0":
                                        board.setSlotIndex(1);
                                        break;
                                    case "Power Supply Module 0":
                                        board.setSlotIndex(3);
                                        break;
                                    case "Power Supply Module 1":
                                        board.setSlotIndex(4);
                                        break;
                                    case "Fan Tray":
                                        board.setSlotIndex(2);
                                        break;
                                }

                                board.setBoardDescription(DESC);
                                board.setBoardType(PID);
                                board.setSerialNumber(SN);
                                board.setCabinetIndex(1);
                                board.setShelfIndex(1);
                                board.setIndexOnSlot(0);
                                }

                            //case 3 for SFPs
                            else if (NAME.matches("subslot (\\d+)\\/(\\d+).+transceiver.+?(\\d+)")) {
                                String name3 = "subslot (\\d+)\\/(\\d+).+transceiver.+?(\\d+)";
                                Pattern transceiverPattern = Pattern.compile(name3);
                                Matcher transceiverMatcher = transceiverPattern.matcher(NAME);
                                while (transceiverMatcher.find()) {
                                    NodeInterface nodeInterface = router.createNodeInterface(1, 1, Integer.valueOf(transceiverMatcher.group(1))
                                            , Integer.valueOf(transceiverMatcher.group(2)));
                                    nodeInterface.setPortId(Integer.valueOf(transceiverMatcher.group(3)));
                                    nodeInterface.setTransceiver(PID);
                                    nodeInterface.setSeialNumber(SN);
                                }


                            }


                            //case 4 for ASR9K. NAME: "module \d/\d/\D for boards
                            else if(NAME.matches("module \\d+\\/\\d+\\/\\D.+"))
                            {
                                Board board = router.createBoard();

                                String name4  = "module \\d+\\/(\\d+)\\/\\D.+";
                                Pattern motherboardPattern = Pattern.compile(name4);
                                Matcher motherboardMatcher = motherboardPattern.matcher(NAME);
                                while (motherboardMatcher.find()) {
                                    board.setSlotIndex(Integer.valueOf(motherboardMatcher.group(1)));
                                }

                                board.setBoardDescription(DESC);
                                board.setBoardType(PID);
                                board.setSerialNumber(SN);
                                board.setCabinetIndex(1);
                                board.setShelfIndex(1);
                                board.setIndexOnSlot(0);
                            }

                            //case 5 for ASR9K  NAME: module \d/\d/\d for daugtherboards

                            else if (NAME.matches("module \\d+\\/\\d+\\/\\d+.*"))
                            {
                                Board board = router.createBoard();

                                String name5 = "module \\d+\\/(\\d+)\\/(\\d+).+";
                                Pattern boardPattern = Pattern.compile(name5);
                                Matcher boardMatcher = boardPattern.matcher(NAME);
                                while (boardMatcher.find()) {
                                    board.setSlotIndex(Integer.valueOf(boardMatcher.group(1)));
                                    board.setIndexOnSlot(Integer.valueOf(boardMatcher.group(2)));
                                }
                                board.setCabinetIndex(1);
                                board.setShelfIndex(1);
                                board.setBoardDescription(DESC);
                                board.setSerialNumber(SN);
                                board.setBoardType(PID);
                            }

                            //case 6 for ASR9K NAME: module \d/\D\/D motherboard
                            else if (NAME.matches("module \\d+\\/\\D.+\\/\\D.+"))
                            {
                                Board board = router.createBoard();

                                String name6 = "module \\d+\\/\\D.+?(\\d+)\\/\\D.+";
                                Pattern motherboardPattern = Pattern.compile(name6);
                                Matcher motherboardMatcher = motherboardPattern.matcher(NAME);
                                while (motherboardMatcher.find()) {
                                    // if 0 --> slot index = 8
                                    if(Integer.valueOf(motherboardMatcher.group(1))==0)
                                    {
                                        board.setIndexOnSlot(8);
                                    }
                                    else {
                                        board.setSlotIndex(9);
                                    }
                                    break;
                                }
                                board.setIndexOnSlot(-1);
                                board.setCabinetIndex(1);
                                board.setShelfIndex(1);
                                board.setBoardDescription(DESC);
                                board.setSerialNumber(SN);
                                board.setBoardType(PID);
                            }

                            //case 7 for ASR9K NAME: module mau for SFPs
                            else if (NAME.matches("module mau.+"))
                            {
                                String name7 = "module mau .+?\\d+\\/(\\d+)\\/.+?\\/(\\d+)";
                                Pattern transceiverPattern = Pattern.compile(name7);
                                Matcher transceiverMatcher = transceiverPattern.matcher(NAME);
                                while (transceiverMatcher.find()) {
                                    NodeInterface nodeInterface = router.createNodeInterface(1, 1, Integer.valueOf(transceiverMatcher.group(1))
                                            , 0);
                                    nodeInterface.setPortId(Integer.valueOf(transceiverMatcher.group(2)));
                                    nodeInterface.setTransceiver(PID);
                                    nodeInterface.setSeialNumber(SN);
                                }


                            }

                        }
                    }
                }
            }

        }
    }


}
