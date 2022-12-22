export class Letter {
  public letter: string;
  public blank: boolean;
  public points: number;

  constructor(letter: string, blank: boolean, points: number) {
    this.letter = letter;
    this.blank = blank;
    this.points = points;
  }
}
