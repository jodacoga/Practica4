package ec.edu.ups.ejb;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ec.edu.ups.jpa.BodegaDAO;
import ec.edu.ups.jpa.FacturaDAO;
import ec.edu.ups.modelos.Bodega;
import ec.edu.ups.modelos.Existencia;
import ec.edu.ups.modelos.Factura;
import ec.edu.ups.modelos.FacturaDetalle;
import ec.edu.ups.modelos.RolUsuario;
import ec.edu.ups.modelos.Usuario;

@FacesConfig(version = Version.JSF_2_3)
@Named
@RequestScoped
public class ControladorConsultaFacturas implements Serializable {

	private static final long serialVersionUID = -191342626082319286L;

	@EJB
	private FacturaDAO facturaDAO;
	
	@EJB
	private BodegaDAO bodegaDAO;
	
	private List<Factura> facturas;
	
	public ControladorConsultaFacturas() {
	}
	
	@PostConstruct
	public void init() {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();	
		try {
			Usuario usuario = (Usuario) contexto.getSessionMap().get("usuario-conectado");
			if (usuario != null && usuario.getRol() == RolUsuario.EMPLEADO){
				facturas = facturaDAO.listar()
									 .stream()
									 .filter(aux -> aux.isActiva())
									 .collect(Collectors.toList());
			} else {
				contexto.redirect("/Practica4/inicio-sesion.xhtml?faces-redirect=true");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Factura> getFacturas() {
		facturas = facturaDAO.listar()
				 .stream()
				 .filter(aux -> aux.isActiva())
				 .collect(Collectors.toList());
		return facturas;
	}
	
	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	
	public void verDetalle(Factura factura) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		try {
			contexto.getExternalContext().getSessionMap().put("factura", factura);
			contexto.getExternalContext().redirect("editar-cliente.xhtml?faces-redirect=true");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void anular(Factura factura) {
		List<Bodega> bodegas = bodegaDAO.listar();
		for (int i = 0; i < bodegas.size(); i++) {
			Bodega bodega = bodegas.get(i);
			List<Existencia> existencias = bodega.getInventario();
			for (int j = 0; j < factura.getDetalles().size(); j++) {
				FacturaDetalle detalle = factura.getDetalles().get(j);
				for (int k = 0; k < existencias.size(); k++) {
					Existencia existencia = existencias.get(k);
					if (existencia.getProducto().getId() == detalle.getProducto().getId()) {
						existencia.setCantidad(existencia.getCantidad() + detalle.getCantidad());
					}
				}
			}
			bodega.setInventario(existencias);
			bodegaDAO.modificar(bodega);
		}
		facturaDAO.anular(factura);
	}
}
