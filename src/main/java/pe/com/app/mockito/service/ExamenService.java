package pe.com.app.mockito.service;

import java.util.Optional;

import pe.com.app.mockito.model.Examen;

public interface ExamenService {
	
	Optional<Examen> findByName(String name);
	
	Examen findByNameWithQuestion(String name);
	
	Examen save(Examen examen);

}
