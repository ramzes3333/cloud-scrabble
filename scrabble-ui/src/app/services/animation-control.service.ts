import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AnimationControlService {

  private animationQueue: Animation[] = [];
  private currentAnimationSubject = new BehaviorSubject<AnimationElement | null>(null);
  private isAnimating = false;

  constructor() {}

  enqueueAnimation(animation: Animation) {
    this.animationQueue.push(animation);
    this.startAnimationsIfNeeded();
  }

  private startAnimationsIfNeeded() {
    if (!this.isAnimating && this.animationQueue.length > 0) {
      this.isAnimating = true;
      this.startNextAnimation();
    }
  }

  private startNextAnimation() {
    const nextAnimation = this.animationQueue.shift();
    if (nextAnimation !== undefined) {
      nextAnimation.elements.forEach(animation => {
        return this.currentAnimationSubject.next(animation)
      });

      setTimeout(() => {
        if (this.animationQueue.length > 0) {
          this.startNextAnimation();
        } else {
          this.isAnimating = false;
        }
      }, 1200);
    } else {
      this.currentAnimationSubject.next(null);
      this.isAnimating = false;
    }
  }

  get currentAnimation(): Observable<AnimationElement | null> {
    return this.currentAnimationSubject.asObservable();
  }
}

export interface AnimationElement {
  x: number;
  y: number;
}

export interface Animation {
  elements: AnimationElement[];
}
