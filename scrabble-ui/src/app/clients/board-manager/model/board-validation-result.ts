export class BoardValidationResult {
  public incorrectWords: CharacterSequence[];
  public orphans: CharacterWithPosition[];

  constructor(incorrectWords: CharacterSequence[], orphans: CharacterWithPosition[]) {
    this.incorrectWords = incorrectWords;
    this.orphans = orphans;
  }
}

class CharacterSequence {
  public characters: CharacterWithPosition[];

  constructor(characters: CharacterWithPosition[]) {
    this.characters = characters;
  }
}

class CharacterWithPosition {
  public x: number;
  public y: number;
  public character: string;

  constructor(x: number, y: number, character: string) {
    this.x = x;
    this.y = y;
    this.character = character;
  }
}
