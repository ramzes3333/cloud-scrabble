import {Element} from "../model/element";

export class Word {
  public points: number;
  public elements: Element[];
  public relatedWords: Word[];

  constructor(points: number, elements: Element[], relatedWords: Word[]) {
    this.points = points;
    this.elements = elements;
    this.relatedWords = relatedWords;
  }
}
