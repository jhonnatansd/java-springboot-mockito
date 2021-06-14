package pe.com.app.mockito.service;

import java.util.List;
import java.util.Optional;

import pe.com.app.mockito.model.Examen;
import pe.com.app.mockito.repository.ExamenRepository;
import pe.com.app.mockito.repository.PreguntaRepository;

public class ExamenServiceImpl implements ExamenService {
	
	private ExamenRepository examenRepository;
	private PreguntaRepository preguntaRepository;

	public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository) {
		this.examenRepository = examenRepository;
		this.preguntaRepository = preguntaRepository;
	}

	@Override
	public Optional<Examen> findByName(String name) {
		return this.examenRepository.findAll()
				.stream()
				.filter(e -> e.getNombre().contains(name))
				.findFirst();
	}

	@Override
	public Examen findByNameWithQuestion(String name) {
		Examen examen = findByName(name).orElseThrow();
		List<String> preguntas = preguntaRepository.findByExamId(examen.getId());
		examen.setPreguntas(preguntas);
		
		return examen;
	}

	@Override
	public Examen save(Examen examen) {
		if (!examen.getPreguntas().isEmpty()) {
			preguntaRepository.saveAll(examen.getPreguntas());
		}
		return examenRepository.save(examen);
	}

}
