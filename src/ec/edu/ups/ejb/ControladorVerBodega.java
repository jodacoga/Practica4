package ec.edu.ups.ejb;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ec.edu.ups.modelos.Bodega;
import ec.edu.ups.modelos.Existencia;
import ec.edu.ups.modelos.RolUsuario;
import ec.edu.ups.modelos.Usuario;

@FacesConfig(version = Version.JSF_2_3)
@Named
@RequestScoped
public class ControladorVerBodega implements Serializable {

	private static final long serialVersionUID = -5007561653049510115L;

	private Bodega bodega;
	
	public ControladorVerBodega() {
	}
	
	@PostConstruct
	public void init() {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();
		try {
			Usuario usuario = (Usuario) contexto.getSessionMap().get("usuario-conectado");
			if (usuario != null && usuario.getRol() == RolUsuario.ADMINISTRADOR) {
				bodega = (Bodega) contexto.getSessionMap().get("bodega");
			} else {
				contexto.redirect("/Practica4/inicio-sesion.xhtml?faces-redirect=true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Bodega getBodega() {
		return bodega;
	}
	
	public void setBodega(Bodega bodega) {
		this.bodega = bodega;
	}
	
	public void editarExistenciasProducto(Existencia existencia) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			contexto.getExternalContext().getSessionMap().put("existencia", existencia);
			contexto.getExternalContext().redirect("ver-existencia.xhtml?faces-redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
