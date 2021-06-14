package pe.com.app.mockito.repository;

import java.util.List;

import pe.com.app.mockito.model.Examen;

public interface ExamenRepository {
	
	List<Examen> findAll();
	
	Examen save(Examen examen);

}
