export interface Player {
    id: string;
    type: Type;
    order: number;
    parameters: Map<string, string>;
}

export enum Type {
    HUMAN,
    BOT,
}
