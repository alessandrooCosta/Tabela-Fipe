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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TelaPrincipalController {

    @FXML
    private ChoiceBox<String> tipoVeiculo;

    @FXML
    private ComboBox<Dados> marcaVeiculo;

    @FXML
    private ComboBox<Dados> modeloVeiculo;

    @FXML
    private ComboBox anoVeiculo;

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
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    String endereco = null;

    @FXML
    public void initialize() {
       tipoVeiculo.getItems().addAll("carros", "motos", "caminhoes");

        // Listener para mudanças
        tipoVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    System.out.println("Tipo Veiculo: " + newVal);
                    selecionaTipoVeiculo();
                }
        );

        marcaVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    System.out.println("Marca: " + newVal);
                    selecionaMarcaVeiculo();
                }
        );
        modeloVeiculo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    System.out.println("Modelo: " + newVal);
                    selecionaSubTipoVeiculo();
                }
        );
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
            var json = consumo.obterDados(endereco);
            Modelos modelos = conversor.obterDados(json, Modelos.class);
            modeloVeiculo.getItems().clear();
            modeloVeiculo.getItems().addAll(modelos.modelos());
        }catch (Exception e){
            System.err.println("Erro em selecionaMarcaVeiculo:");
            e.printStackTrace();
        }
    }

    public void selecionaSubTipoVeiculo(){
        if(modeloVeiculo.getSelectionModel().getSelectedItem() != null){
            endereco = linkApi + tipoVeiculo.getValue() + "/marcas/" + marcaVeiculo.getValue() + "/modelos/" + modeloVeiculo.getValue() + "/anos";
            System.out.println(endereco);
        }
         var json = consumo.obterDados(endereco);
          List<Dados> anos = conversor.obterLista(json, Dados.class);
          List<Veiculos> veiculos = new ArrayList<>();
          for (int i = 0; i < anos.size(); i++) {
           var enderecoAnos = endereco + "/" + anos.get(i).codigo();
           json = consumo.obterDados(enderecoAnos);
           Veiculos veiculo = conversor.obterDados(json, Veiculos.class);
           veiculos.add(veiculo);
         }
        System.out.println("\nTodos os veiculos filtrados com avaliações por ano: ");
          veiculos.forEach(v -> System.out.println(v.marca() + " " + v.modelo() + " " + v.ano()));
          anoVeiculo.getItems().clear();
          List<Integer> todosAnos = veiculos.stream().map(Veiculos::ano).distinct().collect(Collectors.toList());
         anoVeiculo.getItems().addAll(todosAnos);
        if (!todosAnos.isEmpty()) {
            anoVeiculo.getSelectionModel().selectFirst();
        }
        anoVeiculo.getSelectionModel().getSelectedItem();
        endereco = linkApi + tipoVeiculo.getValue() + "/marcas/" + marcaVeiculo.getValue() + "/modelos/" + modeloVeiculo.getValue() + "/anos/" + anoVeiculo.getValue() + "-1";
        System.out.println(endereco);
    }

    public void pesquisar(ActionEvent event) {
        anoVeiculo.getSelectionModel().getSelectedItem();
        endereco = linkApi + tipoVeiculo.getValue() + "/marcas/" + marcaVeiculo.getValue() + "/modelos/" + modeloVeiculo.getValue() + "/anos/" + anoVeiculo.getValue() + "-1";
        var json = consumo.obterDados(endereco);
        var veiculos = conversor.obterDados(json, Veiculos.class);
        System.out.println("\nInformações após precionar o botão. "+ veiculos);
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