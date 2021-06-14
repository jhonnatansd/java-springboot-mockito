package pe.com.app.mockito.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import pe.com.app.mockito.model.Examen;
import pe.com.app.mockito.repository.ExamenRepository;
import pe.com.app.mockito.repository.ExamenRepositoryImpl;
import pe.com.app.mockito.repository.PreguntaRepository;
import pe.com.app.mockito.repository.PreguntaRepositoryImpl;
import pe.com.app.mockito.util.Datos;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceImplSpyTest {
	
	@Spy
	ExamenRepositoryImpl repository;
	
	@Spy
	PreguntaRepositoryImpl preguntaRepository;
	
	@InjectMocks
	ExamenServiceImpl service;
	
	@Test
	void testSpy() {
		//when(preguntaRepository.findByExamId(anyLong())).thenReturn(Datos.PREGUNTAS);
		doReturn(Datos.PREGUNTAS).when(preguntaRepository).findByExamId(anyLong());
		
		Examen examen = service.findByNameWithQuestion("Matematicas");
		
		assertEquals(5, examen.getId());
		assertEquals("Matematicas", examen.getNombre());
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("Aritmética"));
		
		verify(repository).findAll();
		verify(preguntaRepository).findByExamId(anyLong());
	}
}
