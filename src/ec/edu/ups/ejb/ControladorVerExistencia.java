package ec.edu.ups.ejb;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ec.edu.ups.jpa.BodegaDAO;
import ec.edu.ups.modelos.Bodega;
import ec.edu.ups.modelos.Existencia;
import ec.edu.ups.modelos.RolUsuario;
import ec.edu.ups.modelos.Usuario;

@FacesConfig(version = Version.JSF_2_3)
@Named
@RequestScoped
public class ControladorVerExistencia implements Serializable {

	private static final long serialVersionUID = -4723075873529578065L;

	@EJB
	private BodegaDAO bodegaDAO;
	
	private Bodega bodega;
	private Existencia existencia;
	
	public ControladorVerExistencia() {
	}
	
	@PostConstruct
	public void init() {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();
		try {
			Usuario usuario = (Usuario) contexto.getSessionMap().get("usuario-conectado");
			if (usuario != null && usuario.getRol() == RolUsuario.ADMINISTRADOR) {
				bodega = (Bodega) contexto.getSessionMap().get("bodega");
				existencia = (Existencia) contexto.getSessionMap().get("existencia");
			} else {
				contexto.redirect("/Practica4/inicio-sesion.xhtml?faces-redirect=true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Existencia getExistencia() {
		return existencia;
	}
	
	public void setExistencia(Existencia existencia) {
		this.existencia = existencia;
	}
	
	public void guardar() {
		List<Existencia> inventario = bodega.getInventario().stream()
							  .map(aux -> aux.getId() == existencia.getId()? existencia: aux)
							  .collect(Collectors.toList());
		bodega.setInventario(inventario);
		bodegaDAO.modificar(bodega);
		FacesContext.getCurrentInstance().addMessage(null, 
			new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambios guardados correctamente.", "")
		);
	}
}
