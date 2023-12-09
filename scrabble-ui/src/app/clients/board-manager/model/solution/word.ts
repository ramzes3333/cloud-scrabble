import {Element} from "./element";
import {Bonus} from "../bonus";

export interface Word {
  elements: Element[];
  points: number;
  relatedWords: Word[];
  bonuses: Bonus[];
}
