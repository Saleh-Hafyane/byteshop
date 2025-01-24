import { Component } from '@angular/core';
import {Router, Routes} from "@angular/router";
import {routes} from "../../app.routes";

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent {
  constructor(private router:Router) {
  }
  search(value: string) {
    this.router.navigateByUrl("/search/"+value)
  }
}
