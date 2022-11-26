export class Element {
  public x: number;
  public y: number;
  public letter: string;
  public points: number;
  public onBoard: boolean;

  constructor(x: number, y: number, letter: string, points: number, onBoard: boolean) {
    this.x = x;
    this.y = y;
    this.letter = letter;
    this.points = points;
    this.onBoard = onBoard;
  }
}
