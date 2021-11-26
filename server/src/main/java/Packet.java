public class Packet {

    public enum Header
    {
        INVALID,
        SERVER_CMD,
        PEER_MSG,
        BROADCAST_MSG
    }

    private Header type = Header.INVALID;
    private Object content = null;

    public Packet()
    {

    }
    public Packet(Header type, String content) {
        this.type = type;
        this.content = content;
    }

    public Header getType() {
        return type;
    }

    public void setType(Header type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
