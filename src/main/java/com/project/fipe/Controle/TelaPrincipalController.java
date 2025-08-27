package com.project.fipe.Controle;

import com.project.fipe.Model.Dados;
import com.project.fipe.Model.Modelos;
import com.project.fipe.Model.Veiculos;
import com.project.fipe.Service.ConsumoApi;
import com.project.fipe.Service.ConverteDados;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TelaPrincipalController {

    @FXML
    private ChoiceBox<String> tipoVeiculo;

    @FXML
    private ComboBox<String> marcaVeiculo;

    @FXML
    private ComboBox<String> modeloVeiculo;

    @FXML
    private ComboBox<String> anoVeiculo;

    @FXML
    private Button buttonPesquisar;

    @FXML
    private GridPane gridResultado;

    @FXML
    private Label labelMarca;

    @FXML
    private Label labelModelo;

    @FXML
    private Label labelAno;

    @FXML
    private Label labelCombustivel;

    @FXML
    private Label labelValor;

    @FXML
    private Label labelCodigoFipe;

    @FXML
    private Label labelMesRef;

    @FXML
    private Button btnVerGrafico;

    @FXML
    private Button btnIA;

    private final String linkApi = "https://parallelum.com.br/fipe/api/v1/";
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    String endereco = null;
    private List<Dados> marcasList;
    private List<Dados> modelosList;
    private List<Dados> anoList;

    @FXML
    public void initialize() {

        tipoVeiculo.getItems().addAll("carros", "motos", "caminhoes");

        tipoVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    selecionaTipoVeiculo();
                }
        );

        marcaVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if(newVal != null){
                        selecionaMarcaVeiculo();
                    }
                }
        );

        modeloVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if(newVal != null){
                        selecionaSubTipoVeiculo();
                    }
                }
        );
    }

    public void selecionaTipoVeiculo(){
      try {
            String tipoVeiculoSelecionado = tipoVeiculo.getValue();
            if (tipoVeiculoSelecionado == null) {
                return;
            }
                endereco = linkApi + tipoVeiculoSelecionado + "/marcas";
                var json = consumo.obterDados(endereco);
                marcasList = conversor.obterLista(json, Dados.class);
                /* Usando Streams para extrair apenas os nomes
                 Foi criado uma lista de Strings. Essa lista pega os valores que no momento não objetos e transformar
                 em Strings. */
                List<String> nomesMarcas = marcasList.stream().map(Dados::nome).collect(Collectors.toList());
                marcaVeiculo.getItems().clear();
                marcaVeiculo.getItems().addAll(nomesMarcas);
       }catch (Exception e){
           System.err.println("Erro ao carregar marcas!");
           e.printStackTrace();
       }

    }

    public void selecionaMarcaVeiculo(){
        try {
            String marcaSelecionadaNome = marcaVeiculo.getValue();
            String tipoVeiculoSelecionado = tipoVeiculo.getValue();
            if (marcaSelecionadaNome  == null || tipoVeiculoSelecionado == null) {
                return;
            }
            // Stream para buscar na lista a partir do nome da marca selecionada;
            Dados marcaSelecionado = marcasList.stream().filter(marca -> marca.nome().equals(marcaSelecionadaNome)).findFirst().orElse(null);
            endereco = linkApi + tipoVeiculoSelecionado + "/marcas/" + marcaSelecionado.codigo() + "/modelos";
            if (marcaSelecionado == null) return;
            var json = consumo.obterDados(endereco);
            Modelos modelos = conversor.obterDados(json, Modelos.class);
            modelosList = modelos.modelos();
            List<String> nomeModelos = modelosList.stream().map(Dados::nome).collect(Collectors.toList());
            modeloVeiculo.getItems().clear();
            modeloVeiculo.getItems().addAll(nomeModelos);
        }catch (Exception e){
            System.err.println("Erro em selecionaMarcaVeiculo:");
            e.printStackTrace();
        }
    }

    public void selecionaSubTipoVeiculo() {
        try {
            String modeloSelecionadoNome = modeloVeiculo.getValue();
            String marcaSelecionadaNome = marcaVeiculo.getValue();
            String tipoVeiculoSelecionadoNome = tipoVeiculo.getValue();
            if (modeloSelecionadoNome == null || marcaSelecionadaNome == null || tipoVeiculoSelecionadoNome == null) {
                return;
            }
            Dados marcaSelecionada = marcasList.stream().filter(marca -> marca.nome().equals(marcaSelecionadaNome)).findFirst().orElse(null);
            Dados modeloSelecionado = modelosList.stream().filter(modelo -> modelo.nome().equals(modeloSelecionadoNome)).findFirst().orElse(null);
            endereco = linkApi + tipoVeiculo.getValue() + "/marcas/" + marcaSelecionada.codigo() + "/modelos/" + modeloSelecionado.codigo() + "/anos";
            var json = consumo.obterDados(endereco);
            anoList = conversor.obterLista(json, Dados.class);
            List<String> nomesAnos = anoList.stream().map(Dados::nome).collect(Collectors.toList());
            anoVeiculo.getItems().clear();
            anoVeiculo.getItems().addAll(nomesAnos);
        } catch (Exception e) {
            System.err.println("Erro em selecionaSubTipoVeiculo:");
            e.printStackTrace();
        }
    }

    public void pesquisar(ActionEvent event) {
        try {
            String marcaNome = marcaVeiculo.getValue();
            String modeloNome = modeloVeiculo.getValue();
            String anoNome = anoVeiculo.getValue();
            String tipo = tipoVeiculo.getValue();

            if (marcaNome == null || modeloNome == null || anoNome == null || tipo == null) {
                mostrarAlerta("Selecione todos os campos!");
                return;
            }

            Dados marca = marcasList.stream()
                    .filter(m -> m.nome().equals(marcaNome))
                    .findFirst()
                    .orElse(null);

            Dados modelo = modelosList.stream()
                    .filter(m -> m.nome().equals(modeloNome))
                    .findFirst()
                    .orElse(null);

            Dados ano = anoList.stream()
                    .filter(a -> a.nome().equals(anoNome))
                    .findFirst()
                    .orElse(null);

            if (marca == null || modelo == null || ano == null) {
                mostrarAlerta("Dados não encontrados!");
                return;
            }

            endereco = linkApi + tipo + "/marcas/" + marca.codigo() +
                    "/modelos/" + modelo.codigo() + "/anos/" + ano.codigo();

            var json = consumo.obterDados(endereco);
            Veiculos veiculo = conversor.obterDados(json, Veiculos.class);

            preencherResultados(veiculo);

        } catch (Exception e) {
            System.err.println("Erro ao pesquisar:");
            e.printStackTrace();
            mostrarAlerta("Erro ao buscar dados: " + e.getMessage());
        }
    }

    private void preencherResultados(Veiculos veiculo) {
        labelMarca.setText(veiculo.marca());
        labelModelo.setText(veiculo.modelo());
        labelAno.setText(String.valueOf(veiculo.ano()));
        labelCombustivel.setText(veiculo.tipoCombustivel());
        labelValor.setText(veiculo.valor());
        labelCodigoFipe.setText(veiculo.codigoFipe());
        labelMesRef.setText(veiculo.mesRef());

        gridResultado.setVisible(true);
        btnVerGrafico.setVisible(true);
        btnIA.setVisible(true);
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }



}