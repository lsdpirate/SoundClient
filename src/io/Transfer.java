package io;

/**
 *
 * @author lsdpirate
 */
public class Transfer {
    private String transferName;
    private int transferStatus;

    public Transfer(String transferName) {
        this.transferName = transferName;
    }
    
    private Transfer(){};
    
    public void setTransferStatus(int status){
        transferStatus = status;
        System.out.println(transferName + " " + transferStatus);
    }
    
    public void incrementStatus(int increment){
        transferStatus += increment;
    }

    public String getTransferName() {
        return transferName;
    }

    public int getTransferStatus() {
        return transferStatus;
    }
    
    
    
    
}
