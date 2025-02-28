package vetcare.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import vetcare.api.ApiApplication;
import vetcare.api.model.entities.Cliente;
import vetcare.api.model.entities.Fatura;
import vetcare.api.model.entities.Insumo;
import vetcare.gui.BaseUserController;
import vetcare.gui.ListCard;
import vetcare.gui.ScreenManager;
import vetcare.gui.VetCareApp;
import javafx.stage.Stage;
import vetcare.gui.ScreenManager;
import vetcare.gui.controllers.AdicionarPetController;

public class FinanceiroController extends BaseUserController {
    @FXML private TextField searchField;
    @FXML private VBox clientes;
    @FXML private BorderPane fichaCliente;
    @FXML private Text clienteNome;
    @FXML private Text clienteContato;
    @FXML private Text clienteCpf;
    @FXML private ImageView clientePhotoImage;

    @FXML private TextField clienteNomeField;
    @FXML private TextField clienteId;
    @FXML private TextField contatoCliente;

    @FXML private GridPane tabFaturas;


    private Cliente cliente;
    private Fatura fatura;

    private static final String[] clientesPictures = new String[] {
            "b.jpeg", "c.jpeg", "d.jpeg", "f.jpg"
    };

    public void initialize() {
        doSearch();
    }

    public static String getPicture(Cliente cliente) {

        String crmv = cliente.getCpfCliente();
        int index = crmv.substring(crmv.length() - 1).charAt(0) % clientesPictures.length;
        var pic = clientesPictures[index];
        var resource = PacientesController.class.getResource("/vetcare/gui/Images/clientes/" + pic).toExternalForm();
        return resource;
    }

    private void selecionarCliente(Cliente cliente) {
        this.cliente = cliente;

        // Card do cliente
        clienteNome.setText(cliente.getNomeCliente());
        clienteContato.setText(cliente.getContatoCliente() + " - " + cliente.getEndCliente());
        clientePhotoImage.setImage(new Image(getPicture(cliente)));

        // Propriedades editáveis
        clienteNomeField.setText(cliente.getNomeCliente());
        clienteId.setText(cliente.getCpfCliente());
        contatoCliente.setText(String.valueOf((cliente.getContatoCliente())));
        fichaCliente.setVisible(true);

        // Cabeçalho da tabela de faturas
        tabFaturas.getChildren().clear();
        var tipoCab = new StackPane(new Text("Id"));
        tipoCab.getStyleClass().add("table-header-cell");
        var dataCab = new StackPane(new Text("Data"));
        dataCab.getStyleClass().add("table-header-cell");
        var valor = new StackPane(new Text("Valor"));
        valor.getStyleClass().add("table-header-cell");
        var formaPgto = new StackPane(new Text("Forma de Pagamento"));
        formaPgto.getStyleClass().add("table-header-cell");
        var empty = new StackPane();
        empty.getStyleClass().add("table-header-cell");
        tabFaturas.addRow(0, tipoCab, dataCab, valor, formaPgto, empty);

        // Lista todos as faturas
        var faturas = ApiApplication.financeiro.buscarFaturaPorCliente(cliente.getCpfCliente());
        int row = 1;

        for (var at : faturas) {

            var id = new StackPane(new Text(at.getIdFatura().toString()));
            id.getStyleClass().add("table-cell");

            var data = new StackPane(new Text(at.getDate().toString()));
            data.getStyleClass().add("table-cell");

            var a = new Text(at.getValorTotal().toString());
            var valort = new StackPane(a);
            valort.getStyleClass().add("table-cell");

            var b = new Text(at.getFormaPagamento());
            var formapg = new StackPane(b);
            formapg.getStyleClass().add("table-cell");

            tabFaturas.addRow(row++, id, data, valort, formapg);
        }
    }


    private Node criarCliente(Cliente cliente) {
        var imageUrl = getPicture(cliente);
        var name = cliente.getNomeCliente();
        var desc = cliente.getContatoCliente() + " - " + cliente.getCpfCliente();

        var card = new ListCard(imageUrl, name, desc);
        card.setOnMouseClicked(ev -> {
            this.selecionarCliente(cliente);
        });
        card.getStyleClass().add("cliente");
        return card;
    }

    @FXML
    private void doSearch() {
        // Busca no banco de dados
        var query = searchField.getText();
        var client = ApiApplication.financeiro.buscaClientePorNome(query);

        // Limpa a lista e cria elemento
        clientes.getChildren().clear();
        for (Cliente cliente : client) {
            var elem = criarCliente(cliente);
            clientes.getChildren().add(elem);
        }
    }

    @FXML
    private void addFatura() {
        // chamar pop up
        var loader = VetCareApp.screens.getLoaderFor("/vetcare/gui/Scenes/adicionarfatura.fxml");

        try {
            // Carregar o FXML e obter o controlador associado
            Parent root = loader.load();
            AdicionarFaturaController controller = loader.getController();

            // Configurar o CPF do cliente no controlador
            controller.initialize(cliente.getCpfCliente());

            // Configurar e exibir o pop-up
            Stage stage = new Stage();
            stage.setTitle("Adicionar Fatura");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o popup de Adicionar Fatura", e);
        }
    }

    @FXML
    private void addCliente() {
        // chamar pop up
        var loader = VetCareApp.screens.getLoaderFor("/vetcare/gui/Scenes/adicionarclientecompet.fxml");

        try {
            // Carregar o FXML e obter o controlador associado
            Parent root = loader.load();
            AdicionarClientecomPetController controller = loader.getController();

            // Configurar o CPF do cliente no controlador
            controller.initialize();

            // Configurar e exibir o pop-up
            Stage stage = new Stage();
            stage.setTitle("Adicionar Cliente com Pet");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o popup de Adicionar Fatura", e);
        }
    }

    @FXML
    private void saveData() {
        this.cliente.setNomeCliente(clienteNomeField.getText());
        this.cliente.setContatoCliente((clienteId.getText()));
        this.cliente.setCpfCliente(contatoCliente.getText());
        ApiApplication.clientes.updateCliente(this.cliente);
    }

    @FXML
    private void resetData() {
        selecionarCliente(this.cliente);
    }

    @FXML
    void novoPet() {
        clienteNomeField.setText("");
        contatoCliente.setText("");
    }


    @FXML
    public void abrirJanelaAddPet(){

        // chamar pop up
        var loader = VetCareApp.screens.getLoaderFor("/vetcare/gui/Scenes/adicionarpet.fxml");

        try {
            // Carregar o FXML e obter o controlador associado
            Parent root = loader.load();
            AdicionarPetController controller = loader.getController();
            controller.initializePet(this.cliente.getCpfCliente());

            // Configurar e exibir o pop-up
            Stage stage = new Stage();
            stage.setTitle("Adicionar Pet");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o popup de Adicionar Vet", e);
        }
    }

    @FXML
    private void excluirCliente() {
        var loader = VetCareApp.screens.getLoaderFor("/vetcare/gui/Scenes/excluircliente.fxml");

        try {
            // Carregar o popup de confirmação
            Parent root = loader.load();
            ExcluirClienteController controller = loader.getController();
            controller.initialize("Tem certeza que deseja excluir o cliente \"" + cliente.getNomeCliente() + "\"?");

            // Exibir o popup
            Stage stage = new Stage();
            stage.setTitle("Confirmação de Exclusão");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Verificar a resposta do usuário
            if (controller.isConfirmado()) {
                // Lógica para excluir o insumo do banco de dados
                System.out.println("Excluindo cliente: " + cliente.getNomeCliente());
                ApiApplication.clientes.deleteCliente(cliente.getCpfCliente());
                // insumoDao.excluir(insumo.getCdInsumo());
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao abrir o popup de confirmação de exclusão.", e);
        }
    }

}