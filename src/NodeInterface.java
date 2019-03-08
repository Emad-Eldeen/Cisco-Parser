public class NodeInterface {
    Board board;
    String transceiver;
    String seialNumber;
    int portId;

    public NodeInterface (Board board)
    {
        this.board=board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setSeialNumber(String seialNumber) {
        this.seialNumber = seialNumber;
    }

    public void setTransceiver(String transceiver) {
        this.transceiver = transceiver;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }
}
