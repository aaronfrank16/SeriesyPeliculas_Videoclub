package modelo;

import controlador.UsuariosFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named(value = "userLog")
@SessionScoped
public class UserLog implements Serializable {

    private String correo;
    private String contraseña;
    private int idCompra = 0;
    private int idCompraSerie = 0;
    private Exception failure;

    private UsuariosFacade usuarioFacade;

    public UserLog() {

    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdCompraSerie() {
        return idCompraSerie;
    }

    public void setIdCompraSerie(int idCompraSerie) {
        this.idCompraSerie = idCompraSerie;
    }

    public void submit() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (correo.isEmpty() || contraseña.isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los datos no son validos", "Error"));
        } else {
            usuarioFacade = new UsuariosFacade();
            if (usuarioFacade.buscarUsuario(correo, contraseña)) {
                try {
                    FacesContext contex = FacesContext.getCurrentInstance();
                    contex.getExternalContext().redirect("/Videoclub/faces/view/Home.xhtml");
                } catch (Exception e) {
                    System.out.println("Me voy al carajo, no funciona esta redireccion");
                }
            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Usuario no hallado", "Advertencia"));
            }
        }
    }

    public String logOutUser() {
        failure = null;
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tu no estas Logeado.", null));
        context.getExternalContext().invalidateSession();
        return "Login.xhtml?faces-redirect=true";
    }

    public boolean IsOnline(String email) {
        if (email.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean IsOffline(String email) {
        if (email.isEmpty()) {
            return true;
        }
        return false;
    }

}
