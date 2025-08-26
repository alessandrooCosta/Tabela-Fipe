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

@Component
public class TelaPrincipalController {

    @FXML
    private ChoiceBox<String> tipoVeiculo;

    @FXML
    private ComboBox<Dados> marcaVeiculo;

    @FXML
    private ComboBox<Dados> modeloVeiculo;

    @FXML
    private ComboBox<Dados> anoVeiculo;

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


    @FXML
    public void initialize() {

       tipoVeiculo.getItems().addAll("carros", "motos", "caminhoes");

        // Listener para mudanças
        tipoVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    selecionaTipoVeiculo();
                    System.out.println("Tipo Veiculo: " + newVal);
                }
        );

        marcaVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    selecionaMarcaVeiculo();
                    System.out.println("Marca: " + newVal);
                }
        );
        modeloVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    selecionaSubTipoVeiculo();
                    System.out.println("Modelo: " + newVal);
                }
        );
        anoVeiculo.getSelectionModel();

    }

    public void selecionaTipoVeiculo(){
      try {
            if (tipoVeiculo.getSelectionModel().getSelectedItem() == null) {
                return;
            }
                String tipoVeiculoSelecionado = tipoVeiculo.getValue();
                endereco = linkApi + tipoVeiculoSelecionado + "/marcas/";
                var json = consumo.obterDados(endereco);
                List<Dados> marcas = conversor.obterLista(json, Dados.class);
                marcaVeiculo.getItems().clear();
                marcaVeiculo.getItems().addAll(marcas);
       }catch (Exception e){
           System.err.println("Erro ao carregar marcas!");
           e.printStackTrace();
       }

    }

    public void selecionaMarcaVeiculo(){
        try {
            Dados marcaSelecionada = marcaVeiculo.getValue();
            if (marcaSelecionada == null) {
                return;
            }
            endereco = linkApi + tipoVeiculo.getValue() + "/marcas/" + marcaSelecionada.codigo() + "/modelos";
            System.out.println("endereço após selecionar a marca: " + endereco);
            var json = consumo.obterDados(endereco);
            var modeloLista = conversor.obterDados(json, Modelos.class);
            System.out.println("\nModelos dessa Marca: ");
            modeloLista.modelos().stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);
            modeloVeiculo.getItems().addAll(modeloLista.modelos());
            System.out.println("Tipo de marcaVeiculo: " + marcaVeiculo.getClass());
            System.out.println("Tipo genérico: " + marcaVeiculo.getItems().getClass());
        //    Modelos modelos = conversor.obterDados(json, Modelos.class);
       //     modeloVeiculo.getItems().clear();
       //     modeloVeiculo.getItems().addAll(modelos.modelos());
        }catch (Exception e){
            System.err.println("Erro em selecionaMarcaVeiculo:");
            e.printStackTrace();
        }
    }

    public void selecionaSubTipoVeiculo() {
        try {
            Dados modeloSelecionado = modeloVeiculo.getValue();
            if (modeloSelecionado == null) {
                return;
            }
            Dados marcaSelecionada = marcaVeiculo.getValue();
            if (marcaSelecionada == null){
                return;
            }
            endereco = linkApi + tipoVeiculo.getValue() + "/marcas/" + marcaSelecionada.codigo() + "/modelos/" + modeloSelecionado.codigo() + "/anos";
            System.out.println("Endereço que deu certo após selecionar o modelo: " + endereco);
            var json = consumo.obterDados(endereco);
            List<Dados> anos = conversor.obterLista(json, Dados.class);
            anoVeiculo.getItems().clear();
            anoVeiculo.getItems().addAll(anos);
        } catch (Exception e) {
            System.out.println("Endereço que deu errado, está dentro do catch: " + endereco);
            System.err.println("Erro em selecionaSubTipoVeiculo:");
            e.printStackTrace();
        }
    }

    public void pesquisar(ActionEvent event) {
        anoVeiculo.getSelectionModel().getSelectedItem();
        endereco = linkApi + tipoVeiculo.getValue() + "/marcas/" + marcaVeiculo.getValue() + "/modelos/" + modeloVeiculo.getValue() + "/anos/" + anoVeiculo.getValue() + "-1";
        var json = consumo.obterDados(endereco);
        var veiculos = conversor.obterDados(json, Veiculos.class);
        System.out.println("\nInformações após pressionar o botão. "+ veiculos);
        labelMarca.setText(veiculos.marca());
        labelModelo.setText(veiculos.modelo());
        labelAno.setText(String.valueOf(veiculos.ano()));
        labelCombustivel.setText(veiculos.tipoCombustivel());
        labelValor.setText(veiculos.valor());
        labelCodigoFipe.setText(veiculos.codigoFipe());
        labelMesRef.setText(veiculos.mesRef());
        gridResultado.setVisible(true);
        btnVerGrafico.setVisible(true);
        btnIA.setVisible(true);
    }



}