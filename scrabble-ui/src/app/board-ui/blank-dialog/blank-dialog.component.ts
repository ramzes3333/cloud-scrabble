import {Component, Inject, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-blank-dialog',
  templateUrl: './blank-dialog.component.html',
  styleUrls: ['./blank-dialog.component.css']
})
export class BlankDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<BlankDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: BlankDialogData) { }

  ngOnInit(): void {
  }

}

export interface BlankDialogData {
  charset: string[];
}
