package controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import controller.enums.DATABASE_OPERATION_TYPE;
import controller.enums.TRANSACTION_TYPE;
import data.*;
import gui.AlertDialog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.FoodMenu;
import model.Order;
import model.Product;
import model.User;

import javax.jws.soap.SOAPBinding;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

public class ISKAMAdminPageController {

    OrderDAO orderDAO = new OrderDAOImpl();
    ProductDAO productDAO = new ProductDAOImpl();
    FoodMenuDAO foodMenuDAO = new FoodMenuDAOImpl();
    UserDAO userDAO = new UserDAOImpl();
    ObservableList<Order> transactionList;
    ObservableList<Product> productList;
    ObservableList<FoodMenu> foodMenuList;
    Order editedOrder;
    Product editedProduct;
    FoodMenu editedFoodMenu;


    @FXML
    private TabPane iskamPage;

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
    private ListView<Product> lVFProducts;

    @FXML
    private TextField tFTPrice;

    @FXML
    private ComboBox<DATABASE_OPERATION_TYPE> cBPOperationType;

    @FXML
    private ComboBox<DATABASE_OPERATION_TYPE> cBFTypeOfOperation;

    @FXML
    private ComboBox<DATABASE_OPERATION_TYPE> cBTOperationType;

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

    public void initData() {
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void loadData() throws SQLException {
        new Thread(() -> {
            try {
                transactionList = FXCollections.observableArrayList(orderDAO.getAllOrders());
                foodMenuList = FXCollections.observableArrayList(foodMenuDAO.getAllFoodMenu());
                productList = FXCollections.observableArrayList(productDAO.getAllProducts());
                ObservableList<User> users = FXCollections.observableArrayList(userDAO.getAllUsers());
                Platform.runLater(() -> {
                    lvOrders.setItems(transactionList);
                    lVProducts.setItems(productList);
                    lVFoodMenu.setItems(foodMenuList);
                    cBTUser.setItems(FXCollections.observableArrayList(users));
                    cBTProduct.setItems(productList);
                    cBFProducts.setItems(productList);
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();

        //Transaction
        cBTTransactionType.setItems(FXCollections.observableArrayList(TRANSACTION_TYPE.values()));
        cBTOperationType.setItems(FXCollections.observableArrayList(DATABASE_OPERATION_TYPE.values()));
        cBTOperationType.getSelectionModel().select(1);
        lvOrders.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillTransakcePane(newValue);
            editedOrder = newValue;
        });

        //Products
        cbPTypeOfTransaction.setItems(FXCollections.observableArrayList(TRANSACTION_TYPE.values()));
        cBPOperationType.setItems(FXCollections.observableArrayList(DATABASE_OPERATION_TYPE.values()));
        cBPOperationType.getSelectionModel().select(1);
        lVProducts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fillProductPane(newValue);
            editedProduct = newValue;
        });

        //Food menu
        cBFTypeOfOperation.setItems(FXCollections.observableArrayList(DATABASE_OPERATION_TYPE.values()));
        cBFTypeOfOperation.getSelectionModel().select(0);

        cBFProducts.getSelectionModel().select(0);
        lVFoodMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           fillFoodMenu(newValue);
           editedFoodMenu = newValue;
           cBFTypeOfOperationChanged(null);
        });


    }

    @FXML
    void btnTDeleteProductClicked(ActionEvent event) {
        cBTProduct.setValue(null);
    }

    @FXML
    void btnTExecute(ActionEvent event) {
        try {
            switch (cBTOperationType.getValue()) {
                case INSERT:
                    orderDAO.createOrder(
                            new Order(-1,
                                    cBTUser.getValue(),
                                    cBTProduct.getValue(),
                                    cBTTransactionType.getValue(),
                                    Float.parseFloat(tFTPrice.getText()),
                                    Date.valueOf(dPTDate.getValue()),
                                    tATDescription.getText()
                            ));
                    reloadTransakce();
                    AlertDialog.show("Transakce byla přidána.", Alert.AlertType.INFORMATION);
                    break;
                case UPDATE:
                    if (editedOrder != null) {
                        orderDAO.updateOrder(
                                new Order(editedOrder.getId(),
                                        cBTUser.getValue(),
                                        cBTProduct.getValue(),
                                        cBTTransactionType.getValue(),
                                        Float.parseFloat(tFTPrice.getText()),
                                        Date.valueOf(dPTDate.getValue()),
                                        tATDescription.getText()
                                ));
                        reloadTransakce();
                        AlertDialog.show("Transakce byla upravena.", Alert.AlertType.INFORMATION);
                    } else {
                        AlertDialog.show("Není zvolena žádná transakce!", Alert.AlertType.ERROR);
                    }
                    break;
                case DELETE:
                    if (editedOrder != null) {
                        orderDAO.removeOrder(editedOrder);
                        AlertDialog.show("Transakce byla odebrána!", Alert.AlertType.INFORMATION);
                        reloadTransakce();
                    } else {
                        AlertDialog.show("Není zvolena žádná transakce!", Alert.AlertType.ERROR);
                    }
                    break;
            }
        } catch (Exception e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }


    private void fillTransakcePane(Order newValue) {
        if (newValue != null) {
            cBTUser.setValue(newValue.getUser());
            cBTProduct.setValue(newValue.getProduct());
            cBTTransactionType.setValue(newValue.getTypeOfTransaction());
            tFTPrice.setText(String.valueOf(newValue.getPrice()));
            dPTDate.setValue(convertToLocalDate(newValue.getDate()));
            tATDescription.setText(newValue.getDescription());
        } else {
            cBTUser.setValue(null);
            cBTProduct.setValue(null);
            cBTTransactionType.setValue(null);
            tFTPrice.setText("");
            dPTDate.setValue(null);
            tATDescription.setText("");
        }
    }

    private void reloadTransakce() throws SQLException {
        editedOrder = null;
        fillTransakcePane(null);
        new Thread(() -> {
            try {
                transactionList = FXCollections.observableArrayList(orderDAO.getAllOrders());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> lvOrders.setItems(transactionList));
        }).start();
    }

    @FXML
    void btnPExecuteClicked(ActionEvent event) {
        try {
            switch (cBPOperationType.getValue()) {
                case INSERT:
                    productDAO.createProduct(
                            new Product(-1,
                                    tFPName.getText(),
                                    tAPDescription.getText(),
                                    Integer.parseInt(tFPInStock.getText()),
                                    cbPTypeOfTransaction.getValue(),
                                    Float.parseFloat(tAPPrice.getText())
                            ));
                    reloadProduct();
                    AlertDialog.show("Produkt byl přidán.", Alert.AlertType.INFORMATION);
                    break;
                case UPDATE:
                    if (editedProduct != null) {
                        productDAO.updateProduct(
                                new Product(editedProduct.getId(),
                                        tFPName.getText(),
                                        tAPDescription.getText(),
                                        Integer.parseInt(tFPInStock.getText()),
                                        cbPTypeOfTransaction.getValue(),
                                        Float.parseFloat(tAPPrice.getText())
                                ));
                        reloadProduct();
                        AlertDialog.show("Produkt byl upraven.", Alert.AlertType.INFORMATION);
                    } else {
                        AlertDialog.show("Není zvolen žádný produkt!", Alert.AlertType.ERROR);
                    }
                    break;
                case DELETE:
                    if (editedProduct != null) {
                        productDAO.deleteProduct(editedProduct);
                        AlertDialog.show("Produkt byl odebrán.", Alert.AlertType.INFORMATION);
                        reloadProduct();
                    } else {
                        AlertDialog.show("Není zvolen žádný produkt!", Alert.AlertType.ERROR);
                    }
                    break;
            }
        } catch (Exception e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }

    private void fillProductPane(Product newValue) {
        if (newValue != null) {
            tFPName.setText(newValue.getName());
            tAPDescription.setText(newValue.getDescription());
            tFPInStock.setText(String.valueOf(newValue.getInStock()));
            cbPTypeOfTransaction.setValue(newValue.getType());
            tAPPrice.setText(String.valueOf(newValue.getPrice()));
        } else {
            tFPName.setText("");
            tAPDescription.setText("");
            tFPInStock.setText("");
            cbPTypeOfTransaction.setValue(null);
            tAPPrice.setText("");
        }
    }

    private void reloadProduct() throws SQLException {
        editedProduct = null;
        fillProductPane(null);
        new Thread(() -> {
            try {
                productList = FXCollections.observableArrayList(productDAO.getAllProducts());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                lVProducts.setItems(productList);
                cBTProduct.setItems(productList);
                cBFProducts.setItems(productList);
            });
        }).start();
    }


    @FXML
    void cBFTypeOfOperationChanged(ActionEvent event) {
        switch (cBFTypeOfOperation.getValue()) {
            case INSERT:
                lockFoodMenu(true);
                lVFProducts.setItems(null);
                break;
            default:
                lockFoodMenu(false);
                break;
        }
    }

    private void lockFoodMenu(boolean val) {
        lVFProducts.setDisable(val);
        cBFProducts.setDisable(val);
        btnFAddProduct.setDisable(val);
    }

    @FXML
    void cBFProductsChanged(ActionEvent event) {
        if(lVFProducts.getItems().contains(cBFProducts.getValue()))
        {
          btnFAddProduct.setText("Odebrat");
        } else
        {
            btnFAddProduct.setText("Přidat");
        }
    }

    @FXML
    void btnFExecuteClicked(ActionEvent event) {
        try {
            switch (cBFTypeOfOperation.getValue()) {
                case INSERT:
                    foodMenuDAO.createFoodMenu(
                            new FoodMenu(-1,
                                    Date.valueOf(dPFDate.getValue())
                            ));
                    reloadFoodMenu();
                    AlertDialog.show("Jídelní lístek byl přidán.", Alert.AlertType.INFORMATION);
                    break;
                case UPDATE:
                    if (editedFoodMenu != null) {
                        foodMenuDAO.updateFoodMenu(
                                new FoodMenu(editedFoodMenu.getId(),
                                        Date.valueOf(dPFDate.getValue())
                                ));
                        reloadFoodMenu();
                        AlertDialog.show("Jídelní lístek byl upraven.", Alert.AlertType.INFORMATION);
                    } else {
                        AlertDialog.show("Není zvolen žádný jídelní lístek!", Alert.AlertType.ERROR);
                    }
                    break;
                case DELETE:
                    if (editedFoodMenu != null) {
                        foodMenuDAO.deleteFoodMenu(editedFoodMenu);
                        AlertDialog.show("Jídelní lístek byl odebrán.", Alert.AlertType.INFORMATION);
                        reloadFoodMenu();
                    } else {
                        AlertDialog.show("Není zvolen žádný jídelní lístek!", Alert.AlertType.ERROR);
                    }
                    break;
            }
        } catch (Exception e) {
            AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
        }
    }

    private void fillFoodMenu(FoodMenu newValue) {
        if (newValue != null) {
            dPFDate.setValue(convertToLocalDate(newValue.getDate()));

            new Thread(() -> {

                try {
                    ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getProductByFoodMenu(newValue));
                    Platform.runLater(() ->  {
                        lVFProducts.setItems(products);
                        cBFProductsChanged(null);
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            dPFDate.setValue(null);
            lVFProducts.setItems(null);
        }
    }

    private void reloadFoodMenu() throws SQLException {
        editedFoodMenu = null;
        fillFoodMenu(null);
        foodMenuList = FXCollections.observableArrayList(foodMenuDAO.getAllFoodMenu());
        lVFoodMenu.setItems(foodMenuList);
    }


    @FXML
    void btnFAddProductClicked(ActionEvent event) {
        if(lVFProducts.getItems().contains(cBFProducts.getValue()))
        {
            try {
                foodMenuDAO.deleteProductFromFoodMenu(cBFProducts.getValue(), editedFoodMenu);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("obsahuje!");
        } else
        {
            try {
                foodMenuDAO.addProductToFoodMenu(cBFProducts.getValue(), editedFoodMenu);
            } catch (SQLException e){
                AlertDialog.show(e.toString(), Alert.AlertType.ERROR);
            }
        }
        fillFoodMenu(editedFoodMenu);
    }

}
