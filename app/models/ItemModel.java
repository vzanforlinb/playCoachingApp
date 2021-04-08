package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ITEMS")
public class ItemModel {
    @Id
    @GeneratedValue // bd
    public long id;
    @Column(name = "title")
    public String title;
    public int quantity;
    public double price;
}