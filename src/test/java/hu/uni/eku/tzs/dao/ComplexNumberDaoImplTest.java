package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.model.ComplexNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ComplexNumberDaoImplTest {

    @Mock
    private ComplexNumberRepository repository;

    @InjectMocks
    private ComplexNumberDaoImpl dao;

    @Test
    void create() {
        // given
        ComplexNumber model = new ComplexNumber(0.0, 0.0);
        hu.uni.eku.tzs.dao.entity.ComplexNumber entity =
                hu.uni.eku.tzs.dao.entity.ComplexNumber
                        .builder()
                        .realPart(0.0)
                        .imaginaryPart(0.0)
                        .build();
        doReturn(entity).when(repository).save(any());
        // when
        dao.create(model);
        // then
    }

    @Test
    void readAll() {
        // given
        Collection<hu.uni.eku.tzs.dao.entity.ComplexNumber> entityList = List.of(
                hu.uni.eku.tzs.dao.entity.ComplexNumber.builder().realPart(0.0).imaginaryPart(0.0).build(),
                hu.uni.eku.tzs.dao.entity.ComplexNumber.builder().realPart(1.0).imaginaryPart(1.0).build()
        );
        doReturn(entityList).when(repository).findAll();
        Collection<ComplexNumber> expected = List.of(
                new ComplexNumber(0.0,0.0),
                new ComplexNumber(1.0,1.0)
        );
        // when
        Collection<ComplexNumber> actual = dao.readAll();
        // then
        assertThat(actual).containsAll(expected);
    }

    @Test
    void readAllEmptyList() {
        // given
        Collection<hu.uni.eku.tzs.dao.entity.ComplexNumber> entityList = new ArrayList<>();
        doReturn(entityList).when(repository).findAll();
        // when
        Collection<ComplexNumber> actual = dao.readAll();
        // then
        assertThat(actual).isEmpty();
    }
}