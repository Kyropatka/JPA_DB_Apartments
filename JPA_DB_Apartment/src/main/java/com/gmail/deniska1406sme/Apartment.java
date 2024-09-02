package com.gmail.deniska1406sme;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name="Apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String district;
    private String address;
    private BigDecimal area;
    private Integer rooms;
    private BigDecimal price;

    public Apartment() {
    }

    public Apartment(String district, String address, Double area, Integer rooms, Double price) {
        this.district = district;
        this.address = address;
        this.area = BigDecimal.valueOf(area).setScale(2, RoundingMode.HALF_UP);
        this.rooms = rooms;
        this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getArea() {
        return area.doubleValue();
    }

    public void setArea(Double area) {
        this.area = BigDecimal.valueOf(area).setScale(2, RoundingMode.HALF_UP);
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public double getPrice() {
        return price.doubleValue();
    }

    public void setPrice(Double price) {
        this.price = BigDecimal.valueOf(price);
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", area=" + area +
                ", rooms=" + rooms +
                ", price=" + price +
                '}';
    }
}
