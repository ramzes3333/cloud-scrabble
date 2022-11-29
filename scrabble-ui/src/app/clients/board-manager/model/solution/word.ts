import {Element} from "./element";

export class Word {
  public elements: Element[];
  public points: number;
  public relatedWords: Word[];

  constructor(elements: Element[], points: number, relatedWords: Word[]) {
    this.elements = elements;
    this.points = points;
    this.relatedWords = relatedWords;
  }
}
