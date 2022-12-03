import {Element} from "./element";
import {Bonus} from "../bonus";

export class Word {
  public elements: Element[];
  public points: number;
  public relatedWords: Word[];
  public bonuses: Bonus[];

  constructor(elements: Element[], points: number, relatedWords: Word[], bonuses: Bonus[]) {
    this.elements = elements;
    this.points = points;
    this.relatedWords = relatedWords;
    this.bonuses = bonuses;
  }
}
