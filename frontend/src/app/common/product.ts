// Catalog product as the API returns it, including the Spring Data REST _links block.
export class Product {
  constructor(public id:string, // catalog id used in detail routes
              public name:string, // display title
              public description:string, // detail page text
              public unitPrice:number, // current price
              public imageUrl:string, // image path (uploaded file or assets fallback)
              public unitsInStock:number, // quantity available to buy
              public dateCreated:Date, // when it was listed
              public lastUpdated:Date, // when it was last edited
              public _links: { // hypermedia links added by Spring Data REST
                self: {
                  href:string // canonical URL of this product
                },
                product: {
                  href:string // URL of the product resource
                },
                category: {
                  href:string // URL of its category, used to lazy-load it
                },
              }) {
  }
}
