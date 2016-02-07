package io;

/**
 *
 * @author lsdpirate
 */
public enum CommunicationProtocol {

    SONG_HEADER("SN"),
    PLAY_COMMAND_HEADER("PL"),
    PAUSE_COMMAND_HEADER("PS"),
    STOP_COMMAND_HEADER("ST"),
    VOLUME_SET_HEADER("VL"),
    INCOMING_DATA("ID"),
    
    START_OF_FILE("SOF"),
    END_OF_FILE("EOF"),
    //Reserved for player-to-client communication
    PLAYING_NEXT("PN"),
    SHUTDOWN_CODE("SC"),
    STOP_STREAM_CODE("SSC");

    private String value;

    CommunicationProtocol(String value) {
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
