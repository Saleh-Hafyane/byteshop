import { ProductCategory } from './product-category';

export class ProductManagement {
  constructor(
    public categoryId: number,
    public name: string,
    public description: string,
    public unitPrice: number,
    public imageUrl: string,
    public unitsInStock: number
  ) {}
}
