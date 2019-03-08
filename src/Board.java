public class Board {
    String boardType=null;
    String boardDescription=null;
    int slotIndex;
    int indexOnSlot;
    int cabinetIndex;
    int shelfIndex;
    String serialNumber;

    public void setCabinetIndex(int cabinetIndex) {
        //
        this.cabinetIndex = cabinetIndex;
    }

    public void setShelfIndex(int shelfIndex) {
        this.shelfIndex = shelfIndex;
    }



    public void setBoardDescription(String boardDescription) {
        this.boardDescription = boardDescription;
    }

    public void setBoardType(String boardType) {
        this.boardType = boardType;
    }

    public void setIndexOnSlot(int indexOnSlot) {
        this.indexOnSlot = indexOnSlot;
    }

    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getCabinetIndex() {
        return cabinetIndex;
    }

    public int getIndexOnSlot() {
        return indexOnSlot;
    }

    public int getShelfIndex() {
        return shelfIndex;
    }

    public int getSlotIndex() {
        return slotIndex;
    }
}
