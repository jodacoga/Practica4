package ec.edu.ups.ejb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ec.edu.ups.jpa.UsuarioDAO;
import ec.edu.ups.modelos.RolUsuario;
import ec.edu.ups.modelos.Usuario;

@FacesConfig(version = Version.JSF_2_3)
@Named
@SessionScoped
public class ControladorSesion implements Serializable {

	private static final long serialVersionUID = -3931008683335963700L;

	@EJB
	private UsuarioDAO usuarioDAO;
	
	private String correo;
	private String contrasenia;
	private Usuario usuario;
	
	public ControladorSesion() {
	}
	
	@PostConstruct
	public void init() {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();
		try {
			//contexto.getSession(true);
			usuario = (Usuario) contexto.getSessionMap().get("usuario-conectado");
			if (usuario != null) {
				if (usuario.isActivo()) {
					if (usuario.getRol() == RolUsuario.ADMINISTRADOR) {
						contexto.redirect("administrador/consulta-bodegas.xhtml?faces-redirect=true");
					} else if (usuario.getRol() == RolUsuario.EMPLEADO) {
						contexto.redirect("empleado/consulta-clientes.xhtml?faces-redirect=true");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getCorreo() {
		return correo;
	}
	
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public String getContrasenia() {
		return contrasenia;
	}
	
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void iniciarSesion() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			usuario = usuarioDAO.buscarPorCorreo(correo);
			if (usuario.isActivo()) {
				if (usuario.getContrasenia().equals(contrasenia)) {
					contexto.getExternalContext().getSessionMap().put("usuario-conectado", usuario);
					if (usuario.getRol() == RolUsuario.ADMINISTRADOR) {
						contexto.getExternalContext().redirect("administrador/consulta-bodegas.xhtml?faces-redirect=true");
					} else if (usuario.getRol() == RolUsuario.EMPLEADO) {
						contexto.getExternalContext().redirect("empleado/consulta-clientes.xhtml?faces-redirect=true");
					}
				} else {
					contexto.addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña es incorrecta.", "")
					);
				}
			} else {
				contexto.addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Este usuario esta inhabilitado.", "")
				);
			}
		} catch (Exception e) {
			contexto.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario con el correo especificado no existe.", "")
			);
		}
	}
	
	public void cerrarSesion() {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();
		try {
			contexto.getSessionMap().clear();
			contexto.invalidateSession();
			contexto.redirect("/Practica4/inicio-sesion.xhtml?faces-redirect=true");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirigir(String url) {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();
		try {
			contexto.getSessionMap().remove("bodega");
			contexto.getSessionMap().remove("existencia");
			contexto.redirect("/Practica4/" + url + "?faces-redirect=true");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
