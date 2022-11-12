export class BoardValidationResult {
  public errors: BoardValidationError[];

  constructor(errors: BoardValidationError[]) {
    this.errors = errors;
  }
}

class BoardValidationError {
  public incorrectWord: CharacterSequence;

  constructor(incorrectWord: CharacterSequence) {
    this.incorrectWord = incorrectWord;
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
