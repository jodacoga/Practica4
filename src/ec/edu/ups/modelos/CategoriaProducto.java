package ec.edu.ups.modelos;

public enum CategoriaProducto {
	CARNES("Carnes"),
	BEBIDA("Bebidas"),
	LACTEOS("Lácteos"),
	FRUTAS_Y_VEGETALES("Frutas y vegetales");

	private String etiqueta;
	
	private CategoriaProducto(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public String getEtiqueta() {
		return etiqueta;
	}
}
