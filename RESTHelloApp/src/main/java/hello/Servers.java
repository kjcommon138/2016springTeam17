package hello;

import java.util.List;

public class Servers {

    private final long id;
    //private final List<String> serversList;
    private final String[] serversList;

    //public Servers(long id, List<String> serversList) {
    public Servers(long id, String[] serversList) {
        this.id = id;
        this.serversList = serversList;
    }

    public long getId() {
        return id;
    }

    //public List<String> getServersList() {
    public String[] getServersList() {
        return serversList;
    }
}
