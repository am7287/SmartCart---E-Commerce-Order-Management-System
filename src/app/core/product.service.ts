import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { map, Observable, of } from 'rxjs';

import { Product, Review } from './product.model';

interface ProductResponse {
  products: Product[];
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'https://dummyjson.com/products';

  getProducts(limit = 20): Observable<Product[]> {
    return this.http
      .get<ProductResponse>(`${this.baseUrl}?limit=${limit}`)
      .pipe(map((response) => response.products ?? []));
  }

  searchProducts(query: string): Observable<Product[]> {
    const trimmed = query.trim();
    if (!trimmed) {
      return of([]);
    }
    return this.http
      .get<ProductResponse>(`${this.baseUrl}/search?q=${encodeURIComponent(trimmed)}&limit=20`)
      .pipe(map((response) => response.products ?? []));
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`);
  }

  getReviews(productId: number): Review[] {
    const names = [
      'Aarav',
      'Ishita',
      'Kabir',
      'Meera',
      'Rohan',
      'Sana',
      'Vivaan',
      'Diya',
      'Arjun',
      'Nisha',
      'Karan',
      'Maya',
      'Neel',
      'Tara',
      'Siddharth',
      'Anaya',
      'Rahul',
      'Zara',
      'Vihaan',
      'Ritika'
    ];

    const texts = [
      'Exactly what I wanted. The quality feels premium and the color is true.',
      'Arrived quickly and works well for daily use.',
      'Great value for the price. I would recommend it.',
      'Solid pick. The packaging was neat and secure.',
      'Looks even better in person. Super satisfied.',
      'The finish is smooth and the build feels sturdy.',
      'Comfortable to use and matches the description perfectly.',
      'Bought it for a gift and they loved it.',
      'Performance is reliable and the size is just right.',
      'The color and detail look fantastic in real life.'
    ];

    const base = productId % names.length;
    const first = {
      name: names[base],
      rating: 5,
      text: texts[base % texts.length]
    };
    const second = {
      name: names[(base + 5) % names.length],
      rating: 4,
      text: texts[(base + 2) % texts.length]
    };
    const third = {
      name: names[(base + 11) % names.length],
      rating: 5,
      text: texts[(base + 4) % texts.length]
    };

    return [first, second, third];
  }
}
