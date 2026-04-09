import { AsyncPipe, CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';

import { CartItem, CartService } from '../core/cart.service';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CommonModule, RouterLink, AsyncPipe],
  template: `
    <section class="cart-header card">
      <div>
        <span class="chip">Cart</span>
        <h1>Your cart</h1>
        <p>Adjust quantities or remove items before checkout.</p>
      </div>
      <button class="secondary-btn" (click)="clearCart()">Clear cart</button>
    </section>

    @if (items$ | async; as items) {
      @if (items.length === 0) {
        <section class="card empty-cart">
          <h2>Your cart is empty</h2>
          <p>Browse the products and add your favorites.</p>
          <a routerLink="/products" class="primary-btn">Go to products</a>
        </section>
      } @else {
        <section class="cart-grid">
          @for (item of items; track item.product.id) {
            <article class="cart-item card">
              <img [src]="item.product.thumbnail" [alt]="item.product.title" />
              <div class="cart-info">
                <h3>{{ item.product.title }}</h3>
                <p class="muted">\${{ item.product.price }} each</p>
                <div class="qty-row">
                  <button class="qty-btn" (click)="decrease(item)">-</button>
                  <span>{{ item.quantity }}</span>
                  <button class="qty-btn" (click)="increase(item)">+</button>
                </div>
              </div>
              <div class="cart-total">
                <strong>\${{ item.product.price * item.quantity }}</strong>
                <button class="link-btn" (click)="remove(item)">Remove</button>
              </div>
            </article>
          }
        </section>

        <section class="cart-summary card">
          <div>
            <span>Total</span>
            <strong>\${{ getTotal(items) }}</strong>
          </div>
          <button class="primary-btn">Proceed to checkout</button>
        </section>
      }
    }
  `
})
export class CartPageComponent {
  private readonly cartService = inject(CartService);
  readonly items$ = this.cartService.items$;

  getTotal(items: CartItem[]): number {
    return this.cartService.getTotal(items);
  }

  increase(item: CartItem): void {
    this.cartService.updateQuantity(item.product.id, item.quantity + 1);
  }

  decrease(item: CartItem): void {
    this.cartService.updateQuantity(item.product.id, item.quantity - 1);
  }

  remove(item: CartItem): void {
    this.cartService.remove(item.product.id);
  }

  clearCart(): void {
    this.cartService.clear();
  }
}
