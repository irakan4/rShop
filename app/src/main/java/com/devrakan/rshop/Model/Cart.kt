package com.devrakan.rshop.Model

class Cart {

    private var productDescription: String = ""
    private var productName: String = ""
    private var publisher: String = ""
    private var Case: String = ""
    private var number: String = ""
    private var productPrice: String = ""
    private var productCount: String = ""
    private var ProductImage: String = ""
    private var Id: String = ""

    constructor()


    constructor(
        productDescription: String,
        productName: String,
        productPrice: String,
        productCount: String,
        Case: String,
        publisher: String,
        number: String,
        Id: String,
        ProductImage: String
    ) {
        this.productDescription = productDescription
        this.productName = productName
        this.productPrice = productPrice
        this.productCount = productCount
        this.ProductImage = ProductImage
        this.Case = Case
        this.number = number
        this.Id = Id
        this.publisher = publisher
    }


    fun getProductName(): String {
        return productName
    }

    fun setProductName(productName: String) {
        this.productName = productName
    }

    fun getProductPrice(): String {
        return productPrice
    }

    fun setProductPrice(productPrice: String) {
        this.productPrice = productPrice
    }

    fun getProductCount(): String {
        return productCount
    }

    fun setProductCount(productCount: String) {
        this.productCount = productCount
    }


    fun getProductDescription(): String {
        return productDescription
    }

    fun setProductDescription(productDescription: String) {
        this.productDescription = productDescription
    }

    fun getProductImage(): String {
        return ProductImage
    }

    fun setProductImage(ProductImage: String) {
        this.ProductImage = ProductImage
    }
    fun getCase(): String {
        return Case
    }

    fun setCase(Case: String) {
        this.Case = Case
    }
    fun getPublisher(): String {
        return publisher
    }

    fun setPublisher(publisher: String) {
        this.publisher = publisher
    }
    fun getNumber(): String {
        return number
    }

    fun setNumber(number: String) {
        this.number = number
    }
    fun getId(): String {
        return Id
    }

    fun setId(Id: String) {
        this.Id = Id
    }


}