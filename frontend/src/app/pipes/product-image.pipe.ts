// Turns any stored product image value into a URL the browser can actually load.
import { Pipe, PipeTransform } from '@angular/core';

// Where the Spring Boot backend runs, used to resolve /api/... paths.
const BACKEND_ORIGIN = 'http://localhost:8080';

// absolute http(s)/data:/blob: pass through, /api/... gets the backend origin, assets paths stay local, empty gets the gray placeholder.
@Pipe({ name: 'productImage', standalone: true })
export class ProductImagePipe implements PipeTransform {
  // Inline gray "No image" SVG so a missing picture never 404s.
  static readonly PLACEHOLDER =
    'data:image/svg+xml;charset=UTF-8,' +
    '%3Csvg xmlns=%27http://www.w3.org/2000/svg%27 width=%27400%27 height=%27300%27%3E' +
    '%3Crect width=%27100%25%27 height=%27100%25%27 fill=%27%23e9ecef%27/%3E' +
    '%3Ctext x=%2750%25%27 y=%2750%25%27 dominant-baseline=%27middle%27 text-anchor=%27middle%27' +
    ' font-family=%27sans-serif%27 font-size=%2724%27 fill=%27%23959da5%27%3ENo image%3C/text%3E%3C/svg%3E';

  transform(value: string | null | undefined): string {
    // Nothing chosen yet or only whitespace: show the placeholder.
    if (!value || !value.trim()) {
      return ProductImagePipe.PLACEHOLDER;
    }
    const v = value.trim();
    // Already a usable URL (external, in-memory blob or data URI): use it as-is.
    if (
      v.startsWith('http://') ||
      v.startsWith('https://') ||
      v.startsWith('data:') ||
      v.startsWith('blob:')
    ) {
      return v;
    }
    // Backend-relative path like /api/files/abc.png needs the API origin in front.
    if (v.startsWith('/')) {
      return `${BACKEND_ORIGIN}${v}`;
    }
    // Legacy value already under assets/: leave it alone.
    if (v.startsWith('assets/')) {
      return v;
    }
    // Bare filename like images/p.png: treat it as an assets path.
    return `assets/${v}`;
  }
}
