package ec.edu.ups.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ec.edu.ups.jpa.BodegaDAO;
import ec.edu.ups.jpa.ExistenciaDAO;
import ec.edu.ups.jpa.FacturaDAO;
import ec.edu.ups.jpa.PedidoDAO;
import ec.edu.ups.modelos.Bodega;
import ec.edu.ups.modelos.CategoriaProducto;
import ec.edu.ups.modelos.EstadoPedido;
import ec.edu.ups.modelos.Existencia;
import ec.edu.ups.modelos.Factura;
import ec.edu.ups.modelos.FacturaDetalle;
import ec.edu.ups.modelos.Pedido;
import ec.edu.ups.modelos.PedidoDetalle;

@Path(value = "/pedidos/")
public class ServicioPedido {

	@EJB
	private BodegaDAO bodegaDAO;
	
	@EJB
	private ExistenciaDAO existenciaDAO;
	
	@EJB
	private PedidoDAO pedidoDAO;
	
	@EJB
	private FacturaDAO facturaDAO;
	
	@GET
	@Path(value = "listar")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response listarProductos(@QueryParam(value = "bodega-id") int id) {
		Jsonb constructor = JsonbBuilder.create();
		Bodega bodega = bodegaDAO.buscar(id);
		List<Existencia> existencias = bodega.getInventario()
											 .stream()
											 .sorted((e1, e2) -> e1.compareTo(e2))
											 .collect(Collectors.toList());
		return Response.ok(constructor.toJson(existencias))
				       .build();
	}
	
	@POST
	@Path(value = "realizar-pedido")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Produces(value = MediaType.TEXT_PLAIN)
	public Response realizarPedido(String pedidoJSON) {
		Jsonb constructor = JsonbBuilder.create();
		try {
			Pedido pedido = constructor.fromJson(pedidoJSON, Pedido.class);
			pedidoDAO.agregar(pedido);
			Factura factura = new Factura();
			factura.setCliente(pedido.getCliente());
			pedido.getDetalles().forEach(aux -> {
				FacturaDetalle detalle = new FacturaDetalle();
				detalle.setProducto(aux.getProducto());
				detalle.setCantidad(aux.getCantidad());
				factura.getDetalles().add(detalle);
			});
			factura.calcularSubtotal();
			factura.calcularTotal();
			facturaDAO.agregar(factura);
			pedido.setEstado(EstadoPedido.RECEPTADO);
			pedido.calcularSubtotal();
			pedidoDAO.modificar(pedido);
			List<Existencia> existencias = existenciaDAO.listar();
			for (int i = 0; i < existencias.size(); i++) {
				Existencia existencia = existencias.get(i);
				for (int j = 0; j < pedido.getDetalles().size(); j++) {
					PedidoDetalle detallePedido = pedido.getDetalles().get(j);
					if (existencia.getProducto().getId() == detallePedido.getProducto().getId()
						&& existencia.getCantidad() >= detallePedido.getCantidad()
						&& detallePedido.getCantidad() > 0) {
						existencia.setCantidad(existencia.getCantidad() - detallePedido.getCantidad());
						detallePedido.setCantidad(0);
						pedido.getDetalles().set(j, detallePedido);
						existenciaDAO.modificar(existencia);
						break;
					}
				}
			}
			return Response.status(200)
				   	   	   .entity("El pedido ha sido realizado.")
				   	   	   .build();
		} catch (Exception e) {
			return Response.status(404)
					   	   .entity("No se pudo realizar el pedido." + e.getMessage())
					   	   .build();
		}
	}
	
	@GET
	@Path(value = "revisar-estado-pedido")
	@Produces(value = MediaType.TEXT_PLAIN)
	public Response revisarEstadoPedido(@QueryParam(value = "id") int id) {
		Pedido pedido = pedidoDAO.buscar(id);
		if (pedido != null) {
			return Response.status(200)
					       .entity(pedido.getEstado().getEtiqueta())
					       .build();
		} else {
			return Response.status(404)
					       .entity("No existe el pedido con el id especificado.")
					       .build();
		}
	}
	
	@GET
	@Path(value = "/listar-bodegas")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response listarBodegas() {
		Jsonb constructor = JsonbBuilder.create();
		List<Bodega> bodegas = bodegaDAO.listar();
		return Response.ok(constructor.toJson(bodegas))
				       .build();
	}
	
	@GET
	@Path(value = "listar-facturas")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response listarFacturas(@QueryParam(value = "cliente-id") int id) {
		Jsonb constructor = JsonbBuilder.create();
		List<Factura> facturas = facturaDAO.buscarPorCliente(id);
		return Response.ok(constructor.toJson(facturas))
				       .build();
	}
	
	@GET
	@Path(value = "listar-pedidos")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response listarPedidos(@QueryParam(value = "cliente-id") int id) {
		Jsonb constructor = JsonbBuilder.create();
		List<Pedido> pedidos = pedidoDAO.buscarPorCliente(id);
		return Response.ok(constructor.toJson(pedidos))
				       .build();
	}
	
	@GET
	@Path(value = "listar-categorias")
	@Produces(value = MediaType.APPLICATION_JSON)
	public Response listarCategorias() {
		Jsonb constructor = JsonbBuilder.create();
		return Response.ok(constructor.toJson(CategoriaProducto.values()))
				       .build();
	}
}
