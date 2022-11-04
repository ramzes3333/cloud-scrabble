import { TestBed } from '@angular/core/testing';

import { TileManagerService } from './tile-manager.service';

describe('TileManagerService', () => {
  let service: TileManagerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TileManagerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
