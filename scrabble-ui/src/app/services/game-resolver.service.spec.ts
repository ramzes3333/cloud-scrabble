import { TestBed } from '@angular/core/testing';

import { GameResolverService } from './game-resolver.service';

describe('GameResolverService', () => {
  let service: GameResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameResolverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
