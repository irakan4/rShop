package com.devrakan.rshop.Model.Manager

class ProductsM {

    private var ProductId: String = ""
    private var publisher: String = ""
    private var productName: String = ""
    private var productPrice: String = ""
    private var productCount: String = ""
    private var ProductImage: String = ""

    constructor()

    constructor(
        ProductId: String,
        publisher: String,
        productName: String,
        productPrice: String,
        productCount: String,
        ProductImage: String
    ) {
        this.ProductId = ProductId
        this.publisher = publisher
        this.productName = productName
        this.productPrice = productPrice
        this.productCount = productCount
        this.ProductImage = ProductImage
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


    fun getProductId(): String {
        return ProductId
    }

    fun setProductId(ProductId: String) {
        this.ProductId = ProductId
    }

    fun getProductImage(): String {
        return ProductImage
    }

    fun setProductImage(ProductImage: String) {
        this.ProductImage = ProductImage
    }

    fun getPublisher(): String {
        return publisher
    }

    fun setPublisher(publisher: String) {
        this.publisher = publisher
    }

}