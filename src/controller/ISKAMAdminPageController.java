package controller;

import controller.enums.TRANSACTION_TYPE;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.FoodMenu;
import model.Order;
import model.Product;
import model.User;

public class ISKAMAdminPageController {

    @FXML
    private DatePicker dPTDate;

    @FXML
    private DatePicker dPFDate;

    @FXML
    private ComboBox<TRANSACTION_TYPE> cbPTypeOfTransaction;

    @FXML
    private ListView<FoodMenu> lVFoodMenu;

    @FXML
    private TextField tFPInStock;

    @FXML
    private ListView<Order> lvOrders;

    @FXML
    private ComboBox<Product> cBTProduct;

    @FXML
    private ComboBox<TRANSACTION_TYPE> cBTTransactionType;

    @FXML
    private ComboBox<User> cBTUser;

    @FXML
    private ListView<Product> lVProducts;

    @FXML
    private TextField tFTPrice;

    @FXML
    private ComboBox<?> cBPOperationType;

    @FXML
    private ComboBox<?> cBFTypeOfOperation;

    @FXML
    private ComboBox<?> cBTOperationType;

    @FXML
    private ComboBox<Product> cBFProducts;

    @FXML
    private TextArea tATDescription;

    @FXML
    private Button btnFAddProduct;

    @FXML
    private TextArea tAPPrice;

    @FXML
    private TextField tFPName;

    @FXML
    private TextArea tAPDescription;

    @FXML
    void btnTDeleteProductClicked(ActionEvent event) {

    }

    @FXML
    void btnTExecute(ActionEvent event) {

    }

    @FXML
    void btnPExecuteClicked(ActionEvent event) {

    }

    @FXML
    void cBFTypeOfOperationChanged(ActionEvent event) {

    }

    @FXML
    void btnFExecuteClicked(ActionEvent event) {

    }

    @FXML
    void btnFAddProductClicked(ActionEvent event) {

    }

}
