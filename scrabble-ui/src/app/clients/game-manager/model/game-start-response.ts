export class GameStartResponse {
  public id: number;
  public boardId: string;

  constructor(id: number, boardId: string) {
    this.id = id;
    this.boardId = boardId;
  }
}
