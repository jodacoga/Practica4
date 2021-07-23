package ec.edu.ups.ejb;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ec.edu.ups.jpa.BodegaDAO;
import ec.edu.ups.modelos.Bodega;
import ec.edu.ups.modelos.RolUsuario;
import ec.edu.ups.modelos.Usuario;

@FacesConfig(version = Version.JSF_2_3)
@Named
@RequestScoped
public class ControladorConsultaBodegas implements Serializable {

	private static final long serialVersionUID = -3000655854987727138L;

	@EJB
	private BodegaDAO bodegaDAO;
	
	private List<Bodega> bodegas;
	
	public ControladorConsultaBodegas() {
	}
	
	@PostConstruct
	public void init() {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();	
		try {
			Usuario usuario = (Usuario) contexto.getSessionMap().get("usuario-conectado");
			if (usuario != null && usuario.getRol() == RolUsuario.ADMINISTRADOR){
				bodegas = bodegaDAO.listar();
			} else {
				contexto.redirect("/Practica4/inicio-sesion.xhtml?faces-redirect=true");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Bodega> getBodegas() {
		return bodegas;
	}
	
	public void setBodegas(List<Bodega> bodegas) {
		this.bodegas = bodegas;
	}
	
	public void verDetalle(Bodega bodega) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			contexto.getExternalContext().getSessionMap().put("bodega", bodega);
			contexto.getExternalContext().redirect("ver-bodega.xhtml?faces-redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
