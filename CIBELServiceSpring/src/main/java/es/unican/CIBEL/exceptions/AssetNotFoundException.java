package es.unican.CIBEL.exceptions;

@SuppressWarnings("serial")
public class AssetNotFoundException extends RuntimeException {
	public AssetNotFoundException(Long id) {
		super("Activo no encontrado con id: " + id);
	}
}
