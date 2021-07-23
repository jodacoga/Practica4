package ec.edu.ups.modelos;

public enum EstadoPedido {
	ENVIADO("Enviado"),
	RECEPTADO("Receptado"),
	EN_PROCESO("En proceso"),
	EN_CAMINO("En caminado"),
	FINALIZADO("Finalizado");
	
	private String etiqueta;
	
	private EstadoPedido(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
}
