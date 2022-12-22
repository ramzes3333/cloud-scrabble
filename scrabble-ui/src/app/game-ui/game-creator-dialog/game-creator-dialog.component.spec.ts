import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameCreatorDialogComponent } from './game-creator-dialog.component';

describe('GameCreatorDialogComponent', () => {
  let component: GameCreatorDialogComponent;
  let fixture: ComponentFixture<GameCreatorDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameCreatorDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GameCreatorDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
