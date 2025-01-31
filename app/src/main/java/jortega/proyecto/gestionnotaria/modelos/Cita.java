package jortega.proyecto.gestionnotaria.modelos;

public class Cita {

    private int id;
    private int cliente_id;
    private int notario_id;
    private int despacho_id;
    private String fecha_cita;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public int getNotario_id() {
        return notario_id;
    }

    public void setNotario_id(int notario_id) {
        this.notario_id = notario_id;
    }

    public int getDespacho_id() {
        return despacho_id;
    }

    public void setDespacho_id(int despacho_id) {
        this.despacho_id = despacho_id;
    }

    public String getFecha_cita() {
        return fecha_cita;
    }

    public void setFecha_cita(String fecha_cita) {
        this.fecha_cita = fecha_cita;
    }
}
