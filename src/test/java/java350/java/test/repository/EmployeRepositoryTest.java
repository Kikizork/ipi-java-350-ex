package java350.java.test.repository;

import java.util.stream.Stream;

import javax.persistence.EntityExistsException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;

@ExtendWith(MockitoExtension.class)
public class EmployeRepositoryTest {

	@InjectMocks
	EmployeService employeService;

	@Mock
	EmployeRepository employeRepository;

	@ParameterizedTest
	@MethodSource("employeTesting")
	public void testEmployeService(String nom, String prenom, Poste poste, NiveauEtude niveauEtude, Double tempsPartiel,
			String MessageException) throws EntityExistsException, EmployeException {
		String exception = "";
		// Given
		try {
			employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
		} catch (Exception e) {
			exception = e.getMessage();
		}
		// When

		// Then
		ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
		Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
		Employe employe = employeArgumentCaptor.getValue();
		Assertions.assertThat(employe.getNom()).isEqualTo(nom);
		Assertions.assertThat(employe.getMatricule()).isEqualTo("C00001");
		Assertions.assertThat(exception).isEqualTo(MessageException);
	}

	static Stream<Arguments> employeTesting() {
		return Stream.of(Arguments.of("Test", "Test", Poste.COMMERCIAL, NiveauEtude.BTS_IUT, 1.0, ""),
				Arguments.of(null, null, null, null, null, "Impossible de créer un employé sans poste !"));
	}
}
