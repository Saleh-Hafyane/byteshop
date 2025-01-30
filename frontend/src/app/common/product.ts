export class Product {
  constructor(public id:string,
              public name:string,
              public description:string,
              public unitPrice:number,
              public imageUrl:string,
              public unitsInStock:number,
              public dateCreated:Date,
              public lastUpdated:Date,
              public _links: {
                self: {
                  href:string
                },
                product: {
                  href:string
                },
                category: {
                  href:string
                },
              }) {
  }
}
