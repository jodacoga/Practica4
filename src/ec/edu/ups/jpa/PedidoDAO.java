package ec.edu.ups.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Pedido;


@Stateless
public class PedidoDAO extends DAO<Pedido, Integer> {

	@PersistenceContext(unitName = "Practica4")
	private EntityManager gestor;
	
	public PedidoDAO() {
		super(Pedido.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
	
	public List<Pedido> buscarPorCliente(int id) {
		String jqpl = "SELECT p FROM Pedido p JOIN p.cliente c WHERE c.id = " + id;
		return gestor.createQuery(jqpl, Pedido.class).getResultList();
	}
}
