package ec.edu.ups.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.inject.Named;

import ec.edu.ups.jpa.BodegaDAO;
import ec.edu.ups.modelos.Bodega;
import ec.edu.ups.modelos.CategoriaProducto;
import ec.edu.ups.modelos.Existencia;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

@FacesConfig(version = Version.JSF_2_3)
@Named
@SessionScoped
public class ControladorInicio implements Serializable {

	private static final long serialVersionUID = -4994414196681347445L;
	
	@EJB
	private BodegaDAO bodegaDAO;
	
	private Bodega bodega;
	private List<Bodega> bodegas;
	private List<Existencia> inventario;
	private CategoriaProducto categoria;
	
	public ControladorInicio() {
	}
	
	@PostConstruct
	public void init() {
		try {
			bodegas = bodegaDAO.listar();
			bodega = bodegas.get(0); 
			inventario = bodega.getInventario();
		} catch (NullPointerException e) {
			inventario = new ArrayList<Existencia>();
		}
	}
	
	public Bodega getBodega() {
		return bodega;
	}
	
	public void setBodega(Bodega bodega) {
		this.bodega = bodega;
	}
	
	public List<Existencia> getInventario() {
		return inventario;
	}
	
	public void setInventario(List<Existencia> inventario) {
		this.inventario = inventario;
	}
	
	public CategoriaProducto getCategoria() {
		return categoria;
	}
	
	public void setCategoria(CategoriaProducto categoria) {
		this.categoria = categoria;
	}
	
	public List<Bodega> getBodegas() {
		return bodegas;
	}
	
	public CategoriaProducto[] getCategorias() {
		return CategoriaProducto.values();
	}
	
	public void filtrarProductos() {
		if (categoria == null) {
			inventario = bodega.getInventario();
		} else {
			inventario = bodega.getInventario()
				.stream()
				.filter(aux -> aux.getProducto().getCategoria() == categoria)
				.collect(Collectors.toList());
		}
	}
}
