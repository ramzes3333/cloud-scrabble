export class BoardParameters {
  public horizontalSize: number;
  public verticalSize: number;
  public rackSize: number;

  constructor(horizontalSize: number, verticalSize: number, rackSize: number) {
    this.horizontalSize = horizontalSize;
    this.verticalSize = verticalSize;
    this.rackSize = rackSize;
  }
}
