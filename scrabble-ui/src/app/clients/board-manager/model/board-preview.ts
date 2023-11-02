import {Board} from "./board";

export interface BoardPreview {
  fields: EmptyField[];
  boardParameters: BoardParameters;
}

export interface EmptyField {
  x: number;
  y: number;
  bonus: string;
}

export interface BoardParameters {
  horizontalSize: number;
  verticalSize: number;
}
