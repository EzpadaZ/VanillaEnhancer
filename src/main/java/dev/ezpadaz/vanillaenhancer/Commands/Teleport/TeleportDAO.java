package dev.ezpadaz.vanillaenhancer.Commands.Teleport;

public class TeleportDAO {
    private final String origen;
    private final String destino;
    private final boolean isGoingTo;

    public TeleportDAO(String o, String d, boolean i) {
        this.origen = o;
        this.destino = d;
        this.isGoingTo = i;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public boolean isGoingTo() {
        return isGoingTo;
    }
}
