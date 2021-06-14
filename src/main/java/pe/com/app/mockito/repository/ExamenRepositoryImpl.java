package pe.com.app.mockito.repository;

import java.util.List;

import pe.com.app.mockito.model.Examen;
import pe.com.app.mockito.util.Datos;

public class ExamenRepositoryImpl implements ExamenRepository {

	@Override
	public List<Examen> findAll() {
		return Datos.EXAMENES;
	}

	@Override
	public Examen save(Examen examen) {
		return Datos.EXAMEN;
	}

}
