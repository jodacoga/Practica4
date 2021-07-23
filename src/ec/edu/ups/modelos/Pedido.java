package ec.edu.ups.modelos;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 5832693447505281968L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	@Enumerated(value = EnumType.ORDINAL)
	@Column(name = "estado")
	private EstadoPedido estado;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "pedido_id")
	private List<PedidoDetalle> detalles;
	
	@Column(name = "subtotal", nullable = false)
	private double subtotal;
	
	public Pedido() {
		fecha = LocalDate.now();
		estado = EstadoPedido.ENVIADO;
		detalles = new ArrayList<PedidoDetalle>();
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
	
	public EstadoPedido getEstado() {
		return estado;
	}
	
	public void setEstado(EstadoPedido estado) {
		this.estado = estado;
	}
	
	public List<PedidoDetalle> getDetalles() {
		return detalles;
	}
	
	public void setDetalles(List<PedidoDetalle> detalles) {
		this.detalles = detalles;
	}
	
	public double getSubtotal() {
		return subtotal;
	}
	
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	
	public void calcularSubtotal() {
		subtotal = 0;
		for (PedidoDetalle detalle : detalles) {
			subtotal += detalle.calcularSubtotal();
		}
	}
}
