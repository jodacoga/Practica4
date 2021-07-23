package ec.edu.ups.ejb;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ec.edu.ups.jpa.BodegaDAO;
import ec.edu.ups.jpa.ClienteDAO;
import ec.edu.ups.jpa.ExistenciaDAO;
import ec.edu.ups.jpa.FacturaDAO;
import ec.edu.ups.jpa.ProductoDAO;
import ec.edu.ups.modelos.Bodega;
import ec.edu.ups.modelos.Cliente;
import ec.edu.ups.modelos.Existencia;
import ec.edu.ups.modelos.Factura;
import ec.edu.ups.modelos.FacturaDetalle;
import ec.edu.ups.modelos.PedidoDetalle;
import ec.edu.ups.modelos.RolUsuario;
import ec.edu.ups.modelos.Usuario;

@FacesConfig(version = Version.JSF_2_3)
@Named
@SessionScoped
public class ControladorAgregarFactura implements Serializable {

	private static final long serialVersionUID = 4498173608642235813L;

	@EJB
	private FacturaDAO facturaDAO;
	
	@EJB
	private ClienteDAO clienteDAO;
	
	@EJB
	private ExistenciaDAO existenciaDAO;
	
	@EJB
	private ProductoDAO productoDAO;
	
	@EJB
	private BodegaDAO bodegaDAO;
	
	private String cedula;
	private int cantidad;
	
	private Factura factura;
	private int productoID;
	private List<Existencia> existencias;
	
	private String mensaje;
	
	public ControladorAgregarFactura() {
	}
	
	@PostConstruct
	public void init() {
		ExternalContext contexto = FacesContext.getCurrentInstance().getExternalContext();	
		try {
			Usuario usuario = (Usuario) contexto.getSessionMap().get("usuario-conectado");
			if (usuario != null && usuario.getRol() == RolUsuario.EMPLEADO){
				factura = new Factura();
			} else {
				contexto.redirect("/Practica4/inicio-sesion.xhtml?faces-redirect=true");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getCedula() {
		return cedula;
	}
	
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	
	public Factura getFactura() {
		return factura;
	}
	
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	public int getProductoID() {
		return productoID;
	}
	
	public void setProductoID(int productoID) {
		this.productoID = productoID;
	}
	
	public List<Existencia> getExistencias() {
		existencias = existenciaDAO.listar();
		return existencias;
	}
	
	public void setExistencias(List<Existencia> existencias) {
		this.existencias = existencias;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public void buscarCliente() {
		Cliente cliente = clienteDAO.buscarPorCedula(cedula);
		if (cliente == null) {
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "No existe ningún cliente con la cédula especificada.", "")
			);
		} else {
			factura.setCliente(cliente);
		}
		cedula = "";
	}
	
	public void agregarProducto() {
		existencias.forEach(existencia -> {
			boolean yaFueAgregado = false;
			if (existencia.getProducto().getId() == productoID) {
				if (cantidad <= existencia.getCantidad()) {
					for (int i = 0; i < factura.getDetalles().size() && factura.getDetalles().size() > 0; i++) {
						if (factura.getDetalles().get(0).getProducto().equals(existencia.getProducto())) {
							yaFueAgregado = true;
							break;
						}
					}
					if (!yaFueAgregado) {
						FacturaDetalle detalle = new FacturaDetalle();
						detalle.setCantidad(cantidad);
						detalle.setProducto(existencia.getProducto());
						factura.getDetalles().add(detalle);
						factura.calcularSubtotal();
						factura.calcularTotal();
						productoID = 0;
						cantidad = 0;
					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(
							FacesMessage.SEVERITY_INFO, 
							"Existencias insuficientes. Solo existen " + existencia.getCantidad() + " unidades.",
							""
						)
					);
				}
			}
		});
	}
	
	public void quitarProducto(FacturaDetalle detalle) {
		factura.getDetalles().remove(detalle);
		factura.calcularSubtotal();
		factura.calcularTotal();
	}
	
	public void guardar() {
		for (int i = 0; i < existencias.size(); i++) {
			Existencia existencia = existencias.get(i);
			for (int j = 0; j < factura.getDetalles().size(); j++) {
				FacturaDetalle facturaDetalle = factura.getDetalles().get(j);
				if (existencia.getProducto().getId() == facturaDetalle.getProducto().getId()
					&& existencia.getCantidad() >= facturaDetalle.getCantidad()) {
					existencia.setCantidad(existencia.getCantidad() - facturaDetalle.getCantidad());
					existenciaDAO.modificar(existencia);
					break;
				}
			}
		}
		facturaDAO.agregar(factura);
		factura = new Factura();
		productoID = 0;
		cedula = "";
		cantidad = 0;
		FacesContext.getCurrentInstance().addMessage(null, 
			new FacesMessage(FacesMessage.SEVERITY_INFO, "Factura generada correctamente.", "")
		);
	}
}
