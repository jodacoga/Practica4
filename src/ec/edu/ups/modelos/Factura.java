package ec.edu.ups.modelos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "facturas") 
public class Factura implements Serializable {

	private static final long serialVersionUID = -7580515747517026669L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "factura_id")
	private List<FacturaDetalle> detalles;
	
	@Column(name = "subtotal", nullable = false)
	private double subtotal;
	
	@Column(name = "total", nullable = false)
	private double total;
	
	@Column(name = "activa", nullable = false)
	private boolean activa;
	
	private final static float IVA = 0.12f;
	
	public Factura() {
		activa = true;
		fecha = LocalDate.now();
		detalles = new ArrayList<FacturaDetalle>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}
	
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<FacturaDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<FacturaDetalle> detalles) {
		this.detalles = detalles;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public boolean isActiva() {
		return activa;
	}
	
	public void setActiva(boolean activa) {
		this.activa = activa;
	}
	
	public void calcularSubtotal() {
		subtotal = 0;
		for (FacturaDetalle detalle : detalles) {
			subtotal += detalle.calcularSubtotal();
		}
	}
	
	public void calcularTotal() {
		total = subtotal + (subtotal * IVA);
	}
}
