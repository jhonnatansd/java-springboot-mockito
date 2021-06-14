package pe.com.app.mockito.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pe.com.app.mockito.util.Datos;

public class PreguntaRepositoryImpl implements PreguntaRepository {

	@Override
	public List<String> findByExamId(Long id) {
		System.out.println("PreguntaRepositoryImpl.findByExamId");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Datos.PREGUNTAS;
	}

	@Override
	public void saveAll(List<String> questions) {
		System.out.println("PreguntaRepositoryImpl.saveAll");
	}

}
