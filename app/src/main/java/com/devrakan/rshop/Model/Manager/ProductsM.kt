package com.devrakan.rshop.Model.Manager

class ProductsM {
    private var ProductId:String = ""
   private var ProductImage:String = ""
   private var productCount:String = ""
    private var productName:String = ""
    private var productPrice:String = ""
    private var publisher:String = ""

    constructor()
    constructor(ProductId:String,ProductImage:String,productCount:String,productName:String,
                productPrice:String,publisher:String){
        this.ProductId = ProductId
        this.ProductImage = ProductImage
        this.productCount = productCount
        this.productName = productName
        this.productPrice = productPrice
        this.publisher = publisher
    }

    fun getProductId():String{
        return ProductId
    }

     fun setProductId(ProductId: String){
        this.ProductId = ProductId
    }

    fun getProductImg():String{
       return ProductImage
    }

    fun setProductImg(ProductImage: String){
        this.ProductImage = ProductImage
    }

    fun getProductCount():String{

        return productCount
    }

    fun setProductCount(productCount: String){
        this.productCount = productCount

    }

    fun getProductName():String{
        return productName
    }

    fun setProductName(productName: String){
        this.productName = productName
    }

    fun getPrice():String{
        return productPrice
    }

    fun setPrice(productPrice: String){
        this.productPrice = productPrice
    }

    fun getPublisher():String{
        return publisher
    }

    fun setPublisher(publisher: String){
        this.publisher = publisher
    }
}
