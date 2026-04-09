export interface Product {
  id: number;
  title: string;
  description: string;
  price: number;
  rating: number;
  stock: number;
  brand?: string;
  category?: string;
  thumbnail: string;
  images: string[];
}

export interface Review {
  name: string;
  rating: number;
  text: string;
}
