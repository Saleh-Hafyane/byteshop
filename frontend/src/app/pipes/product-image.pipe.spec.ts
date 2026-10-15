// Unit tests for ProductImagePipe: how every kind of image value becomes a loadable URL.
import { ProductImagePipe } from './product-image.pipe';

describe('ProductImagePipe', () => {
  let pipe: ProductImagePipe;

  beforeEach(() => {
    pipe = new ProductImagePipe();
  });

  it('should pass through absolute URLs', () => {
    expect(pipe.transform('https://via.placeholder.com/150')).toBe('https://via.placeholder.com/150');
    expect(pipe.transform('http://example.com/a.png')).toBe('http://example.com/a.png');
  });

  it('should pass through data: and blob: URLs', () => {
    expect(pipe.transform('data:image/png;base64,abc')).toBe('data:image/png;base64,abc');
    expect(pipe.transform('blob:http://localhost:4200/xyz')).toBe('blob:http://localhost:4200/xyz');
  });

  it('should resolve backend-relative URLs against the API origin', () => {
    expect(pipe.transform('/api/files/abc.png')).toBe('http://localhost:8080/api/files/abc.png');
  });

  it('should resolve legacy assets-relative values', () => {
    expect(pipe.transform('assets/images/p.png')).toBe('assets/images/p.png');
    expect(pipe.transform('images/p.png')).toBe('assets/images/p.png');
  });

  it('should fall back to gray placeholder for empty values', () => {
    expect(pipe.transform('')).toBe(ProductImagePipe.PLACEHOLDER);
    expect(pipe.transform('   ')).toBe(ProductImagePipe.PLACEHOLDER);
    expect(pipe.transform(null)).toBe(ProductImagePipe.PLACEHOLDER);
    expect(pipe.transform(undefined)).toBe(ProductImagePipe.PLACEHOLDER);
    expect(ProductImagePipe.PLACEHOLDER.startsWith('data:image/svg+xml')).toBeTrue();
  });
});
