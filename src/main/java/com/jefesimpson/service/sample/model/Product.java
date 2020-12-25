package com.jefesimpson.service.sample.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable
public class Product {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "product_name")
    private String productName;
    @DatabaseField
    private String description;
    @DatabaseField(foreignAutoCreate = true, foreignAutoRefresh = true, foreign = true)
    private Employee employee;

    public Product() {
    }

    public Product(int id, String productName, String description, Employee employee) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(description, product.description) &&
                Objects.equals(employee, product.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, description, employee);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", employee=" + employee +
                '}';
    }

    public static String COLUMN_ID = "id";
    public static String COLUMN_PRODUCT_NAME = "product_name";
    public static String COLUMN_DESCRIPTION = "description";
    public static String COLUMN_EMPLOYEE_ID = "employee";
}
