package es.unican.CIBEL.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unican.CIBEL.domain.Categoria;
import es.unican.CIBEL.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public List<Categoria> categorias() {
		return repository.findAll();
	}
}
