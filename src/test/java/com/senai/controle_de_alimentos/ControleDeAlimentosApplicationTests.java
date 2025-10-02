package com.senai.controle_de_alimentos;

import com.senai.controle_de_alimentos.application.dto.AlimentoDTO;
import com.senai.controle_de_alimentos.application.service.AlimentoService;
import com.senai.controle_de_alimentos.domain.entity.Alimento;
import com.senai.controle_de_alimentos.domain.exception.CodigoDeBarrasExistenteException;
import com.senai.controle_de_alimentos.domain.exception.DataValidadeInvalidaException;
import com.senai.controle_de_alimentos.domain.repository.AlimentoRepository;
import com.senai.controle_de_alimentos.domain.service.AlimentoServiceDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ControleDeAlimentosApplicationTests {
    @Mock
    private AlimentoRepository alimentoRepo;

    // Cria um mock para o service de domínio, isolando suas validações.
    @Mock
    private AlimentoServiceDomain alimentoServiceDomain;

    // Injeta os mocks criados acima em uma instância real do AlimentoService.
    @InjectMocks
    private AlimentoService alimentoService;

    private AlimentoDTO alimentoDTO;
    private Alimento alimento;

    // Método de setup para criar objetos comuns e corretos para os testes
    @BeforeEach
    void setUp() {
        // Criando o DTO com os tipos e campos corretos da sua classe
        alimentoDTO = new AlimentoDTO(
                null, // ID é nulo ao cadastrar
                12345678, // Integer
                "Maçã Fuji",
                5.99, // double
                LocalDate.now(),
                LocalDate.now().plusDays(10)
        );

        // Criando a Entidade com os tipos e campos corretos
        alimento = new Alimento(
                1L,
                12345678, // Integer
                "Maçã Fuji",
                5.99, // double
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                true
        );
    }


    //--- Testes para cadastrarAlimento ---

    @Test
    @DisplayName("Deve cadastrar um alimento com sucesso quando os dados são válidos")
    void deveCadastrarAlimento_QuandoDadosValidos() {
        // Arrange
        when(alimentoServiceDomain.testarCodigoBarras(alimentoDTO)).thenReturn(false);
        when(alimentoServiceDomain.dataValidadeValida(alimentoDTO)).thenReturn(true);
        // O método 'fromDTO' será chamado dentro do service, então o mock do save deve esperar um Alimento
        when(alimentoRepo.save(any(Alimento.class))).thenReturn(alimento);

        // Act
        Alimento alimentoSalvo = alimentoService.cadastrarAlimento(alimentoDTO);

        // Assert
        assertThat(alimentoSalvo).isNotNull();
        assertThat(alimentoSalvo.getId()).isEqualTo(1L);
        assertThat(alimentoSalvo.getNomeAlimento()).isEqualTo(alimentoDTO.nomeAlimento());
        verify(alimentoRepo, times(1)).save(any(Alimento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar código de barras que já existe")
    void deveLancarExcecao_QuandoCodigoDeBarrasJaExiste() {
        // Arrange
        when(alimentoServiceDomain.testarCodigoBarras(alimentoDTO)).thenReturn(true);

        // Act & Assert
        assertThrows(CodigoDeBarrasExistenteException.class, () -> {
            alimentoService.cadastrarAlimento(alimentoDTO);
        });
        verify(alimentoRepo, never()).save(any(Alimento.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar alimento com data de validade inválida")
    void deveLancarExcecao_QuandoDataDeValidadeInvalida() {
        // Arrange
        when(alimentoServiceDomain.testarCodigoBarras(alimentoDTO)).thenReturn(false);
        when(alimentoServiceDomain.dataValidadeValida(alimentoDTO)).thenReturn(false);

        // Act & Assert
        assertThrows(DataValidadeInvalidaException.class, () -> {
            alimentoService.cadastrarAlimento(alimentoDTO);
        });
        verify(alimentoRepo, never()).save(any(Alimento.class));
    }


    //--- Testes para listarAlimentos ---

    @Test
    @DisplayName("Deve listar todos os alimentos ativos")
    void deveListarAlimentosAtivos() {
        // Arrange
        // O mock está correto, ele retorna uma lista com um objeto Alimento.
        when(alimentoRepo.findAllByStatusTrue()).thenReturn(List.of(alimento));

        // Act
        // Com a correção no service, esta chamada agora deve retornar uma lista com um DTO.
        List<AlimentoDTO> lista = alimentoService.listarAlimentos();

        // Assert
        // Agora, a lista não será mais vazia e o teste passará.
        assertThat(lista).isNotNull().hasSize(1);
        assertThat(lista.get(0).nomeAlimento()).isEqualTo("Maçã Fuji");
        assertThat(lista.get(0).codigoDeBarras()).isEqualTo(12345678);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não há alimentos ativos")
    void deveRetornarListaVazia_QuandoNaoHaAlimentosAtivos() {
        // Arrange
        when(alimentoRepo.findAllByStatusTrue()).thenReturn(Collections.emptyList());

        // Act
        List<AlimentoDTO> lista = alimentoService.listarAlimentos();

        // Assert
        // Este teste já deveria passar, mas a correção garante consistência.
        assertThat(lista).isNotNull().isEmpty();
    }


    //--- Testes para buscarPorId ---

    @Test
    @DisplayName("Deve retornar um AlimentoDTO quando o ID existe e está ativo")
    void deveRetornarAlimentoDTO_QuandoEncontradoEAtivo() {
        // Arrange
        when(alimentoRepo.findById(1L)).thenReturn(Optional.of(alimento));

        // Act
        Optional<AlimentoDTO> resultado = alimentoService.buscarPorId(1L);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().nomeAlimento()).isEqualTo("Maçã Fuji");
    }

    @Test
    @DisplayName("Deve retornar vazio quando o ID existe mas está inativo")
    void deveRetornarVazio_QuandoAlimentoInativo() {
        // Arrange
        alimento.setStatus(false);
        when(alimentoRepo.findById(1L)).thenReturn(Optional.of(alimento));

        // Act
        Optional<AlimentoDTO> resultado = alimentoService.buscarPorId(1L);

        // Assert
        assertThat(resultado).isNotPresent();
    }


    //--- Testes para atualizarAlimento ---

    @Test
    @DisplayName("Deve atualizar um alimento quando o ID existe e está ativo")
    void deveAtualizarAlimento_QuandoEncontradoEAtivo() {
        // Arrange
        AlimentoDTO dadosParaAtualizar = new AlimentoDTO(null, null, "Banana Prata", 7.50, null, null);

        // Simula a busca no banco, retornando o alimento original
        when(alimentoRepo.findById(1L)).thenReturn(Optional.of(alimento));

        // Act
        Optional<Alimento> resultado = alimentoService.atualizarAlimento(1L, dadosParaAtualizar);

        // Assert
        ArgumentCaptor<Alimento> alimentoCaptor = ArgumentCaptor.forClass(Alimento.class);
        verify(alimentoRepo).save(alimentoCaptor.capture()); // Captura o alimento que foi salvo

        Alimento alimentoSalvo = alimentoCaptor.getValue();

        assertThat(resultado).isPresent();
        assertThat(alimentoSalvo.getNomeAlimento()).isEqualTo("Banana Prata");
        assertThat(alimentoSalvo.getPreco()).isEqualTo(7.50);
        // Garante que o status não foi alterado
        assertThat(alimentoSalvo.isStatus()).isTrue();
    }


    //--- Testes para deletarAlimento ---

    @Test
    @DisplayName("Deve inativar (deletar logicamente) um alimento quando o ID existe")
    void deveInativarAlimento_QuandoEncontrado() {
        // Arrange
        when(alimentoRepo.findById(1L)).thenReturn(Optional.of(alimento));

        // Act
        Optional<Alimento> resultado = alimentoService.deletarAlimento(1L);

        // Assert
        ArgumentCaptor<Alimento> alimentoCaptor = ArgumentCaptor.forClass(Alimento.class);
        verify(alimentoRepo).save(alimentoCaptor.capture());

        Alimento alimentoInativado = alimentoCaptor.getValue();

        assertThat(resultado).isPresent();
        assertThat(alimentoInativado.isStatus()).isFalse(); // A verificação mais importante!
    }
}
