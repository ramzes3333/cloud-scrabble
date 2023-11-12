import {ComponentFixture, TestBed} from '@angular/core/testing';

import {BlankDialogComponent} from './blank-dialog.component';

describe('BlankDialogComponent', () => {
  let component: BlankDialogComponent;
  let fixture: ComponentFixture<BlankDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BlankDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BlankDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
