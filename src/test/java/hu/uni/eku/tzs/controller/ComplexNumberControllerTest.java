package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ComplexNumberDto;
import hu.uni.eku.tzs.controller.dto.ComplexNumberRecordRequestDto;
import hu.uni.eku.tzs.model.ComplexNumber;
import hu.uni.eku.tzs.service.ComplexNumberService;
import hu.uni.eku.tzs.service.exceptions.ComplexNumberAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ComplexNumberControllerTest {

    @Mock
    private ComplexNumberService service;

    @InjectMocks
    private ComplexNumberController controller;

    @Test
    void record() throws ComplexNumberAlreadyExistsException {
        // given
        ComplexNumberRecordRequestDto requestDto = ComplexNumberRecordRequestDto.builder().real(0.0).imag(0.0).build();
        doNothing().when(service).record(any());
        // when
        controller.record(requestDto);
        // then
        // verification is not necessary
        // UnnecessaryStubbingException will be throw if service.record is not invoked.
    }

    @Test
    void recordShouldThrowExceptionIfComplexNumberIsAlreadyExists() throws ComplexNumberAlreadyExistsException {
        // given
        ComplexNumberRecordRequestDto requestDto = ComplexNumberRecordRequestDto.builder().real(0.0).imag(0.0).build();
        doThrow(new ComplexNumberAlreadyExistsException()).when(service).record(any());
        // when
        // then
        assertThatThrownBy(() -> controller.record(requestDto)).isInstanceOf(Throwable.class);
    }

    @Test
    void query() {
        // given
        Collection<ComplexNumber> models = List.of(new ComplexNumber(0.0, 0.0));
        doReturn(models).when(service).readAll();
        Collection<ComplexNumberDto> expected = List.of(ComplexNumberDto.builder().real(0.0).imaginary(0.0).build());
        // when
        Collection<ComplexNumberDto> actual = controller.query();
        // then
        assertThat(actual).containsAll(expected);
    }

    @Test
    void queryEmptyList() {
        // given
        doReturn(Collections.emptyList()).when(service).readAll();
        // when
        Collection<ComplexNumberDto> actual = controller.query();
        // then
        assertThat(actual).isEmpty();
    }
}