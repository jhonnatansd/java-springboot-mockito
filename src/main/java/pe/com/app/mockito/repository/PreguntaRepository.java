package pe.com.app.mockito.repository;

import java.util.List;

public interface PreguntaRepository {
	
	List<String> findByExamId(Long id);
	
	void saveAll(List<String> questions);

}
