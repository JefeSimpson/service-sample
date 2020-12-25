package com.jefesimpson.service.sample.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable
public class Order {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreignAutoCreate = true, foreignAutoRefresh = true, foreign = true)
    private Client client;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private Product product;
    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = "order_date")
    private LocalDate orderDate;
    @DatabaseField
    private String address;
    @DatabaseField
    private String quantity;
    @DatabaseField
    private String price;
    @DatabaseField(dataType = DataType.ENUM_STRING)
    private OrderStatus status;

    public Order() {
    }

    public Order(int id, Client client, Product product, LocalDate orderDate, String address, String quantity, String price, OrderStatus status) {
        this.id = id;
        this.client = client;
        this.product = product;
        this.orderDate = orderDate;
        this.address = address;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                Objects.equals(client, order.client) &&
                Objects.equals(product, order.product) &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(address, order.address) &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(price, order.price) &&
                Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, product, orderDate, address, quantity, price, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", client=" + client +
                ", product=" + product +
                ", orderDate=" + orderDate +
                ", address='" + address + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                ", status=" + status +
                '}';
    }

    public static String COLUMN_ID = "id";
    public static String COLUMN_CLIENT_ID = "client";
    public static String COLUMN_PRODUCT_ID = "product";
    public static String COLUMN_ORDER_DATE = "order_date";
    public static String COLUMN_ADDRESS = "address";
    public static String COLUMN_QUANTITY = "quantity";
    public static String COLUMN_PRICE = "price";
    public static String COLUMN_STATUS = "status";

}
