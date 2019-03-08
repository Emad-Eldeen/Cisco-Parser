import java.util.ArrayList;

public class Router {
    private String routerType=null;
    String routerName=null;
    ArrayList<Board> routerBoards= new ArrayList<Board>();
    ArrayList<NodeInterface> routerNodeInterfaces = new ArrayList<NodeInterface>();
    String fileName=null;

    public String getRouterType() {
        return routerType;
    }

    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public Board createBoard()
    {
        Board board = new Board();
        routerBoards.add(board);
        return board;
    }

    public NodeInterface createNodeInterface(int cabinetIndex, int shelfIndex, int slotIndex, int indexOnSlot)
    {
        Board board = getBoard(cabinetIndex,shelfIndex,slotIndex,indexOnSlot);
        NodeInterface nodeInterface = new NodeInterface(board);
        routerNodeInterfaces.add(nodeInterface);
        return nodeInterface;
    }

    public Board getBoard(int cabinetIndex,int shelfIndex, int slotIndex, int indexOnSlot)
    {
        for(Board board: routerBoards)
        {
            if(board.getCabinetIndex()==cabinetIndex && board.getShelfIndex()==shelfIndex && board.getSlotIndex()==slotIndex
            && board.getIndexOnSlot()==indexOnSlot)
            {
                return board;
            }
        }
        return null;
    }

    public ArrayList<Board> getRouterBoards() {
        return routerBoards;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
