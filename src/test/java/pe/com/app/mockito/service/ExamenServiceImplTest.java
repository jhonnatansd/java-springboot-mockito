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
public class ExamenServiceImplTest {
	
	@Mock
	ExamenRepositoryImpl repository;
	
	@Mock
	PreguntaRepositoryImpl preguntaRepository;
	
	@InjectMocks
	ExamenServiceImpl service;
	
	@Captor
	ArgumentCaptor<Long> captor;
	
	@BeforeEach
	void setUp() {
//		MockitoAnnotations.openMocks(this);
		
//		repository = mock(ExamenRepository.class);
//		preguntaRepository = mock(PreguntaRepository.class);
//		
//		service = new ExamenServiceImpl(repository, preguntaRepository);
	}
	
	@Test
	void findExamenByName() {		
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		
		Optional<Examen> examen = service.findByName("Matematicas");
		
		assertTrue(examen.isPresent());
		assertEquals(5L, examen.orElseThrow().getId());
		assertEquals("Matematicas", examen.orElseThrow().getNombre());
	}
	
	@Test
	void findExamenByNameEmptyList() {
		List<Examen> datos = Collections.emptyList();
		when(repository.findAll()).thenReturn(datos);
		
		Optional<Examen> examen = service.findByName("Matematicas");
		
		assertFalse(examen.isPresent());
	}
	
	@Test
	void testPreguntasExamen() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findByExamId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		Examen examen = service.findByNameWithQuestion("Matematicas");
		assertEquals(5, examen.getPreguntas().size());
	}

	@Test
	void testPreguntasExamenVerify() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findByExamId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		Examen examen = service.findByNameWithQuestion("Matematicas");
		
		verify(repository).findAll();
		verify(preguntaRepository).findByExamId(anyLong());
	}
	
	@Test
	void testGuardarExamen() {
		Examen newExamen = Datos.EXAMEN;
		newExamen.setPreguntas(Datos.PREGUNTAS);
		
		when(repository.save(any(Examen.class))).then(new Answer<Examen>() {
			Long secuencia = 8L;
			
			@Override
			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);
				
				return examen;
			}
		});
		Examen examen = service.save(newExamen);
		
		assertNotNull(examen.getId());
		assertEquals(8L, examen.getId());
		assertEquals("Fisica", examen.getNombre());
		
		verify(repository).save(any(Examen.class));
		verify(preguntaRepository).saveAll(anyList());
	}
	
	@Test
	void testManejoException() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
		when(preguntaRepository.findByExamId(isNull())).thenThrow(IllegalArgumentException.class);
		
		Exception exception = assertThrows(IllegalArgumentException.class, ()-> service.findByNameWithQuestion("Matematicas"));
		
		assertEquals(IllegalArgumentException.class, exception.getClass());
		
		verify(repository).findAll();
		verify(preguntaRepository).findByExamId(isNull());
	}
	
	@Test
	void testArgumentMatchers() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findByExamId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		service.findByNameWithQuestion("Matematicas");
		
		verify(repository).findAll();
		verify(preguntaRepository).findByExamId(argThat(arg -> arg != null && arg.equals(5L)));
	}
	
	@Test
	void testArgumentMatchers2() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		when(preguntaRepository.findByExamId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		service.findByNameWithQuestion("Matematicas");
		
		verify(repository).findAll();
		verify(preguntaRepository).findByExamId(argThat(new MiArgsMatchers()));
	}
	
	public static class MiArgsMatchers implements ArgumentMatcher<Long> {
		
		private Long argument;
		
		@Override
		public boolean matches(Long argument) {
			this.argument = argument;
			return argument != null && argument > 0;
		}
		
		@Override
		public String toString() {
			return "Es para un mensaje personalizado de error que imprime mockito en caso de que falle el test" +
					argument + " debe ser un entero positivo";
		}
	}
	
	@Test
	void testArgumentCaptor() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		//when(preguntaRepository.findByExamId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		service.findByNameWithQuestion("Matematicas");
		
		//verify(repository).findAll();
		verify(preguntaRepository).findByExamId(captor.capture());
		
		assertEquals(5L, captor.getValue());
	}
	
	@Test
	void testDoThrow() {
		Examen examen = Datos.EXAMEN;
		examen.setPreguntas(Datos.PREGUNTAS);
		
		doThrow(IllegalArgumentException.class).when(preguntaRepository).saveAll(anyList());
		
		assertThrows(IllegalArgumentException.class, ()-> service.save(examen));
	}
	
	@Test
	void testDoCallRealMethod() {
		when(repository.findAll()).thenReturn(Datos.EXAMENES);
		//when(preguntaRepository.findByExamId(anyLong())).thenReturn(Datos.PREGUNTAS);
		
		doCallRealMethod().when(preguntaRepository).findByExamId(anyLong());
		
		Examen examen = service.findByNameWithQuestion("Matematicas");
		
		assertEquals(5L, examen.getId());
		assertEquals("Matematicas", examen.getNombre());
	}
	
	@Test
	void testSpy() {
		ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
		PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
		ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
		
		//when(preguntaRepository.findByExamId(anyLong())).thenReturn(Datos.PREGUNTAS);
		doReturn(Datos.PREGUNTAS).when(preguntaRepository).findByExamId(anyLong());
		
		Examen examen = examenService.findByNameWithQuestion("Matematicas");
		
		assertEquals(5, examen.getId());
		assertEquals("Matematicas", examen.getNombre());
		assertEquals(5, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("Aritmética"));
		
		verify(examenRepository).findAll();
		verify(preguntaRepository).findByExamId(anyLong());
	}
}
