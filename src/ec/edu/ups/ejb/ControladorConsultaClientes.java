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

import ec.edu.ups.jpa.ClienteDAO;
import ec.edu.ups.modelos.Cliente;
import ec.edu.ups.modelos.RolUsuario;
import ec.edu.ups.modelos.Usuario;

@FacesConfig(version = Version.JSF_2_3)
@Named
@RequestScoped
public class ControladorConsultaClientes implements Serializable {

	private static final long serialVersionUID = -7081411065252891089L;

	@EJB
	private ClienteDAO clientesDAO;
	
	private List<Cliente> clientes;
	
	public ControladorConsultaClientes() {
	}
	
	@PostConstruct
	public void init() {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();	
		try {
			Usuario usuario = (Usuario) contexto.getSessionMap().get("usuario-conectado");
			if (usuario != null && usuario.getRol() == RolUsuario.EMPLEADO){
				clientes = clientesDAO.listar();
			} else {
				contexto.redirect("/Practica4/inicio-sesion.xhtml?faces-redirect=true");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Cliente> getClientes() {
		return clientes;
	}
	
	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
	
	public void editarCliente(Cliente cliente) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			contexto.getExternalContext().getSessionMap().put("cliente", cliente);
			contexto.getExternalContext().redirect("editar-cliente.xhtml?faces-redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
