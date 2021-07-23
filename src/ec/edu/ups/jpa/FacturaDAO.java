package ec.edu.ups.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.edu.ups.modelos.Factura;

@Stateless
public class FacturaDAO extends DAO<Factura, Integer> {

	@PersistenceContext(unitName = "Practica4")
	private EntityManager gestor;
	
	public FacturaDAO() {
		super(Factura.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return gestor;
	}
	
	public void anular(Factura factura) {
		factura.setActiva(false);
		gestor.merge(factura);
	}
	
	public List<Factura> buscarPorCliente(int id) {
		String jqpl = "SELECT f FROM Factura f JOIN f.cliente c WHERE c.id = " + id;
		return gestor.createQuery(jqpl, Factura.class).getResultList();
	}
}
