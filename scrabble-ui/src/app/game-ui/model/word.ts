import {Element} from "../model/element";
import {Bonus} from "./bonus";

export class Word {
  public points: number;
  public elements: Element[];
  public relatedWords: Word[];
  public bonuses: Bonus[];

  constructor(points: number, elements: Element[], relatedWords: Word[], bonuses: Bonus[]) {
    this.points = points;
    this.elements = elements;
    this.relatedWords = relatedWords;
    this.bonuses = bonuses;
  }

  getWordAsString(): string {
    let word = "";
    for (const e of this.elements) {
      word += e.letter;
    }
    return word;
  }
}
