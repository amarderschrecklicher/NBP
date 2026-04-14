package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.logs.NbpLogsDto;
import ba.unsa.etf.employeemanagement.mapper.NbpLogsMapper;
import ba.unsa.etf.employeemanagement.model.NbpLogs;
import ba.unsa.etf.employeemanagement.repository.NbpLogsRepository;
import ba.unsa.etf.employeemanagement.service.impl.NbpLogsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NbpLogsServiceTest {

    @Mock
    private NbpLogsRepository nbpLogsRepository;

    @Mock
    private NbpLogsMapper nbpLogsMapper;

    @InjectMocks
    private NbpLogsService nbpLogsService;

    private NbpLogs nbpLog;
    private NbpLogsDto nbpLogDto;

    @BeforeEach
    void setUp() {
        nbpLog = NbpLogs.builder()
                .id(1L)
                .actionName("INSERT")
                .tableName("EMPLOYEE")
                .dateTime(LocalDateTime.now())
                .dbUser("NBPT9")
                .build();

        nbpLogDto = NbpLogsDto.builder()
                .id(1L)
                .actionName("INSERT")
                .tableName("EMPLOYEE")
                .dateTime(LocalDateTime.now())
                .dbUser("NBPT9")
                .build();
    }

    @Test
    void shouldFindAllLogs() {

        when(nbpLogsRepository.findAll()).thenReturn(Arrays.asList(nbpLog));
        when(nbpLogsMapper.toDto(any(NbpLogs.class))).thenReturn(nbpLogDto);


        List<NbpLogsDto> result = nbpLogsService.findAll();


        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActionName()).isEqualTo("INSERT");
        verify(nbpLogsRepository, times(1)).findAll();
    }

    @Test
    void shouldFindLogById() {

        when(nbpLogsRepository.findById(1L)).thenReturn(Optional.of(nbpLog));
        when(nbpLogsMapper.toDto(nbpLog)).thenReturn(nbpLogDto);


        NbpLogsDto result = nbpLogsService.findById(1L);


        assertThat(result).isNotNull();
        assertThat(result.getActionName()).isEqualTo("INSERT");
        verify(nbpLogsRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenLogNotFound() {

        when(nbpLogsRepository.findById(999L)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> nbpLogsService.findById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldFindLogsByTableName() {

        when(nbpLogsRepository.findByTableName("EMPLOYEE")).thenReturn(Arrays.asList(nbpLog));
        when(nbpLogsMapper.toDto(any(NbpLogs.class))).thenReturn(nbpLogDto);


        List<NbpLogsDto> result = nbpLogsService.findByTableName("EMPLOYEE");


        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTableName()).isEqualTo("EMPLOYEE");
        verify(nbpLogsRepository, times(1)).findByTableName("EMPLOYEE");
    }

    @Test
    void shouldFindLogsByActionName() {

        when(nbpLogsRepository.findByActionName("INSERT")).thenReturn(Arrays.asList(nbpLog));
        when(nbpLogsMapper.toDto(any(NbpLogs.class))).thenReturn(nbpLogDto);


        List<NbpLogsDto> result = nbpLogsService.findByActionName("INSERT");


        assertThat(result).hasSize(1);
        verify(nbpLogsRepository, times(1)).findByActionName("INSERT");
    }

    @Test
    void shouldFindRecentLogs() {

        when(nbpLogsRepository.findRecentLogs(10)).thenReturn(Arrays.asList(nbpLog));
        when(nbpLogsMapper.toDto(any(NbpLogs.class))).thenReturn(nbpLogDto);


        List<NbpLogsDto> result = nbpLogsService.findRecentLogs(10);


        assertThat(result).hasSize(1);
        verify(nbpLogsRepository, times(1)).findRecentLogs(10);
    }

    @Test
    void shouldCountLogs() {

        when(nbpLogsRepository.count()).thenReturn(42L);


        long count = nbpLogsService.count();


        assertThat(count).isEqualTo(42L);
        verify(nbpLogsRepository, times(1)).count();
    }

    @Test
    void shouldCountLogsByTableName() {

        when(nbpLogsRepository.countByTableName("EMPLOYEE")).thenReturn(15L);

        long count = nbpLogsService.countByTableName("EMPLOYEE");

        assertThat(count).isEqualTo(15L);
        verify(nbpLogsRepository, times(1)).countByTableName("EMPLOYEE");
    }
}
