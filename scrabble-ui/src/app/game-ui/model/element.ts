export class Element {
  public x: number;
  public y: number;
  public letter: string;
  public points: number;
  public blank: boolean;
  public onBoard: boolean;

  constructor(x: number, y: number, letter: string, points: number, blank: boolean, onBoard: boolean) {
    this.x = x;
    this.y = y;
    this.letter = letter;
    this.points = points;
    this.blank = blank;
    this.onBoard = onBoard;
  }
}
